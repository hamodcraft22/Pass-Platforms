package polytechnic.bh.PassPlatforms_Backend.Repository;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import polytechnic.bh.PassPlatforms_Backend.Entity.User;

import java.util.List;

public interface UserRepo extends JpaRepository<User, String>
{
    // make a user into leader
    @Transactional
    @Modifying
    @Query(value = "update pp_user set roleid = 2 where userid = :studentID", nativeQuery = true)
    void makeLeaders(@Param("studentID") String studentID);

    // get users based on school id
    @Transactional
    @Query(value = "select * from pp_user WHERE userid in (select UNIQUE(LEADERID) from pp_offeredcourse where courseid in (select courseid from pp_course where schoolid = :schoolID))", nativeQuery = true)
    List<User> getSchoolLeaders(String schoolID);

    // get users based on course id
    @Transactional
    @Query(value = "select * from pp_user WHERE userid in (select UNIQUE(LEADERID) from pp_offeredcourse where courseid = :courseID)", nativeQuery = true)
    List<User> getCourseLeaders(String courseID);
}
