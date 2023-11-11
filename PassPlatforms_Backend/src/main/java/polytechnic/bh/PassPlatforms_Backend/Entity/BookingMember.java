package polytechnic.bh.PassPlatforms_Backend.Entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "bookingmember")
public class BookingMember {

  @Id
  private int memberid;
  private java.sql.Date datetime;

  @ManyToOne
  @JoinColumn(name = "STUDENTID", referencedColumnName = "USERID")
  private User student;

  @JsonBackReference
  @ManyToOne
  @JoinColumn(name = "BOOKINGID", referencedColumnName = "BOOKINGID")
  private Booking booking;

  @ManyToOne
  @JoinColumn(name = "STATUSID", referencedColumnName = "STATUSID")
  private MemberStatus status;

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
