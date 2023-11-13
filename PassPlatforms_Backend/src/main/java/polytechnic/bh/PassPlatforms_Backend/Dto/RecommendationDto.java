package polytechnic.bh.PassPlatforms_Backend.Dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import polytechnic.bh.PassPlatforms_Backend.Entity.Recommendation;

import java.sql.Date;
import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RecommendationDto {
    private int recid;
    private Instant datetime;
    private String note;
    private RecStatusDto recStatus;
    private UserDto student;
    private UserDto tutor;

    public RecommendationDto(Recommendation recommendation) {
        this.recid = recommendation.getRecid();
        this.datetime = recommendation.getDatetime().toInstant();
        this.note = recommendation.getNote();
        this.recStatus = new RecStatusDto(recommendation.getStatus());
        this.student = new UserDto(recommendation.getStudent());
        this.tutor = new UserDto(recommendation.getTutor());
    }
}
