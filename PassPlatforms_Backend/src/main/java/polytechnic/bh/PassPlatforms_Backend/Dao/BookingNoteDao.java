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
public class BookingNoteDao
{
    private int noteid;
    private Instant datetime;
    private String notebody;
    private BookingDao booking;
    private UserDao user;

    public BookingNoteDao(BookingNote bookingNote)
    {
        this.noteid = bookingNote.getNoteid();
        this.datetime = bookingNote.getDatetime().toInstant();
        this.notebody = bookingNote.getNotebody();
        this.user = new UserDao(bookingNote.getUser().getUserid(), new RoleDao(bookingNote.getUser().getRole()), getAzureAdName(bookingNote.getUser().getUserid()), null);

        List<BookingMemberDao> bookingMembers = new ArrayList<>();
        if (!bookingNote.getBooking().getBookingMembers().isEmpty())
        {
            for (BookingMember member : bookingNote.getBooking().getBookingMembers())
            {
                bookingMembers.add(new BookingMemberDao(member.getMemberid(),
                        member.getDatetime().toInstant(),
                        new UserDao(member.getStudent().getUserid(), new RoleDao(member.getStudent().getRole()), getAzureAdName(member.getStudent().getUserid()), null),
                        null));
            }
        }

        this.booking = new BookingDao(bookingNote.getBooking().getBookingid(),
                bookingNote.getBooking().getDatebooked().toInstant(),
                bookingNote.getBooking().getBookingdate(),
                bookingNote.getBooking().getNote(),
                bookingNote.getBooking().getStarttime() != null ? (bookingNote.getBooking().getStarttime().toInstant()) : (null),
                bookingNote.getBooking().getEndtime() != null ? (bookingNote.getBooking().getEndtime().toInstant()) : (null),
                bookingNote.getBooking().getBookinglimit(),
                bookingNote.getBooking().isIsonline(),
                new BookingTypeDao(bookingNote.getBooking().getBookingType()),
                bookingNote.getBooking().getSlot() != null ? (new SlotDao(bookingNote.getBooking().getSlot())) : (null),
                new BookingStatusDao(bookingNote.getBooking().getBookingStatus()),
                new UserDao(bookingNote.getBooking().getStudent().getUserid(), new RoleDao(bookingNote.getBooking().getStudent().getRole()), getAzureAdName(bookingNote.getBooking().getStudent().getUserid()), null),
                new CourseDao(bookingNote.getBooking().getCourse()),
                bookingMembers,
                null);


    }
}
