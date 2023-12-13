package polytechnic.bh.PassPlatforms_Backend.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import polytechnic.bh.PassPlatforms_Backend.Dao.BookingMemberDao;
import polytechnic.bh.PassPlatforms_Backend.Entity.Booking;
import polytechnic.bh.PassPlatforms_Backend.Entity.BookingMember;
import polytechnic.bh.PassPlatforms_Backend.Repository.BookingMemberRepo;
import polytechnic.bh.PassPlatforms_Backend.Repository.BookingRepo;
import polytechnic.bh.PassPlatforms_Backend.Repository.ScheduleRepo;
import polytechnic.bh.PassPlatforms_Backend.Repository.UserRepo;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class BookingMemberServ
{
    @Autowired
    BookingRepo bookingRepo;

    @Autowired
    BookingMemberRepo bookingMemberRepo;

    @Autowired
    ScheduleRepo scheduleRepo;

    @Autowired
    UserRepo userRepo;

    // add student to group
    public BookingMemberDao addStudentMember(int bookingID, String studentID)
    {
        List<String> errors = new ArrayList<>();

        Optional<Booking> retrivedBooking = bookingRepo.findById(bookingID);

        // check if booking exists
        if (retrivedBooking.isPresent())
        {
            // check if it can be booked - has not been closed
            if (retrivedBooking.get().getBookingMembers().size() < retrivedBooking.get().getBookinglimit() && retrivedBooking.get().getBookingStatus().getStatusid() == 'a')
            {
                // check if user has current bookings at this time
                if (bookingRepo.existsByStudent_UseridAndBookingdateAndBookingStatus_StatusidAndBookingType_TypeidAndSlot_StarttimeBetweenOrSlot_EndtimeBetween(studentID, retrivedBooking.get().getBookingdate(), 'a', 'N', retrivedBooking.get().getSlot().getStarttime(), retrivedBooking.get().getSlot().getEndtime(), retrivedBooking.get().getSlot().getStarttime(), retrivedBooking.get().getSlot().getEndtime()))
                {
                    errors.add("you have another booking at the same time");
                }

                // check if user is a member of any sessions at this time
                if (bookingMemberRepo.existsByStudent_UseridAndBooking_BookingdateAndBooking_BookingStatus_StatusidAndBooking_BookingType_TypeidAndBooking_Slot_StarttimeBetweenOrBooking_Slot_EndtimeBetween(studentID, retrivedBooking.get().getBookingdate(), 'a', 'G', retrivedBooking.get().getSlot().getStarttime(), retrivedBooking.get().getSlot().getEndtime(), retrivedBooking.get().getSlot().getStarttime(), retrivedBooking.get().getSlot().getEndtime()))
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
            newRevMember.setStudent(userRepo.getReferenceById(studentID));

            return new BookingMemberDao(bookingMemberRepo.save(newRevMember));
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

            if (retrivedBooking.get().getBookingMembers().size() < retrivedBooking.get().getBookinglimit() && retrivedBooking.get().getBookingStatus().getStatusid() == 'a')
            {
                // check if user has another revision session within same time
                if (bookingMemberRepo.existsByStudent_UseridAndBooking_BookingdateAndBooking_BookingType_TypeidAndBooking_StarttimeBetweenOrBooking_EndtimeBetween(studentID, retrivedBooking.get().getBookingdate(), 'R', retrivedBooking.get().getStarttime(), retrivedBooking.get().getEndtime(), retrivedBooking.get().getStarttime(), retrivedBooking.get().getEndtime()))
                {
                    errors.add("you have another revision session booked at the same time");
                }

                // check if they have an active or complete revisions in the same course
                if (bookingMemberRepo.existsByStudent_UseridAndBooking_Course_CourseidAndBooking_BookingStatus_StatusidAndBooking_BookingType_Typeid(studentID, retrivedBooking.get().getCourse().getCourseid(), 'a', 'R') || bookingMemberRepo.existsByStudent_UseridAndBooking_Course_CourseidAndBooking_BookingStatus_StatusidAndBooking_BookingType_Typeid(studentID, retrivedBooking.get().getCourse().getCourseid(), 'c', 'R'))
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
            newRevMember.setStudent(userRepo.getReferenceById(studentID));

            return new BookingMemberDao(bookingMemberRepo.save(newRevMember));
        }


        return null;
    }
}
