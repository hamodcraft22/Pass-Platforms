package polytechnic.bh.PassPlatforms_Backend.Entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;
import java.util.Set;

@Data
@Entity
@Table(name = "application")
public class Application {

  @Id
  private int applicationid;
  private java.sql.Date datetime;
  private String note;

  @ManyToOne
  @JoinColumn(name = "APLCSTATUSID", referencedColumnName = "STATUSID")
  private ApplicationStatus applicationStatus;

  @OneToOne
  @JoinColumn(name = "studentid", referencedColumnName = "USERID")
  private User user;

  // custom (multi item) entities
  @JsonManagedReference
  @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
  @JoinColumn(name = "APPLICATIONID", referencedColumnName = "APPLICATIONID")
  private List<ApplicationNote> applicationNotes;

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
