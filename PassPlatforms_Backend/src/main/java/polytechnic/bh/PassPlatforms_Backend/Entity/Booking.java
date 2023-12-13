package polytechnic.bh.PassPlatforms_Backend.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Cascade;
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
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "pp_booking_SEQ")
    @SequenceGenerator(name = "pp_booking_SEQ", sequenceName = "pp_booking_SEQ", allocationSize = 1)
    private int bookingid;
    private java.sql.Timestamp datebooked;
    private java.sql.Date bookingdate;
    private String note;
    private java.sql.Timestamp starttime;
    private java.sql.Timestamp endtime;
    private int bookinglimit;
    private boolean isonline;

    @ManyToOne
    @JoinColumn(name = "TYPEID", referencedColumnName = "TYPEID")
    private BookingType bookingType;

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
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE, orphanRemoval = true, mappedBy = "booking")
    @Cascade(org.hibernate.annotations.CascadeType.DELETE_ORPHAN)
    private List<BookingMember> bookingMembers;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE, orphanRemoval = true, mappedBy = "booking")
    @Cascade(org.hibernate.annotations.CascadeType.DELETE_ORPHAN)
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
        this.bookingType = new BookingType(bookingDao.getBookingType());
        this.slot = new Slot(bookingDao.getSlot());
        this.bookingStatus = new BookingStatus(bookingDao.getBookingStatus());
        this.student = new User(bookingDao.getStudent());
        this.course = new Course(bookingDao.getCourse());
    }
}
