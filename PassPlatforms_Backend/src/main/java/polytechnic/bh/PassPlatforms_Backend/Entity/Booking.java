package polytechnic.bh.PassPlatforms_Backend.Entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import polytechnic.bh.PassPlatforms_Backend.Dao.BookingDao;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.List;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "pp_booking")
public class Booking
{

    @Id
    private int bookingid;
    private java.sql.Timestamp datebooked;
    private java.sql.Date bookingdate;
    private String note;
    private java.sql.Timestamp starttime;
    private java.sql.Timestamp endtime;
    private int bookinglimit;
    private boolean isonline;
    private boolean isgroup;
    private boolean isrevision;

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
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "BOOKINGID", referencedColumnName = "BOOKINGID")
    private List<BookingMember> bookingMembers;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "BOOKINGID", referencedColumnName = "BOOKINGID")
    private List<BookingNote> bookingNotes;

    public Booking(BookingDao bookingDao)
    {
        this.bookingid = bookingDao.getBookingid();
        this.datebooked = Timestamp.from(bookingDao.getDatebooked());
        this.bookingdate = new Date(bookingDao.getBookingDate().getTime());
        this.note = bookingDao.getNote();
        this.starttime = Timestamp.from(bookingDao.getStarttime());
        this.endtime = Timestamp.from(bookingDao.getEndtime());
        this.bookinglimit = bookingDao.getBookinglimit();
        this.isonline = bookingDao.isIsonline();
        this.isgroup = bookingDao.isIsgroup();
        this.isrevision = bookingDao.isIsrevision();
        this.slot = new Slot(bookingDao.getSlot());
        this.bookingStatus = new BookingStatus(bookingDao.getBookingStatus());
        this.student = new User(bookingDao.getStudent());
        this.course = new Course(bookingDao.getCourse());
    }
}
