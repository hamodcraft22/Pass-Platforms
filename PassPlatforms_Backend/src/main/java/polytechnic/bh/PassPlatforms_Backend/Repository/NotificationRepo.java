package polytechnic.bh.PassPlatforms_Backend.Repository;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import polytechnic.bh.PassPlatforms_Backend.Entity.Notification;

import java.util.List;

public interface NotificationRepo extends JpaRepository<Notification, Integer>
{
    List<Notification> findAllByUser_Userid(String userID);

    // set notification as seen - when retrieved
    @Transactional
    @Modifying
    @Query(value = "UPDATE pp_notification SET seen = true WHERE notficid = :notfcID", nativeQuery = true)
    void setNotfcSeenByNotficid(@Param("notfcID") int notfcID);
}
