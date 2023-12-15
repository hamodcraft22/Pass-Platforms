package polytechnic.bh.PassPlatforms_Backend.Repository;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import polytechnic.bh.PassPlatforms_Backend.Entity.User;

public interface UserRepo extends JpaRepository<User, String>
{
    @Transactional
    @Modifying
    @Query(value = "update pp_user set roleid = 2 where userid = :studentID", nativeQuery = true)
    void makeLeaders(@Param("studentID") String studentID);
}
