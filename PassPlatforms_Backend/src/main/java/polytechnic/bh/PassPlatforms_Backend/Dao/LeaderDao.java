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
public class LeaderDao
{
    private String userid;
    private RoleDao role;

    private List<NotificationDao> notifications;
    private List<BookingDao> bookings; // booking the leader made - includes revisions (should be filtered)
    private List<BookingMemberDao> groupbookings; // bookings the leader is a part of
    private List<BookingMemberDao> revisionsmember;
    private List<BookingDao> revisions; // revisions the leader made
    private List<ScheduleDao> schedules;
    private List<TranscriptDao> transcripts;
    private List<SlotDao> slots;
    private List<OfferedCourseDao> offeredCourses;

    public LeaderDao(User user)
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
            groupbookings.add(new BookingMemberDao(bookingMember.getMemberid(), bookingMember.getDatetime().toInstant(), null, new BookingDao(bookingMember.getBooking().getBookingid(), bookingMember.getBooking().getDatebooked().toInstant(), bookingMember.getBooking().getBookingdate(), bookingMember.getBooking().getNote(), bookingMember.getBooking().getStarttime().toInstant(), bookingMember.getBooking().getEndtime().toInstant(), bookingMember.getBooking().getBookinglimit(), bookingMember.getBooking().isIsonline(), new BookingTypeDao(bookingMember.getBooking().getBookingType()), new SlotDao(bookingMember.getBooking().getSlot()), new BookingStatusDao(bookingMember.getBooking().getBookingStatus()), null, new CourseDao(bookingMember.getBooking().getCourse()), null, null)));
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

        List<BookingDao> revisions = new ArrayList<>();
        for (Booking booking : user.getBookings())
        {
            if (booking.getBookingType().getTypeid() == 'R')
            {
                revisions.add(new BookingDao(booking.getBookingid(), booking.getDatebooked().toInstant(), booking.getBookingdate(), booking.getNote(), booking.getStarttime().toInstant(), booking.getEndtime().toInstant(), booking.getBookinglimit(), booking.isIsonline(), new BookingTypeDao(booking.getBookingType()), new SlotDao(booking.getSlot()), new BookingStatusDao(booking.getBookingStatus()), null, new CourseDao(booking.getCourse()), null, null));
            }
        }
        this.revisions = revisions;

        List<ScheduleDao> schedules = new ArrayList<>();
        for (Schedule schedule : user.getSchedules())
        {
            schedules.add(new ScheduleDao(schedule.getScheduleid(), schedule.getStarttime().toInstant(), schedule.getEndtime().toInstant(), new DayDao(schedule.getDay()), null));
        }
        this.schedules = schedules;

        List<TranscriptDao> transcripts = new ArrayList<>();
        for (Transcript transcript : user.getTranscripts())
        {
            transcripts.add(new TranscriptDao(transcript.getTransid(), transcript.getGrade(), null, new CourseDao(transcript.getCourse())));
        }
        this.transcripts = transcripts;

        List<SlotDao> slots = new ArrayList<>();
        for (Slot slot : user.getSlots())
        {
            slots.add(new SlotDao(slot.getSlotid(), slot.getStarttime().toInstant(), slot.getEndtime().toInstant(), slot.getNote(), new SlotTypeDao(slot.getSlotType()), new DayDao(slot.getDay()), null));
        }
        this.slots = slots;

        List<OfferedCourseDao> offeredCourses = new ArrayList<>();
        for (OfferedCourse offeredCourse : user.getOfferedCourses())
        {
            offeredCourses.add(new OfferedCourseDao(offeredCourse.getOfferid(), null, new CourseDao(offeredCourse.getCourse())));
        }
        this.offeredCourses = offeredCourses;
    }
}
