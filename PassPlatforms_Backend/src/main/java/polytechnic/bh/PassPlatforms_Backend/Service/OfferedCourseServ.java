package polytechnic.bh.PassPlatforms_Backend.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import polytechnic.bh.PassPlatforms_Backend.Dao.OfferedCourseDao;
import polytechnic.bh.PassPlatforms_Backend.Entity.OfferedCourse;
import polytechnic.bh.PassPlatforms_Backend.Entity.User;
import polytechnic.bh.PassPlatforms_Backend.Repository.CourseRepo;
import polytechnic.bh.PassPlatforms_Backend.Repository.OfferedCourseRepo;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class OfferedCourseServ
{

    @Autowired
    private OfferedCourseRepo offeredCourseRepo;

    @Autowired
    private CourseRepo courseRepo;

    @Autowired
    private UserServ userServ;

    // get all courses - not needed
    public List<OfferedCourseDao> getAllOfferedCourses()
    {
        List<OfferedCourseDao> offeredCourses = new ArrayList<>();

        for (OfferedCourse retrievedOfferedCourse : offeredCourseRepo.findAll())
        {
            offeredCourses.add(new OfferedCourseDao(retrievedOfferedCourse));
        }

        return offeredCourses;
    }

    // get all offered courses for a leader
    public List<OfferedCourseDao> getLeaderOfferedCourses(String leaderID)
    {
        List<OfferedCourseDao> offeredCourses = new ArrayList<>();

        for (OfferedCourse retrievedOfferedCourse : offeredCourseRepo.findOfferedCoursesByLeader_Userid(leaderID))
        {
            offeredCourses.add(new OfferedCourseDao(retrievedOfferedCourse));
        }

        return offeredCourses;
    }

    // get all offered courses by course (people that teach the course giving)
    public List<OfferedCourseDao> getOfferedCourseLeaders(String courseID)
    {
        List<OfferedCourseDao> offeredCourses = new ArrayList<>();

        for (OfferedCourse retrievedOfferedCourse : offeredCourseRepo.findOfferedCoursesByCourse_Courseid(courseID))
        {
            offeredCourses.add(new OfferedCourseDao(retrievedOfferedCourse));
        }

        return offeredCourses;
    }

    // get an offered course details - not really needed
    public OfferedCourseDao getOfferedCourseDetails(int offerID)
    {
        Optional<OfferedCourse> retrievedOfferedCourse = offeredCourseRepo.findById(offerID);

        return retrievedOfferedCourse.map(OfferedCourseDao::new).orElse(null);
    }

    // create a single course offer
    public OfferedCourseDao createOfferedCourse(String leaderID, String courseID)
    {
        OfferedCourse newOfferedCourse = new OfferedCourse();

        newOfferedCourse.setLeader(new User(userServ.getUser(leaderID)));
        newOfferedCourse.setCourse(courseRepo.getReferenceById(courseID));

        return new OfferedCourseDao(offeredCourseRepo.save(newOfferedCourse));
    }

    // multi course add
    public List<OfferedCourseDao> createMultiOfferedCourse(List<OfferedCourseDao> offeredCourseDaos)
    {
        List<OfferedCourseDao> newOfferedCourse = new ArrayList<>();

        for (OfferedCourseDao offeredCourseDao : offeredCourseDaos)
        {
            if (!offeredCourseRepo.existsByLeader_UseridAndCourse_Courseid(offeredCourseDao.getLeader().getUserid(), offeredCourseDao.getCourse().getCourseid()))
            {
                newOfferedCourse.add(createOfferedCourse(offeredCourseDao.getLeader().getUserid(), offeredCourseDao.getCourse().getCourseid()));
            }
            else
            {
                System.out.println("ignoring dup course");
            }
        }

        return newOfferedCourse;
    }

    // delete one course
    public boolean deleteOfferedCourse(int offerID)
    {
        offeredCourseRepo.deleteById(offerID);
        return true;
    }

    // delete by course and leader id - not needed for now
    public boolean deleteLeaderOfferedCourse(String leaderID, String courseID)
    {
        offeredCourseRepo.deleteOfferedCourseByLeader_UseridAndCourse_Courseid(leaderID, courseID);
        return true;
    }
}

