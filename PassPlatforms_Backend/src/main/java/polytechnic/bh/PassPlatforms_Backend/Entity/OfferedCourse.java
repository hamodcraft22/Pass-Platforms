package polytechnic.bh.PassPlatforms_Backend.Entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "offeredcourse")
public class OfferedCourse {

  @Id
  private String offerid;
  private String leaderid;
  private String courseid;

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
