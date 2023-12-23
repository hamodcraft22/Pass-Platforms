package polytechnic.bh.PassPlatforms_Backend.Repository;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import polytechnic.bh.PassPlatforms_Backend.Entity.Course;

import java.util.List;

public interface CourseRepo extends JpaRepository<Course, String>
{
    // select all available courses
    @Transactional
    @Query(value = "select * from pp_course where courseid in (select UNIQUE(courseid) from pp_offeredcourse)", nativeQuery = true)
    List<Course> findAvlbCourses();

    // select all revision courses
    @Transactional
    @Query(value = "select * from pp_course where courseid in (select UNIQUE(courseid) from pp_booking where typeid = 'R')", nativeQuery = true)
    List<Course> findRevCourses();

    // get all courses that a leader can offer
    @Transactional
    @Query(value = "select * from pp_course WHERE courseid in (:courseIds)", nativeQuery = true)
    List<Course> findLeaderPosbCourses(List<String> courseIds);
}
