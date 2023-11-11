package polytechnic.bh.PassPlatforms_Backend.Entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "course")
public class Course {

  @Id
  private String courseid;
  private String coursename;
  private String coursedesc;
  private String semaster;
  private String available;

  @ManyToOne
  @JoinColumn(name = "MAJORID", referencedColumnName = "MAJORID")
  private Major major;

//
//  public String getCourseid() {
//    return courseid;
//  }
//
//  public void setCourseid(String courseid) {
//    this.courseid = courseid;
//  }
//
//
//  public String getCoursename() {
//    return coursename;
//  }
//
//  public void setCoursename(String coursename) {
//    this.coursename = coursename;
//  }
//
//
//  public String getCoursedesc() {
//    return coursedesc;
//  }
//
//  public void setCoursedesc(String coursedesc) {
//    this.coursedesc = coursedesc;
//  }
//
//
//  public String getSemaster() {
//    return semaster;
//  }
//
//  public void setSemaster(String semaster) {
//    this.semaster = semaster;
//  }
//
//
//  public String getAvailable() {
//    return available;
//  }
//
//  public void setAvailable(String available) {
//    this.available = available;
//  }
//
//
//  public String getMajorid() {
//    return majorid;
//  }
//
//  public void setMajorid(String majorid) {
//    this.majorid = majorid;
//  }

}
