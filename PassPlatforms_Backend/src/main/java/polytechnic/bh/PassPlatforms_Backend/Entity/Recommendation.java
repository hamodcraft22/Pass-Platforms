package polytechnic.bh.PassPlatforms_Backend.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import polytechnic.bh.PassPlatforms_Backend.Dao.RecommendationDao;

import java.sql.Timestamp;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "pp_recommendation")
public class Recommendation {

  @Id
  private int recid;
  private java.sql.Timestamp datetime;
  private String note;

  @ManyToOne
  @JoinColumn(name = "STATUSID", referencedColumnName = "STATUSID")
  private RecStatus status;

  @ManyToOne
  @JoinColumn(name = "TUTORID", referencedColumnName = "USERID")
  private User tutor;

  @ManyToOne
  @JoinColumn(name = "STUDENTID", referencedColumnName = "USERID")
  private User student;

  public Recommendation(RecommendationDao recommendationDao) {
    this.recid = recommendationDao.getRecid();
    this.datetime = Timestamp.from(recommendationDao.getDatetime());
    this.note = recommendationDao.getNote();
    this.status = new RecStatus(recommendationDao.getRecStatus());
    this.tutor = new User(recommendationDao.getTutor());
    this.student = new User(recommendationDao.getStudent());
  }
}
