package polytechnic.bh.PassPlatforms_Backend.Dto;

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
public class BookingDto {
    private int bookingid;
    private Instant datebooked;
    private Date date;
    private String note;
    private Instant starttime;
    private Instant endtime;
    private int bookinglimit;
    private boolean isonline;
    private boolean isgroup;
    private boolean isrevision;
    private SlotDto slot;
    private BookingStatusDto bookingStatus;
    private UserDto student;
    private CourseDto course;
    private List<BookingMemberDto> bookingMembers;
    private List<BookingNoteDto> bookingNotes;

    public BookingDto(Booking booking) {
        this.bookingid = booking.getBookingid();
        this.datebooked = booking.getDatebooked().toInstant();
        this.date = booking.getBookingdate();
        this.note = booking.getNote();
        this.starttime = booking.getStarttime().toInstant();
        this.endtime = booking.getEndtime().toInstant();
        this.bookinglimit = booking.getBookinglimit();
        this.isonline = booking.isIsonline();
        this.isgroup = booking.isIsgroup();
        this.isrevision = booking.isIsrevision();

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
                        member.getDatetime().toInstant(),
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
                        note.getDatetime().toInstant(),
                        note.getNotebody(),
                        null,
                        new UserDto(note.getUser())));
            }
        }

        this.bookingMembers = bookingMembers;
        this.bookingNotes = bookingNotes;
    }
}
