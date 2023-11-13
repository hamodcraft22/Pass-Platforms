package polytechnic.bh.PassPlatforms_Backend.Dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import polytechnic.bh.PassPlatforms_Backend.Entity.BookingMember;
import polytechnic.bh.PassPlatforms_Backend.Entity.BookingNote;
import polytechnic.bh.PassPlatforms_Backend.Entity.BookingStatus;

import java.sql.Date;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BookingMemberDto {
    private int memberid;
    private Instant datetime;
    private UserDto student;
    private BookingDto booking;
    private MemberStatusDto memberStatus;

    public BookingMemberDto(BookingMember bookingMember) {
        this.memberid = bookingMember.getMemberid();
        this.datetime = bookingMember.getDatetime().toInstant();
        this.student = new UserDto(bookingMember.getStudent());
        this.memberStatus = new MemberStatusDto(bookingMember.getStatus());

        List<BookingNoteDto> bookingNotes = new ArrayList<>();
        if(!bookingMember.getBooking().getBookingNotes().isEmpty())
        {
            for (BookingNote note : bookingMember.getBooking().getBookingNotes())
            {
                bookingNotes.add(new BookingNoteDto(note.getNoteid(),
                        note.getDatetime().toInstant(),
                        note.getNotebody(),
                        null,
                        new UserDto(note.getUser())));
            }
        }

        this.booking = new BookingDto(bookingMember.getBooking().getBookingid(),
                bookingMember.getBooking().getDatebooked().toInstant(),
                bookingMember.getBooking().getBookingdate(),
                bookingMember.getBooking().getNote(),
                bookingMember.getBooking().getStarttime().toInstant(),
                bookingMember.getBooking().getEndtime().toInstant(),
                bookingMember.getBooking().getBookinglimit(),
                bookingMember.getBooking().isIsonline(),
                bookingMember.getBooking().isIsgroup(),
                bookingMember.getBooking().isIsrevision(),
                new SlotDto(bookingMember.getBooking().getSlot()),
                new BookingStatusDto(bookingMember.getBooking().getBookingStatus()),
                new UserDto(bookingMember.getBooking().getStudent()),
                new CourseDto(bookingMember.getBooking().getCourse()),
                null,
                bookingNotes);
    }
}
