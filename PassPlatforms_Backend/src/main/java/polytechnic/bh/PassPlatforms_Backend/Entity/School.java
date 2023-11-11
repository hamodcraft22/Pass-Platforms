package polytechnic.bh.PassPlatforms_Backend.Entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "school")
public class School {

  @Id
  private String schoolid;
  private String schoolname;
  private String schooldesc;

//
//  public String getSchoolid() {
//    return schoolid;
//  }
//
//  public void setSchoolid(String schoolid) {
//    this.schoolid = schoolid;
//  }
//
//
//  public String getSchoolname() {
//    return schoolname;
//  }
//
//  public void setSchoolname(String schoolname) {
//    this.schoolname = schoolname;
//  }
//
//
//  public String getSchooldesc() {
//    return schooldesc;
//  }
//
//  public void setSchooldesc(String schooldesc) {
//    this.schooldesc = schooldesc;
//  }

}
