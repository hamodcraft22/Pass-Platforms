package polytechnic.bh.PassPlatforms_Backend.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import polytechnic.bh.PassPlatforms_Backend.Dao.BookingDao;
import polytechnic.bh.PassPlatforms_Backend.Dao.BookingMemberDao;
import polytechnic.bh.PassPlatforms_Backend.Entity.Booking;
import polytechnic.bh.PassPlatforms_Backend.Entity.BookingMember;
import polytechnic.bh.PassPlatforms_Backend.Entity.Notification;
import polytechnic.bh.PassPlatforms_Backend.Entity.Slot;
import polytechnic.bh.PassPlatforms_Backend.Repository.*;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.*;

import static polytechnic.bh.PassPlatforms_Backend.Constant.BookingStatusConstant.BKNGSTAT_ACTIVE;
import static polytechnic.bh.PassPlatforms_Backend.Constant.BookingStatusConstant.BKNGSTAT_FINISHED;
import static polytechnic.bh.PassPlatforms_Backend.Constant.BookingTypeConstant.*;
import static polytechnic.bh.PassPlatforms_Backend.Constant.SlotTypeConstant.SLTYP_UNSCHEDULED;

@Service
public class BookingServ
{
    @Autowired
    private BookingRepo bookingRepo;

    @Autowired
    private UserServ userServ;

    @Autowired
    private SlotRepo slotRepo;

    @Autowired
    private SlotTypeRepo slotTypeRepo;

    @Autowired
    private CourseRepo courseRepo;

    @Autowired
    private OfferedCourseRepo offeredCourseRepo;

    @Autowired
    private ScheduleRepo scheduleRepo;

    @Autowired
    private BookingStatusRepo bookingStatusRepo;

    @Autowired
    private BookingMemberRepo bookingMemberRepo;

    @Autowired
    private BookingTypeRepo bookingTypeRepo;

    @Autowired
    private NotificationRepo notificationRepo;

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

        List<Booking> schoolBookings = new ArrayList<>();

        schoolBookings.addAll(bookingRepo.findBookingsByCourse_School_SchoolidAndBookingType_Typeid(schoolID, BKNGTYP_NORMAL));
        schoolBookings.addAll(bookingRepo.findBookingsByCourse_School_SchoolidAndBookingType_Typeid(schoolID, BKNGTYP_UNSCHEDULED));

        for (Booking retrivedBooking : schoolBookings)
        {
            bookings.add(new BookingDao(retrivedBooking));
        }

        return bookings;
    }

    // get all revisions - for a school
    public List<BookingDao> getSchoolRevisions(String schoolID)
    {
        List<BookingDao> bookings = new ArrayList<>();

        for (Booking retrivedBooking : bookingRepo.findBookingsByCourse_School_SchoolidAndBookingType_Typeid(schoolID, BKNGTYP_REVISION))
        {
            bookings.add(new BookingDao(retrivedBooking));
        }

        return bookings;
    }

    // create booking / group booking
    public BookingDao createNewBooking(Date bookingDate, String note, boolean online, int slotID, String studentID, String courseID, List<BookingMemberDao> bookingMembers, boolean unscheduled, Timestamp startTime, Timestamp endTime, String leaderID)
    {
        List<String> errors = new ArrayList<>();

        Calendar cal = Calendar.getInstance();


        // if session is unscheduled, skip all checks and add (by pass leader)
        if (!unscheduled)
        {
            // TODO check if it is of time of mid or final sessions ADD


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
                    if (bookingRepo.existsBySlot_SlotidAndBookingdateAndBookingStatus_Statusid(slotID, bookingDate, BKNGSTAT_ACTIVE))
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
                if (bookingRepo.existsByStudent_UseridAndBookingdateAndBookingStatus_StatusidAndBookingType_TypeidAndSlot_StarttimeBetweenOrSlot_EndtimeBetween(studentID, bookingDate, BKNGSTAT_ACTIVE, BKNGTYP_NORMAL, retrivedSlot.get().getStarttime(), retrivedSlot.get().getEndtime(), retrivedSlot.get().getStarttime(), retrivedSlot.get().getEndtime()))
                {
                    errors.add("you have another booking at the same time");
                }

                // check if user is a member of any sessions at this time
                if (bookingMemberRepo.existsByStudent_UseridAndBooking_BookingdateAndBooking_BookingStatus_StatusidAndBooking_BookingType_TypeidAndBooking_Slot_StarttimeBetweenOrBooking_Slot_EndtimeBetween(studentID, bookingDate, BKNGSTAT_ACTIVE, BKNGTYP_GROUP, retrivedSlot.get().getStarttime(), retrivedSlot.get().getEndtime(), retrivedSlot.get().getStarttime(), retrivedSlot.get().getEndtime()))
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

            // TODO check if student booked x amount of sessions this week
        }


        // create the booking
        if (errors.isEmpty())
        {
            Booking newBooking = new Booking();

            newBooking.setDatebooked(Timestamp.from(Instant.now()));
            newBooking.setBookingdate(new java.sql.Date(bookingDate.getTime()));
            newBooking.setNote(note);
            newBooking.setIsonline(online);
            newBooking.setStudent(userServ.getUser(studentID));
            newBooking.setCourse(courseRepo.getReferenceById(courseID));

            if (unscheduled)
            {
                newBooking.setStarttime(startTime);
                newBooking.setEndtime(endTime);

                newBooking.setBookingStatus(bookingStatusRepo.getReferenceById(BKNGSTAT_FINISHED));

                // create or get unscheduled slot
                Optional<Slot> unselectedSlot = slotRepo.findSlotByLeader_UseridAndSlotType_Typeid(leaderID, SLTYP_UNSCHEDULED);
                if (unselectedSlot.isPresent())
                {
                    newBooking.setSlot(unselectedSlot.get());
                }
                else
                {
                    Slot newSlot = new Slot();
                    newSlot.setSlotType(slotTypeRepo.getReferenceById(SLTYP_UNSCHEDULED));
                    newSlot.setLeader(userServ.getUser(leaderID));

                    Slot savedSlot = slotRepo.save(newSlot);
                    newBooking.setSlot(savedSlot);
                }
            }
            else
            {
                newBooking.setBookingStatus(bookingStatusRepo.getReferenceById(BKNGSTAT_ACTIVE));
                newBooking.setSlot(slotRepo.getReferenceById(slotID));
            }


            // if it has members, make the type of booking to be grouped
            if (bookingMembers != null && !bookingMembers.isEmpty())
            {
                // group booking
                if (unscheduled)
                {
                    newBooking.setBookingType(bookingTypeRepo.getReferenceById(BKNGTYP_GROUP_UNSCHEDULED));
                }
                else
                {
                    newBooking.setBookingType(bookingTypeRepo.getReferenceById(BKNGTYP_GROUP));
                }

                Booking createdBooking = bookingRepo.save(newBooking);

                // notify leader by booking
                Notification newNotification = new Notification();
                newNotification.setEntity("Booking");
                newNotification.setItemid(String.valueOf(createdBooking.getBookingid()));
                newNotification.setNotficmsg("new student booking");
                newNotification.setUser(createdBooking.getSlot().getLeader());
                newNotification.setSeen(false);

                notificationRepo.save(newNotification);

                // add group members
                for (BookingMemberDao member : bookingMembers)
                {
                    BookingMember newBookingMember = new BookingMember();

                    newBookingMember.setDatetime(Timestamp.from(Instant.now()));
                    newBookingMember.setStudent(userServ.getUser(member.getStudent().getUserid()));
                    newBookingMember.setBooking(bookingRepo.getReferenceById(createdBooking.getBookingid()));

                    BookingMember savedMember = bookingMemberRepo.save(newBookingMember);

                    // notify each student
                    newNotification.setNotficmsg("you have been added to a booking");
                    newNotification.setUser(savedMember.getStudent());

                    notificationRepo.save(newNotification);
                }

                // return dto with correct things
                return new BookingDao(bookingRepo.findById(createdBooking.getBookingid()).get());
            }
            else
            {
                // normal booking
                if (unscheduled)
                {
                    newBooking.setBookingType(bookingTypeRepo.getReferenceById(BKNGTYP_UNSCHEDULED));
                }
                else
                {
                    newBooking.setBookingType(bookingTypeRepo.getReferenceById(BKNGTYP_NORMAL));
                }

                Booking createdBooking = bookingRepo.save(newBooking);

                // notify leader
                Notification newNotification = new Notification();
                newNotification.setEntity("Booking");
                newNotification.setItemid(String.valueOf(createdBooking.getBookingid()));
                newNotification.setNotficmsg("new student booking");
                newNotification.setUser(createdBooking.getSlot().getLeader());
                newNotification.setSeen(false);

                notificationRepo.save(newNotification);

                // return dto with correct things
                return new BookingDao(bookingRepo.findById(createdBooking.getBookingid()).get());
            }


        }

        return null;

    }

    // create revision booking - by leader
    public BookingDao createNewRevision(Date bookingDate, String note, Timestamp startTime, Timestamp endTime, int bookingLimit, boolean online, String courseID, String leaderID)
    {
        List<String> errors = new ArrayList<>();

        // check if there is any other revisions at this time by this leader
        if (bookingRepo.existsByStudent_UseridAndBookingdateAndBookingStatus_StatusidAndBookingType_TypeidAndStarttimeBetweenOrEndtimeBetween(leaderID, bookingDate, BKNGSTAT_ACTIVE, BKNGTYP_REVISION, startTime, endTime, startTime, endTime))
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
            newRevision.setBookingType(bookingTypeRepo.getReferenceById(BKNGTYP_REVISION));
            newRevision.setBookingStatus(bookingStatusRepo.getReferenceById(BKNGSTAT_ACTIVE));
            newRevision.setCourse(courseRepo.getReferenceById(courseID));

            Booking savedRevision = bookingRepo.save(newRevision);

            return new BookingDao(savedRevision);
        }

        return null;
    }

    // edit booking / revision -- can set to canceled or change time etc
    public BookingDao updateBooking(int bookingID, char statusID, boolean studentRequest, Timestamp startTime, Timestamp endTime)
    {
        Optional<Booking> bookingToUpdate = bookingRepo.findById(bookingID);

        if (bookingToUpdate.isPresent())
        {
            bookingToUpdate.get().setBookingStatus(bookingStatusRepo.getReferenceById(statusID));

            if (statusID == BKNGSTAT_FINISHED)
            {
                bookingToUpdate.get().setStarttime(startTime);
                bookingToUpdate.get().setEndtime(endTime);
            }

            if (studentRequest)
            {
                // notify leader
                Notification newNotification = new Notification();
                newNotification.setEntity("Booking");
                newNotification.setItemid(String.valueOf(bookingID));
                newNotification.setNotficmsg("booking status changed");
                newNotification.setUser(bookingToUpdate.get().getSlot().getLeader());
                newNotification.setSeen(false);

                notificationRepo.save(newNotification);
            }
            else
            {
                // notify student
                Notification newNotification = new Notification();
                newNotification.setEntity("Booking");
                newNotification.setItemid(String.valueOf(bookingID));
                newNotification.setNotficmsg("booking status changed");
                newNotification.setUser(bookingToUpdate.get().getStudent());
                newNotification.setSeen(false);

                notificationRepo.save(newNotification);
            }

            if (bookingToUpdate.get().getBookingType().getTypeid() == BKNGTYP_GROUP)
            {
                for (BookingMember member : bookingToUpdate.get().getBookingMembers())
                {
                    // notify each student
                    Notification newNotification = new Notification();
                    newNotification.setEntity("Booking");
                    newNotification.setItemid(String.valueOf(bookingID));
                    newNotification.setNotficmsg("booking status changed");
                    newNotification.setUser(member.getStudent());
                    newNotification.setSeen(false);

                    notificationRepo.save(newNotification);
                }
            }

            return new BookingDao(bookingRepo.save(bookingToUpdate.get()));
        }
        else
        {
            return null;
        }
    }

    // delete booking - not by users
    public boolean deleteBooking(int bookingID)
    {
        bookingRepo.deleteById(bookingID);
        return true;
    }
}
