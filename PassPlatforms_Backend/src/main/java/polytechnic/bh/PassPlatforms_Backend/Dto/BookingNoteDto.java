package polytechnic.bh.PassPlatforms_Backend.Dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import polytechnic.bh.PassPlatforms_Backend.Entity.BookingMember;
import polytechnic.bh.PassPlatforms_Backend.Entity.BookingNote;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BookingNoteDto {
    private int noteid;
    private java.sql.Date datetime;
    private String notebody;
    private BookingDto booking;
    private UserDto user;

    public BookingNoteDto(BookingNote bookingNote) {
        this.noteid = bookingNote.getNoteid();
        this.datetime = bookingNote.getDatetime();
        this.notebody = bookingNote.getNotebody();
        this.user = new UserDto(bookingNote.getUser());

        List<BookingMemberDto> bookingMembers = new ArrayList<>();
        if(!bookingNote.getBooking().getBookingMembers().isEmpty())
        {
            for (BookingMember member : bookingNote.getBooking().getBookingMembers())
            {
                bookingMembers.add(new BookingMemberDto(member.getMemberid(),
                        member.getDatetime(),
                        new UserDto(member.getStudent()),
                        null,
                        new MemberStatusDto(member.getStatus())));
            }
        }

        this.booking = new BookingDto(bookingNote.getBooking().getBookingid(),
                bookingNote.getBooking().getDatebooked(),
                bookingNote.getBooking().getDate(),
                bookingNote.getBooking().getNote(),
                bookingNote.getBooking().getStarttime(),
                bookingNote.getBooking().getEndtime(),
                bookingNote.getBooking().getBookinglimit(),
                bookingNote.getBooking().getIsonline(),
                bookingNote.getBooking().getIsgroup(),
                bookingNote.getBooking().getIsrevision(),
                new SlotDto(bookingNote.getBooking().getSlot()),
                new BookingStatusDto(bookingNote.getBooking().getBookingStatus()),
                new UserDto(bookingNote.getBooking().getStudent()),
                new CourseDto(bookingNote.getBooking().getCourse()),
                bookingMembers,
                null);


    }
}
