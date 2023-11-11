package polytechnic.bh.PassPlatforms_Backend.Entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "offeredcourse")
public class OfferedCourse {

  @Id
  private int offerid;

  @ManyToOne
  @JoinColumn(name = "LEADERID", referencedColumnName = "USERID")
  private User leader;

  @ManyToOne
  @JoinColumn(name = "COURSEID", referencedColumnName = "COURSEID")
  private Course course;

//
//  public String getOfferid() {
//    return offerid;
//  }
//
//  public void setOfferid(String offerid) {
//    this.offerid = offerid;
//  }
//
//
//  public String getLeaderid() {
//    return leaderid;
//  }
//
//  public void setLeaderid(String leaderid) {
//    this.leaderid = leaderid;
//  }
//
//
//  public String getCourseid() {
//    return courseid;
//  }
//
//  public void setCourseid(String courseid) {
//    this.courseid = courseid;
//  }

}
