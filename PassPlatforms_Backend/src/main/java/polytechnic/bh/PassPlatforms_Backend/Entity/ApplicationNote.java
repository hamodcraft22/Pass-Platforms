package polytechnic.bh.PassPlatforms_Backend.Entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "applicationnote")
public class ApplicationNote {

  @Id
  private String noteid;
  private java.sql.Date datetime;
  private String notebody;
  private String applicationid;
  private String userid;

//
//  public String getNoteid() {
//    return noteid;
//  }
//
//  public void setNoteid(String noteid) {
//    this.noteid = noteid;
//  }
//
//
//  public java.sql.Date getDatetime() {
//    return datetime;
//  }
//
//  public void setDatetime(java.sql.Date datetime) {
//    this.datetime = datetime;
//  }
//
//
//  public String getNotebody() {
//    return notebody;
//  }
//
//  public void setNotebody(String notebody) {
//    this.notebody = notebody;
//  }
//
//
//  public String getApplicationid() {
//    return applicationid;
//  }
//
//  public void setApplicationid(String applicationid) {
//    this.applicationid = applicationid;
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

}
