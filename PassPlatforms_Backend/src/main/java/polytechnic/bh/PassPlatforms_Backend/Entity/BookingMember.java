package polytechnic.bh.PassPlatforms_Backend.Entity;

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
public class BookingMember
{

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "pp_member_SEQ")
    @SequenceGenerator(name = "pp_member_SEQ", sequenceName = "pp_member_SEQ", allocationSize = 1)
    private int memberid;
    private java.sql.Timestamp datetime;

    @ManyToOne
    @JoinColumn(name = "STUDENTID", referencedColumnName = "USERID")
    private User student;

    @ManyToOne
    @JoinColumn(name = "BOOKINGID", referencedColumnName = "BOOKINGID")
    private Booking booking;


    public BookingMember(BookingMemberDao bookingMemberDao)
    {
        this.memberid = bookingMemberDao.getMemberid();
        this.datetime = Timestamp.from(bookingMemberDao.getDatetime());
        this.student = new User(bookingMemberDao.getStudent());
        this.booking = new Booking(bookingMemberDao.getBooking());
    }
}
