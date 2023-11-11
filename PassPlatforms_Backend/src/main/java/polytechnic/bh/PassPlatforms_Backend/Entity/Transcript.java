package polytechnic.bh.PassPlatforms_Backend.Entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "transcript")
public class Transcript {

  @Id
  private String transid;
  private String grade;
  private String studentid;
  private String courseid;

//
//  public String getTransid() {
//    return transid;
//  }
//
//  public void setTransid(String transid) {
//    this.transid = transid;
//  }
//
//
//  public String getGrade() {
//    return grade;
//  }
//
//  public void setGrade(String grade) {
//    this.grade = grade;
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
//
//
//  public String getCourseid() {
//    return courseid;
//  }
//
//  public void setCourseid(String courseid) {
//    this.courseid = courseid;
//  }

}
