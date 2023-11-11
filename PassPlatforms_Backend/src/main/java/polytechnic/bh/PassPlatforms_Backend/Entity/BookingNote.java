package polytechnic.bh.PassPlatforms_Backend.Entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "bookingnote")
public class BookingNote {

  @Id
  private int noteid;
  private java.sql.Date datetime;
  private String notebody;

  @JsonBackReference
  @ManyToOne
  @JoinColumn(name = "BOOKINGID", referencedColumnName = "BOOKINGID")
  private Booking booking;

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
//  public String getBookingid() {
//    return bookingid;
//  }
//
//  public void setBookingid(String bookingid) {
//    this.bookingid = bookingid;
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
