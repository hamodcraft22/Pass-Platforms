package polytechnic.bh.PassPlatforms_Backend.Dao;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import polytechnic.bh.PassPlatforms_Backend.Entity.Notification;
import polytechnic.bh.PassPlatforms_Backend.Entity.Recommendation;
import polytechnic.bh.PassPlatforms_Backend.Entity.User;

import java.util.ArrayList;
import java.util.List;

import static polytechnic.bh.PassPlatforms_Backend.Util.UsersService.getAzureAdName;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TutorDao
{
    private String userid;
    private RoleDao role;

    private List<NotificationDao> notifications;
    private List<RecommendationDao> recommendations;

    public TutorDao(User user)
    {
        this.userid = user.getUserid();
        this.role = new RoleDao(user.getRole());

        List<NotificationDao> notifications = new ArrayList<>();
        for (Notification notification : user.getNotifications())
        {
            notifications.add(new NotificationDao(notification.getNotficid(), notification.getEntity(), notification.getItemid(), notification.getNotficmsg(), notification.isSeen(), null));
        }
        this.notifications = notifications;

        List<RecommendationDao> recommendations = new ArrayList<>();
        for (Recommendation recommendation : user.getRecommendations())
        {
            recommendations.add(new RecommendationDao(recommendation.getRecid(), recommendation.getDatetime().toInstant(), recommendation.getNote(), new RecStatusDao(recommendation.getStatus()), new UserDao(recommendation.getStudent().getUserid(), new RoleDao(recommendation.getStudent().getRole()), getAzureAdName(recommendation.getStudent().getUserid()), null), null));
        }
        this.recommendations = recommendations;
    }
}
