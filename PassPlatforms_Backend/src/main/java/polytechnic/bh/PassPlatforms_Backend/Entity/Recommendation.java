package polytechnic.bh.PassPlatforms_Backend.Entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "recommendation")
public class Recommendation {

  @Id
  private String recid;
  private java.sql.Date datetime;
  private String note;
  private String statusid;
  private String tutorid;
  private String studentid;

//
//  public String getRecid() {
//    return recid;
//  }
//
//  public void setRecid(String recid) {
//    this.recid = recid;
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
//  public String getStatusid() {
//    return statusid;
//  }
//
//  public void setStatusid(String statusid) {
//    this.statusid = statusid;
//  }
//
//
//  public String getTutorid() {
//    return tutorid;
//  }
//
//  public void setTutorid(String tutorid) {
//    this.tutorid = tutorid;
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
