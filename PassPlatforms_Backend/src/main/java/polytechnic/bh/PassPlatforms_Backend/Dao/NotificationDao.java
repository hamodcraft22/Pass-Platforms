package polytechnic.bh.PassPlatforms_Backend.Dao;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import polytechnic.bh.PassPlatforms_Backend.Entity.Notification;

import static polytechnic.bh.PassPlatforms_Backend.Util.UsersService.getAzureAdName;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class NotificationDao
{
    private int notficid;
    private String entity;
    private String itemid;
    private String notficmsg;
    private boolean seen;

    private UserDao user;

    public NotificationDao(Notification notification)
    {
        this.notficid = notification.getNotficid();
        this.entity = notification.getEntity();
        this.itemid = notification.getItemid();
        this.notficmsg = notification.getNotficmsg();
        this.seen = notification.isSeen();

        this.user = new UserDao(notification.getUser().getUserid(), new RoleDao(notification.getUser().getRole()), getAzureAdName(notification.getUser().getUserid()), null);
    }
}
