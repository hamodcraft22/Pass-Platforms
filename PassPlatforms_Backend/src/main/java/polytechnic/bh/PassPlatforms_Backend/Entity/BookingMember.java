package polytechnic.bh.PassPlatforms_Backend.Entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "bookingmember")
public class BookingMember {

  @Id
  private String memberid;
  private java.sql.Date datetime;
  private String studentid;
  private String bookingid;
  private String statusid;

//
//  public String getMemberid() {
//    return memberid;
//  }
//
//  public void setMemberid(String memberid) {
//    this.memberid = memberid;
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
//  public String getStudentid() {
//    return studentid;
//  }
//
//  public void setStudentid(String studentid) {
//    this.studentid = studentid;
//  }
//
//
//  public String getBookingid() {
//    return bookingid;
//  }
//
//  public void setBookingid(String bookingid) {
//    this.bookingid = bookingid;
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

}
