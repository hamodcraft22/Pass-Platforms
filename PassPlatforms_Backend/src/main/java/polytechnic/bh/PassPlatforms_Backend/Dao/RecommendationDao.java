package polytechnic.bh.PassPlatforms_Backend.Dao;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import polytechnic.bh.PassPlatforms_Backend.Entity.Recommendation;

import java.time.Instant;

import static polytechnic.bh.PassPlatforms_Backend.Util.UsersService.getAzureAdName;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RecommendationDao
{
    private int recid;
    private Instant datetime;
    private String note;
    private RecStatusDao recStatus;
    private UserDao student;
    private UserDao tutor;

    public RecommendationDao(Recommendation recommendation)
    {
        this.recid = recommendation.getRecid();
        this.datetime = recommendation.getDatetime().toInstant();
        this.note = recommendation.getNote();
        this.recStatus = new RecStatusDao(recommendation.getStatus());
        this.student = new UserDao(recommendation.getStudent().getUserid(), new RoleDao(recommendation.getStudent().getRole()), getAzureAdName(recommendation.getStudent().getUserid()), null);
        this.tutor = new UserDao(recommendation.getTutor().getUserid(), new RoleDao(recommendation.getTutor().getRole()), getAzureAdName(recommendation.getTutor().getUserid()), null);
    }
}
