package polytechnic.bh.PassPlatforms_Backend.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import polytechnic.bh.PassPlatforms_Backend.Dao.BookingNoteDao;

import java.sql.Timestamp;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "pp_bookingnote")
public class BookingNote
{

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "pp_bnote_SEQ")
    @SequenceGenerator(name = "pp_bnote_SEQ", sequenceName = "pp_bnote_SEQ", allocationSize = 1)
    private int noteid;
    private java.sql.Timestamp datetime;
    private String notebody;

    @ManyToOne
    @JoinColumn(name = "BOOKINGID", referencedColumnName = "BOOKINGID")
    private Booking booking;

    @ManyToOne
    @JoinColumn(name = "USERID", referencedColumnName = "USERID")
    private User user;

    public BookingNote(BookingNoteDao bookingNoteDao)
    {
        this.noteid = bookingNoteDao.getNoteid();
        this.datetime = Timestamp.from(bookingNoteDao.getDatetime());
        this.notebody = bookingNoteDao.getNotebody();
        this.booking = new Booking(bookingNoteDao.getBooking());
        this.user = new User(bookingNoteDao.getUser());
    }
}
