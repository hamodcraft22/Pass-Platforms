package polytechnic.bh.PassPlatforms_Backend.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import polytechnic.bh.PassPlatforms_Backend.Entity.Notification;

import java.util.List;

public interface NotificationRepo extends JpaRepository<Notification, Integer>
{
    List<Notification> findAllByUser_Userid(String userID);

    // TODO - check if it works
    @Modifying
    @Query("update Notification set seen = 1 where notficid = :notfcID")
    void setNotfcSeenByNotficid(@Param("notfcID") int notfcID);
}
