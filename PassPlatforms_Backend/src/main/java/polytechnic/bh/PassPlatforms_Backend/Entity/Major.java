package polytechnic.bh.PassPlatforms_Backend.Entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "major")
public class Major {

  @Id
  private String majorid;
  private String majorname;
  private String majordesc;
  private String schoolid;

//
//  public String getMajorid() {
//    return majorid;
//  }
//
//  public void setMajorid(String majorid) {
//    this.majorid = majorid;
//  }
//
//
//  public String getMajorname() {
//    return majorname;
//  }
//
//  public void setMajorname(String majorname) {
//    this.majorname = majorname;
//  }
//
//
//  public String getMajordesc() {
//    return majordesc;
//  }
//
//  public void setMajordesc(String majordesc) {
//    this.majordesc = majordesc;
//  }
//
//
//  public String getSchoolid() {
//    return schoolid;
//  }
//
//  public void setSchoolid(String schoolid) {
//    this.schoolid = schoolid;
//  }

}
