package polytechnic.bh.PassPlatforms_Backend.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import polytechnic.bh.PassPlatforms_Backend.Entity.Course;

import java.util.List;

public interface CourseRepo extends JpaRepository<Course, String> {
    List<Course> findCoursesByCoursenameContainsIgnoreCase(String name);
    List<Course> findCoursesByCoursedescContainsIgnoreCase(String description);
    List<Course> findCoursesBySemaster(char semaster);
    List<Course> findCoursesByAvailable(boolean isAvailable);
}
