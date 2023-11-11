package polytechnic.bh.PassPlatforms_Backend.Entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Data
@Entity
@Table(name = "booking")
public class Booking {

  @Id
  private int bookingid;
  private java.sql.Date datebooked;
  private java.sql.Date date;
  private String note;
  private java.sql.Timestamp starttime;
  private java.sql.Timestamp endtime;
  private String bookinglimit;
  private String isonline;
  private String isgroup;
  private String isrevision;

  @ManyToOne
  @JoinColumn(name = "SLOTID", referencedColumnName = "SLOTID")
  private Slot slot;

  @ManyToOne
  @JoinColumn(name = "STATUSID", referencedColumnName = "STATUSID")
  private BookingStatus bookingStatus;

  @ManyToOne
  @JoinColumn(name = "STUDENTID", referencedColumnName = "USERID")
  private User student;

  @ManyToOne
  @JoinColumn(name = "COURSEID", referencedColumnName = "COURSEID")
  private Course course;

  // custom (multi item) entities
  @JsonManagedReference
  @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
  @JoinColumn(name = "BOOKINGID", referencedColumnName = "BOOKINGID")
  private List<BookingMember> bookingMembers;

  @JsonManagedReference
  @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
  @JoinColumn(name = "BOOKINGID", referencedColumnName = "BOOKINGID")
  private List<BookingNote> bookingNotes;

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
//  public java.sql.Date getDatebooked() {
//    return datebooked;
//  }
//
//  public void setDatebooked(java.sql.Date datebooked) {
//    this.datebooked = datebooked;
//  }
//
//
//  public java.sql.Date getDate() {
//    return date;
//  }
//
//  public void setDate(java.sql.Date date) {
//    this.date = date;
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
//  public java.sql.Timestamp getStarttime() {
//    return starttime;
//  }
//
//  public void setStarttime(java.sql.Timestamp starttime) {
//    this.starttime = starttime;
//  }
//
//
//  public java.sql.Timestamp getEndtime() {
//    return endtime;
//  }
//
//  public void setEndtime(java.sql.Timestamp endtime) {
//    this.endtime = endtime;
//  }
//
//
//  public String getBookinglimit() {
//    return bookinglimit;
//  }
//
//  public void setBookinglimit(String bookinglimit) {
//    this.bookinglimit = bookinglimit;
//  }
//
//
//  public String getIsonline() {
//    return isonline;
//  }
//
//  public void setIsonline(String isonline) {
//    this.isonline = isonline;
//  }
//
//
//  public String getIsgroup() {
//    return isgroup;
//  }
//
//  public void setIsgroup(String isgroup) {
//    this.isgroup = isgroup;
//  }
//
//
//  public String getIsrevision() {
//    return isrevision;
//  }
//
//  public void setIsrevision(String isrevision) {
//    this.isrevision = isrevision;
//  }
//
//
//  public String getSlotid() {
//    return slotid;
//  }
//
//  public void setSlotid(String slotid) {
//    this.slotid = slotid;
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
