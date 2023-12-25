package polytechnic.bh.PassPlatforms_Backend.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import polytechnic.bh.PassPlatforms_Backend.Dao.*;
import polytechnic.bh.PassPlatforms_Backend.Dto.GenericDto;
import polytechnic.bh.PassPlatforms_Backend.Dto.RevisionSlotsDto;
import polytechnic.bh.PassPlatforms_Backend.Entity.*;
import polytechnic.bh.PassPlatforms_Backend.Repository.*;

import java.awt.print.Book;
import java.sql.Timestamp;
import java.time.DayOfWeek;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.TemporalAdjusters;
import java.util.*;
import java.util.stream.Collectors;

import static polytechnic.bh.PassPlatforms_Backend.Constant.BookingStatusConstant.BKNGSTAT_ACTIVE;
import static polytechnic.bh.PassPlatforms_Backend.Constant.BookingStatusConstant.BKNGSTAT_FINISHED;
import static polytechnic.bh.PassPlatforms_Backend.Constant.BookingTypeConstant.*;
import static polytechnic.bh.PassPlatforms_Backend.Constant.SlotTypeConstant.*;
import static polytechnic.bh.PassPlatforms_Backend.Util.UsersService.getAzureAdName;

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

    @Autowired
    private MetadataServ metadataServ;

    @Autowired
    private BookingMemberServ bookingMemberServ;

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

    // get student bookings
    public List<BookingDao> getStudentBookings(String studentID)
    {
        List<BookingDao> bookings = new ArrayList<>();

        for (Booking retrivedBooking : bookingRepo.findAllStudentBookings(studentID))
        {
            bookings.add(new BookingDao(retrivedBooking));
        }

        return bookings;
    }

    // get member bookings
    public List<BookingDao> getMemberBookings(String studentID)
    {
        List<BookingDao> bookings = new ArrayList<>();

        for (Booking retrivedBooking : bookingRepo.findAllMemberBookings(studentID))
        {
            bookings.add(new BookingDao(retrivedBooking));
        }

        return bookings;
    }

    // get leader bookings
    public List<BookingDao> getLeaderBookings(String leaderID)
    {
        List<BookingDao> bookings = new ArrayList<>();

        for (Booking retrivedBooking : bookingRepo.findAllLeaderBookings(leaderID))
        {
            bookings.add(new BookingDao(retrivedBooking));
        }

        return bookings;
    }

    // get open revisions
    public List<RevisionSlotsDto> getCourseRevs(String courseID, Date weekStartDate)
    {
        // get all revisions for a course (within the week range);
        Date weekEndDate = Date.from(weekStartDate.toInstant().plusSeconds(518400));

        // revisions for course within range
        List<Booking> revisions = bookingRepo.findAllCourseRevisions(courseID, weekStartDate, weekEndDate);

        // revisions mapped by user
        Map<User, List<Booking>> mappedRevisions = revisions.stream().collect(Collectors.groupingBy(Booking::getStudent));

        // final list of bookings to be returned
        List<RevisionSlotsDto> revisionsAvlb = new ArrayList<>();

        for (User user : mappedRevisions.keySet())
        {
            // revisions which are still open (have space)
            List<BookingDao> bookingsDao = new ArrayList<>();

            for (Booking userRevision : mappedRevisions.get(user))
            {
                if (userRevision.getBookingMembers().size() < userRevision.getBookinglimit())
                {
                    bookingsDao.add(new BookingDao(userRevision));
                }
            }

            // if the user does have any revisions
            if (!bookingsDao.isEmpty())
            {
                RevisionSlotsDto newRevision = new RevisionSlotsDto();

                newRevision.setLeaderID(user.getUserid());
                newRevision.setLeaderName(getAzureAdName(user.getUserid()));
                newRevision.setRevisions(bookingsDao);

                revisionsAvlb.add(newRevision);
            }
        }

        // return
        return revisionsAvlb;
    }

    // get student revisions
    public List<BookingDao> getStudentRevisions(String studentID)
    {
        List<BookingDao> bookings = new ArrayList<>();

        for (Booking retrivedBooking : bookingRepo.findAllStudentRevisions(studentID))
        {
            bookings.add(new BookingDao(retrivedBooking));
        }

        return bookings;
    }

    // get leader revisions
    public List<BookingDao> getLeaderRevisions(String leaderID)
    {
        List<BookingDao> bookings = new ArrayList<>();

        for (Booking retrivedBooking : bookingRepo.findAllLeaderRevisions(leaderID))
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
    public GenericDto<BookingDao> createNewBooking(Date bookingDate, String note, boolean online, int slotID, String studentID, String courseID, List<BookingMemberDao> bookingMembers, boolean unscheduled, Timestamp startTime, Timestamp endTime, String leaderID)
    {
        List<String> errors = new ArrayList<>();

        Calendar cal = Calendar.getInstance();

        MetadataDao metadata = metadataServ.getMetadata();

        // check if booking is allowed
        if (metadata != null && !metadata.isBooking())
        {
            errors.add("creating bookings is not allowed currently");
        }

        // get the slot is available
        Optional<Slot> retrivedSlot = slotRepo.findById(slotID);

        // if session is unscheduled, skip all checks and add (by pass leader)
        if (!unscheduled && errors.isEmpty())
        {
            // check if in exam week - convert to local dates
            if (metadata != null && (
                    (bookingDate.toInstant().atZone(ZoneId.of("Asia/Bahrain")).toLocalDate().isAfter(metadata.getFrwstart().toInstant().atZone(ZoneId.of("Asia/Bahrain")).toLocalDate()) && bookingDate.toInstant().atZone(ZoneId.of("Asia/Bahrain")).toLocalDate().isBefore(metadata.getMrwend().toInstant().atZone(ZoneId.of("Asia/Bahrain")).toLocalDate())) ||
                            (bookingDate.toInstant().atZone(ZoneId.of("Asia/Bahrain")).toLocalDate().isAfter(metadata.getFrwstart().toInstant().atZone(ZoneId.of("Asia/Bahrain")).toLocalDate()) && bookingDate.toInstant().atZone(ZoneId.of("Asia/Bahrain")).toLocalDate().isBefore(metadata.getFrwend().toInstant().atZone(ZoneId.of("Asia/Bahrain")).toLocalDate())) ||
                            (bookingDate.toInstant().atZone(ZoneId.of("Asia/Bahrain")).toLocalDate().isAfter(metadata.getMwstart().toInstant().atZone(ZoneId.of("Asia/Bahrain")).toLocalDate()) && bookingDate.toInstant().atZone(ZoneId.of("Asia/Bahrain")).toLocalDate().isBefore(metadata.getMwend().toInstant().atZone(ZoneId.of("Asia/Bahrain")).toLocalDate())) ||
                            (bookingDate.toInstant().atZone(ZoneId.of("Asia/Bahrain")).toLocalDate().isAfter(metadata.getFwstart().toInstant().atZone(ZoneId.of("Asia/Bahrain")).toLocalDate()) && bookingDate.toInstant().atZone(ZoneId.of("Asia/Bahrain")).toLocalDate().isBefore(metadata.getFwend().toInstant().atZone(ZoneId.of("Asia/Bahrain")).toLocalDate()))))
            {
                errors.add("normal bookings are not allowed withing exam / exam break weeks");
            }

            // check if the booking is in the feature -- checked
            if (bookingDate.toInstant().atZone(ZoneId.of("Asia/Bahrain")).toLocalDate().isBefore(LocalDate.now(ZoneId.of("Asia/Bahrain"))))
            {
                errors.add("cannot book in the past");
            }

            // check if it is a same day booking -- checked
            if (Objects.equals(bookingDate.toInstant().atZone(ZoneId.of("Asia/Bahrain")).toLocalDate(), LocalDate.now(ZoneId.of("Asia/Bahrain"))))
            {
                errors.add("cannot make a booking in the same day");
            }

            // check if slot is available -- checked
            if (retrivedSlot.isPresent())
            {
                cal.setTime(bookingDate);

                // check if user is trying to book himself
                if (Objects.equals(studentID, retrivedSlot.get().getLeader().getUserid()))
                {
                    errors.add("you cannot book yourself");
                }

                // check if the date is the of the same day of the slot - checked
                if (cal.get(Calendar.DAY_OF_WEEK) != retrivedSlot.get().getDay().getDayNum())
                {
                    errors.add("selected date day and slot day does not match");
                }

                // check if any active bookings are under this slot in the date selected - checked
                if (bookingRepo.activeUnderSlot(slotID, bookingDate) != 0)
                {
                    errors.add("the booking slot is booked by another student");
                }

                // check if the leader chosen teaches the course - checked
                if (!offeredCourseRepo.existsByLeader_UseridAndCourse_Courseid(retrivedSlot.get().getLeader().getUserid(), courseID))
                {
                    errors.add("selected PASS Leader does not teach the course selected");
                }

                // check if user has current bookings at this time - checked
                if (bookingRepo.sameTimeSessionsFind(studentID, bookingDate, retrivedSlot.get().getStarttime(), retrivedSlot.get().getEndtime()) != 0)
                {
                    errors.add("you have another booking at the same time");
                }

                // check if user is a member of any sessions at this time -- checked
                if (bookingMemberRepo.sameTimeMemberSessionsFind(studentID, bookingDate, retrivedSlot.get().getStarttime(), retrivedSlot.get().getEndtime()) != 0)
                {
                    errors.add("you have another (group) session you are a part of at the same time");
                }

                // check if student has no classes - in schedule - at this time -- checked
                if (scheduleRepo.sameTimeClassesFind(studentID, retrivedSlot.get().getDay().getDayid(), retrivedSlot.get().getStarttime(), retrivedSlot.get().getEndtime()) != 0)
                {
                    errors.add("you have a class in the same time as the booking session");
                }

                // booking check - fair distribution policy -- checked
                LocalDate specificDate = bookingDate.toInstant().atZone(ZoneId.of("Asia/Bahrain")).toLocalDate().with(TemporalAdjusters.previousOrSame(DayOfWeek.SUNDAY)).atStartOfDay(ZoneId.of("Asia/Bahrain")).toLocalDate();
                if (bookingRepo.sameLeaderBookingFind(studentID, retrivedSlot.get().getLeader().getUserid(), Date.from(specificDate.atStartOfDay(ZoneId.of("Asia/Bahrain")).toInstant()), Date.from(specificDate.plusDays(6).atStartOfDay(ZoneId.of("Asia/Bahrain")).toInstant())) >= 1)
                {
                    errors.add("you have already booked one session with this leader this week");
                }
            }
            else
            {
                errors.add("slot given does not exist");
            }
        }


        // create the booking
        if (errors.isEmpty())
        {
            Booking newBooking = new Booking();

            // setting basic info
            newBooking.setDatebooked(Timestamp.from(Instant.now()));
            newBooking.setBookingdate(new java.sql.Date(bookingDate.getTime()));
            newBooking.setNote(note);

            // override booking type based on slot restriction
            if (retrivedSlot.isPresent())
            {
                if (retrivedSlot.get().getSlotType().getTypeid() == SLTYP_ONLINE)
                {
                    newBooking.setIsonline(true);
                }
                else if (retrivedSlot.get().getSlotType().getTypeid() == SLTYP_PHYSICAL)
                {
                    newBooking.setIsonline(false);
                }
            }
            else
            {
                newBooking.setIsonline(online);
            }

            // other booking static info
            newBooking.setStudent(new User(userServ.getUser(studentID)));
            newBooking.setCourse(courseRepo.getReferenceById(courseID));

            // if the booking is unscheduled - leader did it at their own - none slot - time, add the unscheduled slot
            if (unscheduled)
            {
                // add the real start and end time directory
                newBooking.setStarttime(startTime);
                newBooking.setEndtime(endTime);

                // set it as complete -
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
                    newSlot.setLeader(new User(userServ.getUser(leaderID)));

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

                if (!unscheduled)
                {
                    notificationRepo.save(newNotification);
                }

                List<String> warnings = new ArrayList<>();

                // add group members
                for (BookingMemberDao member : bookingMembers)
                {
                    GenericDto<BookingMemberDao> newMember = bookingMemberServ.addStudentMember(createdBooking.getBookingid(), member.getStudent().getUserid());
                    if (newMember.getError() != null)
                    {
                        warnings.add("Student " + member.getStudent().getUserid() + " could not be added, they have a clash with their bookings / scheduels");
                    }
                }

                // return dto with correct things
                return new GenericDto<>(null, new BookingDao(bookingRepo.findById(createdBooking.getBookingid()).get()), null, warnings);
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
                return new GenericDto<>(null, new BookingDao(bookingRepo.findById(createdBooking.getBookingid()).get()), null, null);
            }
        }
        else
        {
            return new GenericDto<>(null, null, errors, null);
        }
    }

    // create revision booking - by leader
    public GenericDto<BookingDao> createNewRevision(Date bookingDate, String note, Timestamp startTime, Timestamp endTime, int bookingLimit, boolean online, String courseID, String leaderID)
    {
        List<String> errors = new ArrayList<>();

        // check if there is any other revisions at this time by this leader
        if (bookingRepo.sameLeaderRevisionTimeFind(leaderID, bookingDate, startTime, endTime) != 0)
        {
            errors.add("there is another session or schedule within the selected time");
        }

//        // check if the leader teaches the course
//        if (!offeredCourseRepo.existsByLeader_UseridAndCourse_Courseid(leaderID, courseID))
//        {
//            errors.add("selected PASS Leader does not teach the course selected");
//        }

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
            newRevision.setStudent(new User(userServ.getUser(leaderID)));

            Booking savedRevision = bookingRepo.save(newRevision);

            return new GenericDto<>(null, new BookingDao(savedRevision), null, null);
        }
        else
        {
            return new GenericDto<>(null, null, errors, null);
        }
    }

    // edit booking / revision -- can set to canceled or change time etc
    public BookingDao updateBooking(int bookingID, char statusID, boolean studentRequest, Timestamp startTime, Timestamp endTime)
    {
        Optional<Booking> bookingToUpdate = bookingRepo.findById(bookingID);

        if (bookingToUpdate.isPresent())
        {
            bookingToUpdate.get().setBookingStatus(bookingStatusRepo.getReferenceById(statusID));

            if (statusID == BKNGSTAT_FINISHED && bookingToUpdate.get().getBookingType().getTypeid() != BKNGTYP_REVISION)
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
