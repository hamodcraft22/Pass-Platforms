package polytechnic.bh.PassPlatforms_Backend.Entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "studentmajor")
public class StudentMajor {

  @Id
  private int stumajorid;
  private String isminor;

  @ManyToOne
  @JoinColumn(name = "USERID", referencedColumnName = "USERID")
  private User user;

  @ManyToOne
  @JoinColumn(name = "MAJORID", referencedColumnName = "MAJORID")
  private Major major;

//
//  public String getStumajorid() {
//    return stumajorid;
//  }
//
//  public void setStumajorid(String stumajorid) {
//    this.stumajorid = stumajorid;
//  }
//
//
//  public String getIsminor() {
//    return isminor;
//  }
//
//  public void setIsminor(String isminor) {
//    this.isminor = isminor;
//  }
//
//
//  public String getUserid() {
//    return userid;
//  }
//
//  public void setUserid(String userid) {
//    this.userid = userid;
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
