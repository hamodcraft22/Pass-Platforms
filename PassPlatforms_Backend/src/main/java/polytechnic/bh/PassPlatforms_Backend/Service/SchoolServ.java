package polytechnic.bh.PassPlatforms_Backend.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import polytechnic.bh.PassPlatforms_Backend.Dao.MajorDao;
import polytechnic.bh.PassPlatforms_Backend.Dao.SchoolDao;
import polytechnic.bh.PassPlatforms_Backend.Entity.School;
import polytechnic.bh.PassPlatforms_Backend.Repository.SchoolRepo;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class SchoolServ
{

    @Autowired
    private SchoolRepo schoolRepo;

    public List<SchoolDao> getAllSchools()
    {
        List<SchoolDao> schools = new ArrayList<>();

        for (School retrievedSchool : schoolRepo.findAll())
        {
            schools.add(new SchoolDao(retrievedSchool));
        }

        return schools;
    }

    public SchoolDao getSchoolDetails(String schoolID)
    {
        Optional<School> retrievedSchool = schoolRepo.findById(schoolID);

        return retrievedSchool.map(SchoolDao::new).orElse(null);
    }

    public SchoolDao createSchool(String schoolName, String schoolDesc)
    {
        School newSchool = new School();

        newSchool.setSchoolname(schoolName);
        newSchool.setSchooldesc(schoolDesc);

        return new SchoolDao(schoolRepo.save(newSchool));
    }

    public SchoolDao editSchool(SchoolDao updatedSchool)
    {
        Optional<School> retrievedSchool = schoolRepo.findById(updatedSchool.getSchoolid());

        if (retrievedSchool.isPresent())
        {
            retrievedSchool.get().setSchoolname(updatedSchool.getSchoolname());
            retrievedSchool.get().setSchooldesc(updatedSchool.getSchooldesc());

            return new SchoolDao(schoolRepo.save(retrievedSchool.get()));
        }
        else
        {
            return null;
        }
    }

    public boolean deleteSchool(String schoolID)
    {
        schoolRepo.deleteById(schoolID);
        return true;
    }
}

