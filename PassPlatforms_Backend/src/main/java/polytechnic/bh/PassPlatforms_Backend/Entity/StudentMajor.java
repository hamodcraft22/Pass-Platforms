package polytechnic.bh.PassPlatforms_Backend.Entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "studentmajor")
public class StudentMajor {

  @Id
  private String stumajorid;
  private String isminor;
  private String userid;
  private String majorid;

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
