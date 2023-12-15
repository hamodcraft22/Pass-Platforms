package polytechnic.bh.PassPlatforms_Backend.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import polytechnic.bh.PassPlatforms_Backend.Dao.NotificationDao;
import polytechnic.bh.PassPlatforms_Backend.Entity.Notification;
import polytechnic.bh.PassPlatforms_Backend.Repository.NotificationRepo;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service

public class NotificationServ
{
    @Autowired
    private NotificationRepo notificationRepo;

    // get all notifications for a user
    public List<NotificationDao> getUserNotfs(String userID)
    {
        List<NotificationDao> notifications = new ArrayList<>();

        for (Notification notification : notificationRepo.findAllByUser_Userid(userID))
        {
            notifications.add(new NotificationDao(notification));
        }

        return notifications;
    }

    // get one notification info
    public NotificationDao getNotifc(int notefcID)
    {
        Optional<Notification> notification = notificationRepo.findById(notefcID);

        if (notification.isPresent())
        {
            setAsSeen(notefcID);
            return new NotificationDao(notification.get());
        }
        else
        {
            return null;
        }
    }

    // set notification as read
    public void setAsSeen(int notfcID)
    {
        notificationRepo.setNotfcSeenByNotficid(notfcID);
    }
}
