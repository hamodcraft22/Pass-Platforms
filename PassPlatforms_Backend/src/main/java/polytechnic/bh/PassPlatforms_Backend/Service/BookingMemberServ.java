package polytechnic.bh.PassPlatforms_Backend.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import polytechnic.bh.PassPlatforms_Backend.Dao.BookingMemberDao;
import polytechnic.bh.PassPlatforms_Backend.Dto.GenericDto;
import polytechnic.bh.PassPlatforms_Backend.Entity.Booking;
import polytechnic.bh.PassPlatforms_Backend.Entity.BookingMember;
import polytechnic.bh.PassPlatforms_Backend.Entity.Notification;
import polytechnic.bh.PassPlatforms_Backend.Entity.User;
import polytechnic.bh.PassPlatforms_Backend.Repository.BookingMemberRepo;
import polytechnic.bh.PassPlatforms_Backend.Repository.BookingRepo;
import polytechnic.bh.PassPlatforms_Backend.Repository.NotificationRepo;
import polytechnic.bh.PassPlatforms_Backend.Repository.ScheduleRepo;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static polytechnic.bh.PassPlatforms_Backend.Constant.BookingStatusConstant.BKNGSTAT_ACTIVE;

@Service
public class BookingMemberServ
{
    @Autowired
    private BookingRepo bookingRepo;

    @Autowired
    private BookingMemberRepo bookingMemberRepo;

    @Autowired
    private ScheduleRepo scheduleRepo;

    @Autowired
    private UserServ userServ;

    @Autowired
    private NotificationRepo notificationRepo;

    // add student to group
    public GenericDto<BookingMemberDao> addStudentMember(int bookingID, String studentID)
    {
        List<String> errors = new ArrayList<>();

        Optional<Booking> retrivedBooking = bookingRepo.findById(bookingID);

        // check if booking exists
        if (retrivedBooking.isPresent())
        {
            // check if there is limit (if limit is not 0)
            if (retrivedBooking.get().getBookinglimit() != 0)
            {
                // has a booking limit, check it
                if (retrivedBooking.get().getBookingMembers().size() >= retrivedBooking.get().getBookinglimit())
                {
                    errors.add("booking is full, cant add other members");
                }
            }

            // check if booking is closed
            if (retrivedBooking.get().getBookingStatus().getStatusid() != BKNGSTAT_ACTIVE)
            {
                errors.add("booking is closed, cant add other members");
            }

            // check if user has current bookings at this time
            if (bookingRepo.sameTimeSessionsFind(studentID, retrivedBooking.get().getBookingdate(), retrivedBooking.get().getSlot().getStarttime(), retrivedBooking.get().getSlot().getEndtime()) != 0)
            {
                errors.add("student " + studentID + " another booking at the same time");
            }

            // check if user is a member of any sessions at this time
            if (bookingMemberRepo.sameTimeMemberSessionsFind(studentID, retrivedBooking.get().getBookingdate(), retrivedBooking.get().getSlot().getStarttime(), retrivedBooking.get().getSlot().getEndtime()) != 0)
            {
                errors.add("student " + studentID + " has another (group) session you are a part of at the same time");
            }

            // check if student has no classes - in schedule - at this time -- checked
            if (scheduleRepo.sameTimeClassesFind(studentID, retrivedBooking.get().getSlot().getDay().getDayid(), retrivedBooking.get().getSlot().getStarttime(), retrivedBooking.get().getSlot().getEndtime()) != 0)
            {
                errors.add("student " + studentID + " has a class in the same time as the booking session");
            }

            // check if user is already a part of this booking
            if (bookingMemberRepo.existsByStudent_UseridAndBooking_Bookingid(studentID, retrivedBooking.get().getBookingid()))
            {
                errors.add("student " + studentID + " already is a part of this group");
            }
        }
        else
        {
            errors.add("the booking selected does not exist");
        }

        // if they have no errors
        if (errors.isEmpty())
        {
            BookingMember newRevMember = new BookingMember();

            newRevMember.setBooking(bookingRepo.getReferenceById(retrivedBooking.get().getBookingid()));
            newRevMember.setDatetime(Timestamp.from(Instant.now()));
            newRevMember.setStudent(new User(userServ.getUser(studentID)));

            BookingMember addedMember = bookingMemberRepo.save(newRevMember);

            // notify student added
            Notification newNotification = new Notification();
            newNotification.setEntity("Booking");
            newNotification.setItemid(String.valueOf(addedMember.getBooking().getBookingid()));
            newNotification.setNotficmsg("you have been added to a booking");
            newNotification.setUser(addedMember.getStudent());
            newNotification.setSeen(false);

            notificationRepo.save(newNotification);

            return new GenericDto<>(null, new BookingMemberDao(addedMember), null, null);
        }

        return new GenericDto<>(null, null, errors, null);
    }

    // register in revision
    public GenericDto<BookingMemberDao> revisionSignUp(int bookingID, String studentID)
    {
        List<String> errors = new ArrayList<>();

        Optional<Booking> retrivedBooking = bookingRepo.findById(bookingID);

        // check if revision exists
        if (retrivedBooking.isPresent())
        {
            // check if it can be booked

            if (retrivedBooking.get().getBookingMembers().size() < retrivedBooking.get().getBookinglimit() && retrivedBooking.get().getBookingStatus().getStatusid() == BKNGSTAT_ACTIVE)
            {
                // check if user has another revision session within same time
                if (bookingMemberRepo.sameTimeMemberSessionsFind(studentID, retrivedBooking.get().getBookingdate(), retrivedBooking.get().getStarttime(), retrivedBooking.get().getEndtime()) != 0)
                {
                    errors.add("you have another revision session booked at the same time");
                }

                // check if they have an active or complete revisions in the same course
                if (bookingMemberRepo.sameCourseRevisionFind(studentID, retrivedBooking.get().getCourse().getCourseid()) != 0)
                {
                    errors.add("you have already booked in this course");
                }
            }
            else
            {
                errors.add("session is full / closed");
            }
        }
        else
        {
            errors.add("the booking selected does not exist");
        }

        // if they have no errors
        if (errors.isEmpty())
        {
            BookingMember newRevMember = new BookingMember();

            newRevMember.setDatetime(Timestamp.from(Instant.now()));
            newRevMember.setBooking(bookingRepo.getReferenceById(retrivedBooking.get().getBookingid()));
            newRevMember.setStudent(new User(userServ.getUser(studentID)));

            BookingMember addedMember = bookingMemberRepo.save(newRevMember);

            // notify student
            Notification newNotification = new Notification();
            newNotification.setEntity("Booking");
            newNotification.setItemid(String.valueOf(addedMember.getBooking().getBookingid()));
            newNotification.setNotficmsg("you have registered in a revision");
            newNotification.setUser(addedMember.getStudent());
            newNotification.setSeen(false);

            notificationRepo.save(newNotification);

            return new GenericDto<>(null, new BookingMemberDao(addedMember), null, null);
        }

        return new GenericDto<>(null, null, errors, null);
    }

    // remove student from group / revision
    public boolean removeStudentMember(int bookingID, String studentID)
    {
        bookingMemberRepo.deleteByStudent_UseridAndBooking_Bookingid(studentID, bookingID);
        return true;
    }

}
