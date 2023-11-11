package polytechnic.bh.PassPlatforms_Backend.Entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "application")
public class Application {

  @Id
  private int applicationid;
  private java.sql.Date datetime;
  private String note;

  private String aplcstatusid;
  private String studentid;

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
//  public java.sql.Date getDatetime() {
//    return datetime;
//  }
//
//  public void setDatetime(java.sql.Date datetime) {
//    this.datetime = datetime;
//  }
//
//
//  public String getNote() {
//    return note;
//  }
//
//  public void setNote(String note) {
//    this.note = note;
//  }
//
//
//  public String getAplcstatusid() {
//    return aplcstatusid;
//  }
//
//  public void setAplcstatusid(String aplcstatusid) {
//    this.aplcstatusid = aplcstatusid;
//  }
//
//
//  public String getStudentid() {
//    return studentid;
//  }
//
//  public void setStudentid(String studentid) {
//    this.studentid = studentid;
//  }

}
