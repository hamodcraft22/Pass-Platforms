package polytechnic.bh.PassPlatforms_Backend.Entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import polytechnic.bh.PassPlatforms_Backend.Dao.BookingMemberDao;

import java.sql.Timestamp;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "pp_bookingmember")
public class BookingMember {

  @Id
  private int memberid;
  private java.sql.Timestamp datetime;

  @ManyToOne
  @JoinColumn(name = "STUDENTID", referencedColumnName = "USERID")
  private User student;

  @ManyToOne
  @JoinColumn(name = "BOOKINGID", referencedColumnName = "BOOKINGID")
  private Booking booking;

  @ManyToOne
  @JoinColumn(name = "STATUSID", referencedColumnName = "STATUSID")
  private MemberStatus status;

  public BookingMember(BookingMemberDao bookingMemberDao) {
    this.memberid = bookingMemberDao.getMemberid();
    this.datetime = Timestamp.from(bookingMemberDao.getDatetime());
    this.student = new User(bookingMemberDao.getStudent());
    this.booking = new Booking(bookingMemberDao.getBooking());
    this.status = new MemberStatus(bookingMemberDao.getMemberStatus());
  }
}
