package polytechnic.bh.PassPlatforms_Backend.Entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "applicationstatus")
public class ApplicationStatus {

  @Id
  private char statusid;
  private String statusname;
  private String statusdesc;

//
//  public String getStatusid() {
//    return statusid;
//  }
//
//  public void setStatusid(String statusid) {
//    this.statusid = statusid;
//  }
//
//
//  public String getStatusname() {
//    return statusname;
//  }
//
//  public void setStatusname(String statusname) {
//    this.statusname = statusname;
//  }
//
//
//  public String getStatusdesc() {
//    return statusdesc;
//  }
//
//  public void setStatusdesc(String statusdesc) {
//    this.statusdesc = statusdesc;
//  }

}
