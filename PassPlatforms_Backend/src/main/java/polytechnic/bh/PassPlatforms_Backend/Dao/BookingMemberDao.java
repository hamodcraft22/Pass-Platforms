package polytechnic.bh.PassPlatforms_Backend.Dao;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import polytechnic.bh.PassPlatforms_Backend.Entity.BookingMember;
import polytechnic.bh.PassPlatforms_Backend.Entity.BookingNote;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import static polytechnic.bh.PassPlatforms_Backend.Util.UsersService.getAzureAdName;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BookingMemberDao
{
    private int memberid;
    private Instant datetime;
    private UserDao student;
    private BookingDao booking;

    public BookingMemberDao(BookingMember bookingMember)
    {
        this.memberid = bookingMember.getMemberid();
        this.datetime = bookingMember.getDatetime().toInstant();
        this.student = new UserDao(bookingMember.getStudent().getUserid(), new RoleDao(bookingMember.getStudent().getRole()), getAzureAdName(bookingMember.getStudent().getUserid()), null);

        List<BookingNoteDao> bookingNotes = new ArrayList<>();
        if (bookingMember.getBooking().getBookingNotes()!= null &&  !bookingMember.getBooking().getBookingNotes().isEmpty())
        {
            for (BookingNote note : bookingMember.getBooking().getBookingNotes())
            {
                bookingNotes.add(new BookingNoteDao(note.getNoteid(),
                        note.getDatetime().toInstant(),
                        note.getNotebody(),
                        null,
                        new UserDao(note.getUser().getUserid(), new RoleDao(note.getUser().getRole()), getAzureAdName(note.getUser().getUserid()), null)));
            }
        }

        this.booking = new BookingDao(bookingMember.getBooking().getBookingid(),
                bookingMember.getBooking().getDatebooked().toInstant(),
                bookingMember.getBooking().getBookingdate(),
                bookingMember.getBooking().getNote(),
                bookingMember.getBooking().getStarttime() != null ? (bookingMember.getBooking().getStarttime().toInstant()):(null),
                bookingMember.getBooking().getEndtime() != null ? (bookingMember.getBooking().getEndtime().toInstant()):(null),
                bookingMember.getBooking().getBookinglimit(),
                bookingMember.getBooking().isIsonline(),
                new BookingTypeDao(bookingMember.getBooking().getBookingType()),
                bookingMember.getBooking().getSlot() != null ? (new SlotDao(bookingMember.getBooking().getSlot())):(null),
                new BookingStatusDao(bookingMember.getBooking().getBookingStatus()),
                new UserDao(bookingMember.getBooking().getStudent().getUserid(), new RoleDao(bookingMember.getBooking().getStudent().getRole()), getAzureAdName(bookingMember.getBooking().getStudent().getUserid()), null),
                new CourseDao(bookingMember.getBooking().getCourse()),
                null,
                null);
    }
}
