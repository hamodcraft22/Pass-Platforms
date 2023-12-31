package polytechnic.bh.PassPlatforms_Backend.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import polytechnic.bh.PassPlatforms_Backend.Dao.CourseDao;
import polytechnic.bh.PassPlatforms_Backend.Dao.SchoolDao;
import polytechnic.bh.PassPlatforms_Backend.Entity.School;
import polytechnic.bh.PassPlatforms_Backend.Repository.SchoolRepo;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class SchoolServ
{

    @Autowired
    private SchoolRepo schoolRepo;

    @Autowired
    private CourseServ courseServ;

    // get all of the schools
    public List<SchoolDao> getAllSchools()
    {
        List<SchoolDao> schools = new ArrayList<>();

        for (School retrievedSchool : schoolRepo.findAll())
        {
            schools.add(new SchoolDao(retrievedSchool));
        }

        return schools;
    }

    // get the revision schools - schoole that have revision offered
    public List<SchoolDao> getAllRevSchools()
    {
        List<SchoolDao> schools = new ArrayList<>();

        List<CourseDao> revCourses = courseServ.getRevCourses();

        for (School revSchool : schoolRepo.findRevSchools())
        {
            SchoolDao newRevSchool = new SchoolDao();

            newRevSchool.setSchoolid(revSchool.getSchoolid());
            newRevSchool.setSchoolname(revSchool.getSchoolname());

            List<CourseDao> schoolRevCourses = new ArrayList<>();

            List<CourseDao> leftRevCourses = new ArrayList<>();

            // loop to school courses
            for (CourseDao course : revCourses)
            {
                if (Objects.equals(course.getSchool().getSchoolid(), revSchool.getSchoolid()))
                {
                    schoolRevCourses.add(new CourseDao(course.getCourseid(), course.getCoursename(), null));
                }
                else
                {
                    leftRevCourses.add(course);
                }
            }

            // assigning the left courses
            revCourses = leftRevCourses;

            // saving to the list
            newRevSchool.setCourses(schoolRevCourses);

            schools.add(newRevSchool);
        }

        return schools;
    }

    // get school which are being offered by leaders
    public List<SchoolDao> getAllAvlbSchools()
    {
        List<SchoolDao> schools = new ArrayList<>();

        List<CourseDao> avlbCourses = courseServ.getAvlbCourses();

        for (School revSchool : schoolRepo.findAvlbSchools())
        {
            SchoolDao newAvlbSchool = new SchoolDao();

            newAvlbSchool.setSchoolid(revSchool.getSchoolid());
            newAvlbSchool.setSchoolname(revSchool.getSchoolname());

            List<CourseDao> schoolAvlbCourses = new ArrayList<>();

            List<CourseDao> leftRevCourses = new ArrayList<>();

            // loop to school courses
            for (CourseDao course : avlbCourses)
            {
                if (Objects.equals(course.getSchool().getSchoolid(), revSchool.getSchoolid()))
                {
                    schoolAvlbCourses.add(new CourseDao(course.getCourseid(), course.getCoursename(), null));
                }
                else
                {
                    leftRevCourses.add(course);
                }
            }

            // assigning the left courses
            avlbCourses = leftRevCourses;

            // saving to the list
            newAvlbSchool.setCourses(schoolAvlbCourses);

            schools.add(newAvlbSchool);
        }

        return schools;
    }

    // get a single school information - not really needed
    public SchoolDao getSchoolDetails(String schoolID)
    {
        Optional<School> retrievedSchool = schoolRepo.findById(schoolID);

        return retrievedSchool.map(SchoolDao::new).orElse(null);
    }

    // create a single school
    public SchoolDao createSchool(String schoolID, String schoolName, List<CourseDao> courses)
    {
        School newSchool = new School();

        newSchool.setSchoolid(schoolID);
        newSchool.setSchoolname(schoolName);

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

    // create multi schools - used by uplaod function
    public List<SchoolDao> createMultiSchool(List<SchoolDao> schools)
    {
        List<SchoolDao> addedSchools = new ArrayList<>();

        for (SchoolDao schoolDao : schools)
        {
            try
            {
                School newSchool = new School();

                newSchool.setSchoolid(schoolDao.getSchoolid());
                newSchool.setSchoolname(schoolDao.getSchoolname());

                if (schoolDao.getCourses() != null && !schoolDao.getCourses().isEmpty())
                {
                    // save the school first
                    SchoolDao savedSchool = new SchoolDao(schoolRepo.save(newSchool));

                    // add all the courses to the school
                    List<CourseDao> savedCourses = courseServ.createMultiCourse(schoolDao.getCourses());

                    // add the courses to the object
                    savedSchool.setCourses(savedCourses);

                    // return
                    addedSchools.add(savedSchool);
                }
                else
                {
                    addedSchools.add(new SchoolDao(schoolRepo.save(newSchool)));
                }
            }
            catch (Exception ex)
            {
                System.out.println("ignored dup school");
            }
        }

        return addedSchools;
    }

    // edit school information - name only
    public SchoolDao editSchool(SchoolDao updatedSchool)
    {
        Optional<School> retrievedSchool = schoolRepo.findById(updatedSchool.getSchoolid());

        if (retrievedSchool.isPresent())
        {
            retrievedSchool.get().setSchoolname(updatedSchool.getSchoolname());

            return new SchoolDao(schoolRepo.save(retrievedSchool.get()));
        }
        else
        {
            return null;
        }
    }

    // delete a school - audited function
    public boolean deleteSchool(String schoolID)
    {
        schoolRepo.deleteById(schoolID);
        return true;
    }
}

