package polytechnic.bh.PassPlatforms_Backend.Dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import polytechnic.bh.PassPlatforms_Backend.Entity.BookingMember;
import polytechnic.bh.PassPlatforms_Backend.Entity.BookingNote;
import polytechnic.bh.PassPlatforms_Backend.Entity.BookingStatus;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BookingMemberDto {
    private int memberid;
    private java.sql.Date datetime;
    private UserDto student;
    private BookingDto booking;
    private MemberStatusDto memberStatus;

    public BookingMemberDto(BookingMember bookingMember) {
        this.memberid = bookingMember.getMemberid();
        this.datetime = bookingMember.getDatetime();
        this.student = new UserDto(bookingMember.getStudent());
        this.memberStatus = new MemberStatusDto(bookingMember.getStatus());

        List<BookingNoteDto> bookingNotes = new ArrayList<>();
        if(!bookingMember.getBooking().getBookingNotes().isEmpty())
        {
            for (BookingNote note : bookingMember.getBooking().getBookingNotes())
            {
                bookingNotes.add(new BookingNoteDto(note.getNoteid(),
                        note.getDatetime(),
                        note.getNotebody(),
                        null,
                        new UserDto(note.getUser())));
            }
        }

        this.booking = new BookingDto(bookingMember.getBooking().getBookingid(),
                bookingMember.getBooking().getDatebooked(),
                bookingMember.getBooking().getDate(),
                bookingMember.getBooking().getNote(),
                bookingMember.getBooking().getStarttime(),
                bookingMember.getBooking().getEndtime(),
                bookingMember.getBooking().getBookinglimit(),
                bookingMember.getBooking().getIsonline(),
                bookingMember.getBooking().getIsgroup(),
                bookingMember.getBooking().getIsrevision(),
                new SlotDto(bookingMember.getBooking().getSlot()),
                new BookingStatusDto(bookingMember.getBooking().getBookingStatus()),
                new UserDto(bookingMember.getBooking().getStudent()),
                new CourseDto(bookingMember.getBooking().getCourse()),
                null,
                bookingNotes);
    }
}
