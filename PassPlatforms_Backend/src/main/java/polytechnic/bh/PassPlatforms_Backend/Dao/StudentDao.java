package polytechnic.bh.PassPlatforms_Backend.Dao;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import polytechnic.bh.PassPlatforms_Backend.Entity.*;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class StudentDao
{
    private String userid;
    private RoleDao role;

    private List<NotificationDao> notifications;
    private List<BookingDao> bookings;
    private List<BookingMemberDao> groupbookings;
    private List<BookingMemberDao> revisionsmember;
    private List<ScheduleDao> schedules;
    private ApplicationDao application;
    private List<TranscriptDao> transcripts;

    public StudentDao(User user)
    {
        this.userid = user.getUserid();
        this.role = new RoleDao(user.getRole());

        List<NotificationDao> notifications = new ArrayList<>();
        for (Notification notification : user.getNotifications())
        {
            notifications.add(new NotificationDao(notification.getNotficid(), notification.getEntity(), notification.getItemid(), notification.getNotficmsg(), notification.isSeen(), null));
        }
        this.notifications = notifications;

        List<BookingDao> bookings = new ArrayList<>();
        for (Booking booking : user.getBookings())
        {
            if (booking.getBookingType().getTypeid() != 'R')
            {
                bookings.add(new BookingDao(booking.getBookingid(), booking.getDatebooked().toInstant(), booking.getBookingdate(), booking.getNote(), booking.getStarttime().toInstant(), booking.getEndtime().toInstant(), booking.getBookinglimit(), booking.isIsonline(), new BookingTypeDao(booking.getBookingType()), new SlotDao(booking.getSlot()), new BookingStatusDao(booking.getBookingStatus()), null, new CourseDao(booking.getCourse()), null, null));
            }
        }
        this.bookings = bookings;

        List<BookingMemberDao> groupbookings = new ArrayList<>();
        for (BookingMember bookingMember : user.getGroupbookings())
        {
            if (bookingMember.getBooking().getBookingType().getTypeid() != 'R')
            {
                groupbookings.add(new BookingMemberDao(bookingMember.getMemberid(), bookingMember.getDatetime().toInstant(), null, new BookingDao(bookingMember.getBooking().getBookingid(), bookingMember.getBooking().getDatebooked().toInstant(), bookingMember.getBooking().getBookingdate(), bookingMember.getBooking().getNote(), bookingMember.getBooking().getStarttime().toInstant(), bookingMember.getBooking().getEndtime().toInstant(), bookingMember.getBooking().getBookinglimit(), bookingMember.getBooking().isIsonline(), new BookingTypeDao(bookingMember.getBooking().getBookingType()), new SlotDao(bookingMember.getBooking().getSlot()), new BookingStatusDao(bookingMember.getBooking().getBookingStatus()), null, new CourseDao(bookingMember.getBooking().getCourse()), null, null)));
            }
        }
        this.groupbookings = groupbookings;

        List<BookingMemberDao> revisionsmember = new ArrayList<>();
        for (BookingMember bookingMember : user.getGroupbookings())
        {
            if (bookingMember.getBooking().getBookingType().getTypeid() == 'R')
            {
                revisionsmember.add(new BookingMemberDao(bookingMember.getMemberid(), bookingMember.getDatetime().toInstant(), null, new BookingDao(bookingMember.getBooking().getBookingid(), bookingMember.getBooking().getDatebooked().toInstant(), bookingMember.getBooking().getBookingdate(), bookingMember.getBooking().getNote(), bookingMember.getBooking().getStarttime().toInstant(), bookingMember.getBooking().getEndtime().toInstant(), bookingMember.getBooking().getBookinglimit(), bookingMember.getBooking().isIsonline(), new BookingTypeDao(bookingMember.getBooking().getBookingType()), new SlotDao(bookingMember.getBooking().getSlot()), new BookingStatusDao(bookingMember.getBooking().getBookingStatus()), null, new CourseDao(bookingMember.getBooking().getCourse()), null, null)));
            }
        }
        this.revisionsmember = revisionsmember;

        List<ScheduleDao> schedules = new ArrayList<>();
        for (Schedule schedule : user.getSchedules())
        {
            schedules.add(new ScheduleDao(schedule.getScheduleid(), schedule.getStarttime().toInstant(), schedule.getEndtime().toInstant(), new DayDao(schedule.getDay()), null));
        }
        this.schedules = schedules;

        if (user.getApplication() != null)
        {
            List<ApplicationNoteDao> applicationNotes = new ArrayList<>();
            for (ApplicationNote applicationNote : user.getApplication().getApplicationNotes())
            {
                applicationNotes.add(new ApplicationNoteDao(applicationNote.getNoteid(), applicationNote.getDatetime().toInstant(), applicationNote.getNotebody(), null, null));
            }
            this.application = new ApplicationDao(user.getApplication().getApplicationid(), user.getApplication().getDatetime().toInstant(), user.getApplication().getNote(), new ApplicationStatusDao(user.getApplication().getApplicationStatus()), null, applicationNotes);
        }

        List<TranscriptDao> transcripts = new ArrayList<>();
        for (Transcript transcript : user.getTranscripts())
        {
            transcripts.add(new TranscriptDao(transcript.getTransid(), transcript.getGrade(), null, transcript.getCourseid()));
        }
        this.transcripts = transcripts;
    }
}
