package polytechnic.bh.PassPlatforms_Backend.Dao;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import polytechnic.bh.PassPlatforms_Backend.Entity.Booking;
import polytechnic.bh.PassPlatforms_Backend.Entity.BookingMember;
import polytechnic.bh.PassPlatforms_Backend.Entity.BookingNote;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BookingDao
{
    private int bookingid;
    private Instant datebooked;
    private Date bookingDate;
    private String note;
    private Instant starttime; // for leader - when did it start irl
    private Instant endtime; // for leader - when did it start irl
    private int bookinglimit; // only for revision sessions
    private boolean isonline;
    private BookingTypeDao bookingType;
    private SlotDao slot;
    private BookingStatusDao bookingStatus;
    private UserDao student;
    private CourseDao course;
    private List<BookingMemberDao> bookingMembers;
    private List<BookingNoteDao> bookingNotes;

    public BookingDao(Booking booking)
    {
        this.bookingid = booking.getBookingid();
        this.datebooked = booking.getDatebooked().toInstant();
        this.bookingDate = booking.getBookingdate();
        this.note = booking.getNote();
        this.starttime = booking.getStarttime().toInstant();
        this.endtime = booking.getEndtime().toInstant();
        this.bookinglimit = booking.getBookinglimit();
        this.isonline = booking.isIsonline();

        this.bookingType = new BookingTypeDao(booking.getBookingType());
        this.slot = new SlotDao(booking.getSlot());
        this.bookingStatus = new BookingStatusDao(booking.getBookingStatus());
        this.student = new UserDao(booking.getStudent().getUserid(), new RoleDao(booking.getStudent().getRole()), null);
        this.course = new CourseDao(booking.getCourse());

        List<BookingMemberDao> bookingMembers = new ArrayList<>();
        if (!booking.getBookingMembers().isEmpty())
        {
            for (BookingMember member : booking.getBookingMembers())
            {
                bookingMembers.add(new BookingMemberDao(member.getMemberid(),
                        member.getDatetime().toInstant(),
                        new UserDao(member.getStudent().getUserid(), new RoleDao(member.getStudent().getRole()), null),
                        null));
            }
        }

        List<BookingNoteDao> bookingNotes = new ArrayList<>();
        if (!booking.getBookingNotes().isEmpty())
        {
            for (BookingNote note : booking.getBookingNotes())
            {
                bookingNotes.add(new BookingNoteDao(note.getNoteid(),
                        note.getDatetime().toInstant(),
                        note.getNotebody(),
                        null,
                        new UserDao(note.getUser().getUserid(), new RoleDao(note.getUser().getRole()), null)));
            }
        }

        this.bookingMembers = bookingMembers;
        this.bookingNotes = bookingNotes;
    }
}
