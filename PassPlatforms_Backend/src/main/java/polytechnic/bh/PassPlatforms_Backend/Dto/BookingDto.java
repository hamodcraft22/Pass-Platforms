package polytechnic.bh.PassPlatforms_Backend.Dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import polytechnic.bh.PassPlatforms_Backend.Entity.Booking;
import polytechnic.bh.PassPlatforms_Backend.Entity.BookingMember;
import polytechnic.bh.PassPlatforms_Backend.Entity.BookingNote;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BookingDto {
    private int bookingid;
    private java.sql.Date datebooked;
    private java.sql.Date date;
    private String note;
    private java.sql.Timestamp starttime;
    private java.sql.Timestamp endtime;
    private String bookinglimit;
    private String isonline;
    private String isgroup;
    private String isrevision;
    private SlotDto slot;
    private BookingStatusDto bookingStatus;
    private UserDto student;
    private CourseDto course;
    private List<BookingMemberDto> bookingMembers;
    private List<BookingNoteDto> bookingNotes;

    public BookingDto(Booking booking) {
        this.bookingid = booking.getBookingid();
        this.datebooked = booking.getDatebooked();
        this.date = booking.getDate();
        this.note = booking.getNote();
        this.starttime = booking.getStarttime();
        this.endtime = booking.getEndtime();
        this.bookinglimit = booking.getBookinglimit();
        this.isonline = booking.getIsonline();
        this.isgroup = booking.getIsgroup();
        this.isrevision = booking.getIsrevision();

        this.slot = new SlotDto(booking.getSlot());
        this.bookingStatus = new BookingStatusDto(booking.getBookingStatus());
        this.student = new UserDto(booking.getStudent());
        this.course = new CourseDto(booking.getCourse());

        List<BookingMemberDto> bookingMembers = new ArrayList<>();
        if(!booking.getBookingMembers().isEmpty())
        {
            for (BookingMember member : booking.getBookingMembers())
            {
                bookingMembers.add(new BookingMemberDto(member.getMemberid(),
                        member.getDatetime(),
                        new UserDto(member.getStudent()),
                        null,
                        new MemberStatusDto(member.getStatus())));
            }
        }

        List<BookingNoteDto> bookingNotes = new ArrayList<>();
        if(!booking.getBookingNotes().isEmpty())
        {
            for (BookingNote note : booking.getBookingNotes())
            {
                bookingNotes.add(new BookingNoteDto(note.getNoteid(),
                        note.getDatetime(),
                        note.getNotebody(),
                        null,
                        new UserDto(note.getUser())));
            }
        }

        this.bookingMembers = bookingMembers;
        this.bookingNotes = bookingNotes;
    }
}
