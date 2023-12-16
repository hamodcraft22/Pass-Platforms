package polytechnic.bh.PassPlatforms_Backend.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import polytechnic.bh.PassPlatforms_Backend.Dao.BookingMemberDao;
import polytechnic.bh.PassPlatforms_Backend.Entity.Booking;
import polytechnic.bh.PassPlatforms_Backend.Entity.BookingMember;
import polytechnic.bh.PassPlatforms_Backend.Entity.Notification;
import polytechnic.bh.PassPlatforms_Backend.Repository.BookingMemberRepo;
import polytechnic.bh.PassPlatforms_Backend.Repository.BookingRepo;
import polytechnic.bh.PassPlatforms_Backend.Repository.NotificationRepo;
import polytechnic.bh.PassPlatforms_Backend.Repository.ScheduleRepo;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static polytechnic.bh.PassPlatforms_Backend.Constant.BookingStatusConstant.*;
import static polytechnic.bh.PassPlatforms_Backend.Constant.BookingTypeConstant.*;

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
    public BookingMemberDao addStudentMember(int bookingID, String studentID)
    {
        List<String> errors = new ArrayList<>();

        Optional<Booking> retrivedBooking = bookingRepo.findById(bookingID);

        // check if booking exists
        if (retrivedBooking.isPresent())
        {
            // check if it can be booked - has not been closed
            if (retrivedBooking.get().getBookingMembers().size() < retrivedBooking.get().getBookinglimit() && retrivedBooking.get().getBookingStatus().getStatusid() == BKNGSTAT_ACTIVE)
            {
                // check if user has current bookings at this time
                if (bookingRepo.existsByStudent_UseridAndBookingdateAndBookingStatus_StatusidAndBookingType_TypeidAndSlot_StarttimeBetweenOrSlot_EndtimeBetween(studentID, retrivedBooking.get().getBookingdate(), BKNGSTAT_ACTIVE, BKNGTYP_NORMAL, retrivedBooking.get().getSlot().getStarttime(), retrivedBooking.get().getSlot().getEndtime(), retrivedBooking.get().getSlot().getStarttime(), retrivedBooking.get().getSlot().getEndtime()))
                {
                    errors.add("you have another booking at the same time");
                }

                // check if user is a member of any sessions at this time
                if (bookingMemberRepo.existsByStudent_UseridAndBooking_BookingdateAndBooking_BookingStatus_StatusidAndBooking_BookingType_TypeidAndBooking_Slot_StarttimeBetweenOrBooking_Slot_EndtimeBetween(studentID, retrivedBooking.get().getBookingdate(), BKNGSTAT_ACTIVE, BKNGTYP_GROUP, retrivedBooking.get().getSlot().getStarttime(), retrivedBooking.get().getSlot().getEndtime(), retrivedBooking.get().getSlot().getStarttime(), retrivedBooking.get().getSlot().getEndtime()))
                {
                    errors.add("you have another revision session you are a part of (group) at the same time");
                }

                // check if student has no classes - in schedule - at this time
                if (scheduleRepo.existsByStarttimeBetweenAndUser_Userid(retrivedBooking.get().getSlot().getStarttime(), retrivedBooking.get().getSlot().getEndtime(), studentID) || scheduleRepo.existsByEndtimeBetweenAndUser_Userid(retrivedBooking.get().getSlot().getStarttime(), retrivedBooking.get().getSlot().getEndtime(), studentID))
                {
                    errors.add("you have a class in the same time as the booking session");
                }

                // check if user is already a part of this booking
                if (bookingMemberRepo.existsByStudent_UseridAndBooking_Bookingid(studentID, retrivedBooking.get().getBookingid()))
                {
                    errors.add("you are already a part of this group");
                }
            }
            else
            {
                errors.add("session is full");
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
            newRevMember.setStudent(userServ.getUser(studentID));

            BookingMember addedMember = bookingMemberRepo.save(newRevMember);

            // notify leader
            Notification newNotification = new Notification();
            newNotification.setEntity("Booking");
            newNotification.setItemid(String.valueOf(addedMember.getBooking().getBookingid()));
            newNotification.setNotficmsg("new student added to booking");
            newNotification.setUser(addedMember.getBooking().getSlot().getLeader());
            newNotification.setSeen(false);

            notificationRepo.save(newNotification);

            // notify student added
            newNotification.setNotficmsg("you have been added to a booking");
            newNotification.setUser(addedMember.getStudent());

            notificationRepo.save(newNotification);

            return new BookingMemberDao();
        }


        return null;
    }

    // remove student from group / revision
    public boolean removeStudentMember(int bookingID, String studentID)
    {
        bookingMemberRepo.deleteByStudent_UseridAndBooking_Bookingid(studentID, bookingID);
        return true;
    }

    // register in revision
    public BookingMemberDao revisionSignUp(int bookingID, String studentID)
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
                if (bookingMemberRepo.existsByStudent_UseridAndBooking_BookingdateAndBooking_BookingType_TypeidAndBooking_StarttimeBetweenOrBooking_EndtimeBetween(studentID, retrivedBooking.get().getBookingdate(), BKNGTYP_REVISION, retrivedBooking.get().getStarttime(), retrivedBooking.get().getEndtime(), retrivedBooking.get().getStarttime(), retrivedBooking.get().getEndtime()))
                {
                    errors.add("you have another revision session booked at the same time");
                }

                // check if they have an active or complete revisions in the same course
                if (bookingMemberRepo.existsByStudent_UseridAndBooking_Course_CourseidAndBooking_BookingStatus_StatusidAndBooking_BookingType_Typeid(studentID, retrivedBooking.get().getCourse().getCourseid(), BKNGSTAT_ACTIVE, BKNGTYP_REVISION) || bookingMemberRepo.existsByStudent_UseridAndBooking_Course_CourseidAndBooking_BookingStatus_StatusidAndBooking_BookingType_Typeid(studentID, retrivedBooking.get().getCourse().getCourseid(), BKNGSTAT_FINISHED, BKNGTYP_REVISION))
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

            newRevMember.setBooking(bookingRepo.getReferenceById(retrivedBooking.get().getBookingid()));
            newRevMember.setStudent(userServ.getUser(studentID));

            BookingMember addedMember = bookingMemberRepo.save(newRevMember);

            // notify student
            Notification newNotification = new Notification();
            newNotification.setEntity("Booking");
            newNotification.setItemid(String.valueOf(addedMember.getBooking().getBookingid()));
            newNotification.setNotficmsg("you have registered in a revision");
            newNotification.setUser(addedMember.getStudent());
            newNotification.setSeen(false);

            notificationRepo.save(newNotification);

            return new BookingMemberDao();
        }


        return null;
    }
}
