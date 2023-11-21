package polytechnic.bh.PassPlatforms_Backend.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import polytechnic.bh.PassPlatforms_Backend.Dao.MajorDao;
import polytechnic.bh.PassPlatforms_Backend.Entity.Major;
import polytechnic.bh.PassPlatforms_Backend.Repository.MajorRepo;
import polytechnic.bh.PassPlatforms_Backend.Repository.SchoolRepo;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class MajorServ
{

    @Autowired
    private MajorRepo majorRepo;

    @Autowired
    private SchoolRepo schoolRepo;

    public List<MajorDao> getAllMajors()
    {
        List<MajorDao> majors = new ArrayList<>();

        for (Major retrievedMajor : majorRepo.findAll())
        {
            majors.add(new MajorDao(retrievedMajor));
        }

        return majors;
    }

    public MajorDao getMajorDetails(String majorID)
    {
        Optional<Major> retrievedMajor = majorRepo.findById(majorID);

        return retrievedMajor.map(MajorDao::new).orElse(null);
    }

    public MajorDao createMajor(String majorName, String majorDesc, String schoolID)
    {
        Major newMajor = new Major();

        newMajor.setMajorname(majorName);
        newMajor.setMajordesc(majorDesc);
        newMajor.setSchool(schoolRepo.getReferenceById(schoolID));

        return new MajorDao(majorRepo.save(newMajor));
    }

    public MajorDao editMajor(MajorDao updatedMajor)
    {
        Optional<Major> retrievedMajor = majorRepo.findById(updatedMajor.getMajorid());

        if (retrievedMajor.isPresent())
        {
            retrievedMajor.get().setMajorname(updatedMajor.getMajorname());
            retrievedMajor.get().setMajordesc(updatedMajor.getMajordesc());
            retrievedMajor.get().setSchool(schoolRepo.getReferenceById(updatedMajor.getSchool().getSchoolid()));

            return new MajorDao(majorRepo.save(retrievedMajor.get()));
        }
        else
        {
            return null;
        }
    }

    public boolean deleteMajor(String majorID)
    {
        majorRepo.deleteById(majorID);
        return true;
    }
}

