package polytechnic.bh.PassPlatforms_Backend.Entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "applicationnote")
public class ApplicationNote {

  @Id
  private int noteid;
  private java.sql.Date datetime;
  private String notebody;

  @JsonBackReference
  @ManyToOne
  @JoinColumn(name = "APPLICATIONID", referencedColumnName = "APPLICATIONID")
  private Application application;

  @ManyToOne
  @JoinColumn(name = "USERID", referencedColumnName = "USERID")
  private User user;

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
