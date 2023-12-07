package polytechnic.bh.PassPlatforms_Backend.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import polytechnic.bh.PassPlatforms_Backend.Dao.CourseDao;
import polytechnic.bh.PassPlatforms_Backend.Dao.SchoolDao;
import polytechnic.bh.PassPlatforms_Backend.Entity.Course;
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

    @Autowired
    private CourseServ courseServ;

    public List<SchoolDao> getAllSchools()
    {
        List<SchoolDao> schools = new ArrayList<>();

        for (School retrievedSchool : schoolRepo.findAll())
        {
            schools.add(new SchoolDao(retrievedSchool.getSchoolid(),retrievedSchool.getSchoolname(),retrievedSchool.getSchooldesc(),null));
        }

        return schools;
    }

    public SchoolDao getSchoolDetails(String schoolID)
    {
        Optional<School> retrievedSchool = schoolRepo.findById(schoolID);

        return retrievedSchool.map(SchoolDao::new).orElse(null);
    }

    public SchoolDao createSchool(String schoolID, String schoolName, String schoolDesc, List<CourseDao> courses)
    {
        School newSchool = new School();

        newSchool.setSchoolid(schoolID);
        newSchool.setSchoolname(schoolName);
        newSchool.setSchooldesc(schoolDesc);

        if (courses != null && !courses.isEmpty())
        {
            // save the school first
            SchoolDao savedSchool = new SchoolDao(schoolRepo.save(newSchool));

            // add all the courses to the school
            List<CourseDao> savedCourses = courseServ.createMultiCourse(courses);

            // add the courses to the object
            savedSchool.setCourses(savedCourses);

            // return
            return (savedSchool);
        }


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

