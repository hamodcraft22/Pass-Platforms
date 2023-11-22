package polytechnic.bh.PassPlatforms_Backend.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import polytechnic.bh.PassPlatforms_Backend.Entity.OfferedCourse;

import java.util.List;

public interface OfferedCourseRepo extends JpaRepository<OfferedCourse, Integer>
{
    List<OfferedCourse> findOfferedCoursesByLeader_Userid(String leaderID);

    List<OfferedCourse> findOfferedCoursesByCourse_Courseid(String courseID);

    boolean existsByLeader_UseridAndCourse_Courseid(String leaderID, String courseID);

    void deleteOfferedCourseByLeader_UseridAndCourse_Courseid(String leaderID, String courseID);
}
