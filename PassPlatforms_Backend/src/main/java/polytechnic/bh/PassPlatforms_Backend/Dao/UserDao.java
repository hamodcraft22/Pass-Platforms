package polytechnic.bh.PassPlatforms_Backend.Dao;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import polytechnic.bh.PassPlatforms_Backend.Entity.Notification;
import polytechnic.bh.PassPlatforms_Backend.Entity.User;

import java.util.ArrayList;
import java.util.List;

import static polytechnic.bh.PassPlatforms_Backend.Util.UsersService.getAzureAdName;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserDao
{
    private String userid;
    private RoleDao role;
    private String userName;
    private List<NotificationDao> notifications;

    public UserDao(User user)
    {
        this.userid = user.getUserid();
        this.role = new RoleDao(user.getRole());
        this.userName = getAzureAdName(user.getUserid());

        List<NotificationDao> notificationDaos = new ArrayList<>();
        for (Notification notification : user.getNotifications())
        {
            notificationDaos.add(new NotificationDao(notification.getNotficid(), notification.getEntity(), notification.getItemid(), notification.getNotficmsg(), notification.isSeen(), null));
        }
        this.notifications = notificationDaos;
    }
}
