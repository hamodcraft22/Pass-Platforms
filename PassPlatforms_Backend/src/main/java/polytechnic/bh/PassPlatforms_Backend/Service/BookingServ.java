package polytechnic.bh.PassPlatforms_Backend.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import polytechnic.bh.PassPlatforms_Backend.Dao.BookingDao;
import polytechnic.bh.PassPlatforms_Backend.Dao.BookingMemberDao;
import polytechnic.bh.PassPlatforms_Backend.Entity.Booking;
import polytechnic.bh.PassPlatforms_Backend.Entity.BookingMember;
import polytechnic.bh.PassPlatforms_Backend.Entity.Slot;
import polytechnic.bh.PassPlatforms_Backend.Repository.*;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.*;

@Service
public class BookingServ
{
    @Autowired
    BookingRepo bookingRepo;

    @Autowired
    UserRepo userRepo;

    @Autowired
    SlotRepo slotRepo;

    @Autowired
    CourseRepo courseRepo;

    @Autowired
    OfferedCourseRepo offeredCourseRepo;

    @Autowired
    ScheduleRepo scheduleRepo;

    @Autowired
    BookingStatusRepo bookingStatusRepo;

    @Autowired
    BookingMemberRepo bookingMemberRepo;

    // get all bookings / revisions - manager
    public List<BookingDao> getAllBookings()
    {
        List<BookingDao> bookings = new ArrayList<>();

        for (Booking retrivedBooking : bookingRepo.findAll())
        {
            bookings.add(new BookingDao(retrivedBooking));
        }

        return bookings;
    }

    // get a single booking
    public BookingDao getBookingDetails(int bookingID)
    {
        Optional<Booking> retrivedBooking = bookingRepo.findById(bookingID);

        return retrivedBooking.map(BookingDao::new).orElse(null);
    }

    // get all sessions - for a school
    public List<BookingDao> getSchoolSessions(String schoolID)
    {
        List<BookingDao> bookings = new ArrayList<>();

        for (Booking retrivedBooking : bookingRepo.findBookingsByCourse_School_SchoolidAndIsrevision(schoolID, false))
        {
            if (retrivedBooking.isIsrevision())
            {
                bookings.add(new BookingDao(retrivedBooking));
            }
        }

        return bookings;
    }

    // get all revisions - for a school
    public List<BookingDao> getSchoolRevisions(String schoolID)
    {
        List<BookingDao> bookings = new ArrayList<>();

        for (Booking retrivedBooking : bookingRepo.findBookingsByCourse_School_SchoolidAndIsrevision(schoolID, true))
        {
            if (retrivedBooking.isIsrevision())
            {
                bookings.add(new BookingDao(retrivedBooking));
            }
        }

        return bookings;
    }

    // create booking / group booking
    public BookingDao createNewBooking(Date bookingDate, String note, boolean group, int slotID, String studentID, String courseID, List<String> bookingMembersIDs)
    {
        List<String> errors = new ArrayList<>();

        Calendar cal = Calendar.getInstance();


        // check if it is of time of mid or final sessions ADD


        // check if the booking is in the feature
        if (bookingDate.before(cal.getTime()))
        {
            errors.add("cannot book in the past");
        }

        // check if the slot is available (validation checks)
        Optional<Slot> retrivedSlot = slotRepo.findById(slotID);
        if (retrivedSlot.isPresent())
        {
            cal.setTime(bookingDate);

            // check if the date is the of the same day of the slot
            if (cal.get(Calendar.DAY_OF_WEEK) != retrivedSlot.get().getDay().getDayNum())
            {
                errors.add("selected date day and slot day does not match");
            }
            else
            {
                // check if any active bookings are under this slot in the date selected
                if (bookingRepo.existsBySlot_SlotidAndBookingdateAndBookingStatus_Statusid(slotID, bookingDate, 'a'))
                {
                    errors.add("the booking slot is booked by another student");
                }
            }

            // check if the leader chosen teaches the course
            if (!offeredCourseRepo.existsByLeader_UseridAndCourse_Courseid(retrivedSlot.get().getLeader().getUserid(), courseID))
            {
                errors.add("selected PASS Leader does not teach the course selected");
            }

            // check if user has current bookings at this time
            if (bookingRepo.existsByStudent_UseridAndBookingdateAndBookingStatus_StatusidAndIsrevisionAndSlot_StarttimeBetweenOrSlot_EndtimeBetween(studentID, bookingDate, 'a',false,retrivedSlot.get().getStarttime(),retrivedSlot.get().getEndtime(),retrivedSlot.get().getStarttime(),retrivedSlot.get().getEndtime()))
            {
                errors.add("you have another booking at the same time");
            }

            // check if user is a member of any sessions at this time
            if (bookingMemberRepo.existsByStudent_UseridAndBooking_BookingdateAndBooking_BookingStatus_StatusidAndBooking_IsgroupAndBooking_IsrevisionAndBooking_Slot_StarttimeBetweenOrBooking_Slot_EndtimeBetween(studentID, bookingDate, 'a', true, false, retrivedSlot.get().getStarttime(),retrivedSlot.get().getEndtime(),retrivedSlot.get().getStarttime(),retrivedSlot.get().getEndtime()))
            {
                errors.add("you have another revision session you are a part of (group) at the same time");
            }

            // check if student has no classes - in schedule - at this time
            if (scheduleRepo.existsByStarttimeBetweenAndUser_Userid(retrivedSlot.get().getStarttime(), retrivedSlot.get().getEndtime(), studentID) || scheduleRepo.existsByEndtimeBetweenAndUser_Userid(retrivedSlot.get().getStarttime(), retrivedSlot.get().getEndtime(), studentID))
            {
                errors.add("you have a class in the same time as the booking session");
            }
        }
        else
        {
            errors.add("slot given does not exist");
        }


        // create the booking
        if (errors.isEmpty())
        {
            Booking newBooking = new Booking();

            newBooking.setDatebooked(Timestamp.from(Instant.now()));
            newBooking.setBookingdate(new java.sql.Date(bookingDate.getTime()));
            newBooking.setNote(note);
            newBooking.setIsonline(retrivedSlot.get().isIsonline());
            newBooking.setIsgroup(group);
            newBooking.setIsrevision(false);
            newBooking.setSlot(slotRepo.getReferenceById(slotID));
            newBooking.setBookingStatus(bookingStatusRepo.getReferenceById('a'));
            newBooking.setStudent(userRepo.getReferenceById(studentID));

            Booking createdBooking = bookingRepo.save(newBooking);

            if (group)
            {
                // add group memebers
                for (String memberID : bookingMembersIDs)
                {
                    BookingMember newBookingMember = new BookingMember();

                    newBookingMember.setDatetime(Timestamp.from(Instant.now()));
                    newBookingMember.setStudent(userRepo.getReferenceById(memberID));
                    newBookingMember.setBooking(bookingRepo.getReferenceById(createdBooking.getBookingid()));

                    bookingMemberRepo.save(newBookingMember);
                }
            }

            // return dto with correct things
            return new BookingDao(bookingRepo.findById(createdBooking.getBookingid()).get());
        }

        return null;

    }

    // create revision booking - by leader
    public BookingDao createNewRevision(Date bookingDate, String note, Timestamp startTime, Timestamp endTime, int bookingLimit, boolean online, String courseID, String leaderID)
    {
        List<String> errors = new ArrayList<>();

        // check if there is any other revisions at this time by this leader
        if (bookingRepo.existsByStudent_UseridAndBookingdateAndBookingStatus_StatusidAndIsrevisionAndStarttimeBetweenOrEndtimeBetween(leaderID, bookingDate, 'a', true, startTime,endTime,startTime,endTime))
        {
            errors.add("there is another session or schedule within the selected time");
        }

        // check if the leader teaches the course
        if (!offeredCourseRepo.existsByLeader_UseridAndCourse_Courseid(leaderID, courseID))
        {
            errors.add("selected PASS Leader does not teach the course selected");
        }

        if (errors.isEmpty())
        {
            Booking newRevision = new Booking();

            newRevision.setDatebooked(Timestamp.from(Instant.now()));
            newRevision.setBookingdate(new java.sql.Date(bookingDate.getTime()));
            newRevision.setNote(note);
            newRevision.setStarttime(startTime);
            newRevision.setEndtime(endTime);
            newRevision.setBookinglimit(bookingLimit);
            newRevision.setIsonline(online);
            newRevision.setIsgroup(false);
            newRevision.setIsrevision(true);
            newRevision.setBookingStatus(bookingStatusRepo.getReferenceById('a'));
            newRevision.setCourse(courseRepo.getReferenceById(courseID));

            Booking savedRevision = bookingRepo.save(newRevision);

            return new BookingDao(savedRevision);
        }

        return null;
    }

    // edit booking / revision -- can set to canceled
    public BookingDao updateBooking(int bookingID, char statusID)
    {
        Optional<Booking> bookingToUpdate = bookingRepo.findById(bookingID);

        if (bookingToUpdate.isPresent())
        {
            bookingToUpdate.get().setBookingStatus(bookingStatusRepo.getReferenceById(statusID));
            return new BookingDao(bookingRepo.save(bookingToUpdate.get()));
        }
        else
        {
            return null;
        }
    }

    // add student to group
    public BookingMemberDao addStudentMember(int bookingID, String studentID)
    {
        List<String> errors = new ArrayList<>();

        Optional<Booking> retrivedBooking = bookingRepo.findById(bookingID);

        // check if booking exists
        if (retrivedBooking.isPresent())
        {
            // check if it can be booked - has not been closed
            if (retrivedBooking.get().getBookingMembers().size() < retrivedBooking.get().getBookinglimit() && retrivedBooking.get().getBookingStatus().getStatusid()=='a')
            {
                // check if user has current bookings at this time
                if (bookingRepo.existsByStudent_UseridAndBookingdateAndBookingStatus_StatusidAndIsrevisionAndSlot_StarttimeBetweenOrSlot_EndtimeBetween(studentID, retrivedBooking.get().getBookingdate(), 'a',false,retrivedBooking.get().getSlot().getStarttime(),retrivedBooking.get().getSlot().getEndtime(),retrivedBooking.get().getSlot().getStarttime(),retrivedBooking.get().getSlot().getEndtime()))
                {
                    errors.add("you have another booking at the same time");
                }

                // check if user is a member of any sessions
                if (bookingMemberRepo.existsByStudent_UseridAndBooking_BookingdateAndBooking_BookingStatus_StatusidAndBooking_IsgroupAndBooking_IsrevisionAndBooking_Slot_StarttimeBetweenOrBooking_Slot_EndtimeBetween(studentID, retrivedBooking.get().getBookingdate(), 'a', true, false, retrivedBooking.get().getSlot().getStarttime(),retrivedBooking.get().getSlot().getEndtime(),retrivedBooking.get().getSlot().getStarttime(),retrivedBooking.get().getSlot().getEndtime()))
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
    public BookingMemberDao removeStudentMember(int bookingID, String studentID)
    {

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

            if (retrivedBooking.get().getBookingMembers().size() < retrivedBooking.get().getBookinglimit() && retrivedBooking.get().getBookingStatus().getStatusid()=='a')
            {
                // check if user has another revision session within same time
                List<BookingMember>  bookingMemberList = bookingMemberRepo.findBookingMembersByStudent_UseridAndBooking_BookingStatus_StatusidAndBooking_Bookingdate(studentID,'a',retrivedBooking.get().getBookingdate());

                // get all active revisions they are a part off
                if (!bookingMemberList.isEmpty())
                {
                    // no bookings at the same time
                    if (bookingRepo.existsByBookingMembersContainsAndStarttimeBetween(bookingMemberList, retrivedBooking.get().getStarttime(), retrivedBooking.get().getEndtime()) || bookingRepo.existsByBookingMembersContainsAndEndtimeBetween(bookingMemberList, retrivedBooking.get().getStarttime(), retrivedBooking.get().getEndtime()))
                    {
                        errors.add("you have another revision session booked at the same time");
                    }
                }


                // check if they have an active booking in the same course
                List<BookingMember> courseMemberList = bookingMemberRepo.findBookingMembersByStudent_UseridAndBooking_Course_CourseidAndBooking_BookingStatus_StatusidAndBooking_Isrevision(studentID,retrivedBooking.get().getCourse().getCourseid(), 'a', true);

                if (!courseMemberList.isEmpty())
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

    // delete booking - not by users
    public boolean deleteBooking(int bookingID)
    {
        bookingRepo.deleteById(bookingID);
        return true;
    }
}
