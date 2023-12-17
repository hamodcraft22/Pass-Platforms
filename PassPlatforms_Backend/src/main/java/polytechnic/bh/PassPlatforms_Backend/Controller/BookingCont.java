package polytechnic.bh.PassPlatforms_Backend.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import polytechnic.bh.PassPlatforms_Backend.Dao.BookingDao;
import polytechnic.bh.PassPlatforms_Backend.Dao.BookingMemberDao;
import polytechnic.bh.PassPlatforms_Backend.Dto.GenericDto;
import polytechnic.bh.PassPlatforms_Backend.Service.BookingMemberServ;
import polytechnic.bh.PassPlatforms_Backend.Service.BookingServ;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import java.util.Objects;

import static polytechnic.bh.PassPlatforms_Backend.Constant.APIkeyConstant.*;

@RestController
@RequestMapping("/api/booking")
public class BookingCont
{
    @Autowired
    private BookingServ bookingServ;

    @Autowired
    private BookingMemberServ bookingMemberServ;


    // get all bookings - only managers
    @GetMapping("")
    public ResponseEntity<GenericDto<List<BookingDao>>> getAllBookings(
            @RequestHeader(value = "Authorization") String requestKey)
    {
        if (Objects.equals(requestKey, ADMIN_KEY) || Objects.equals(requestKey, MANAGER_KEY))
        {
            List<BookingDao> bookings = bookingServ.getAllBookings();

            if (bookings != null && !bookings.isEmpty())
            {
                return new ResponseEntity<>(new GenericDto<>(null, bookings, null), HttpStatus.OK);
            }
            else
            {
                return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
            }
        }
        else
        {
            return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
        }
    }

    // get all bookings - per school
    @GetMapping("/{schoolID}")
    public ResponseEntity<GenericDto<List<BookingDao>>> getSchoolBookings(
            @RequestHeader(value = "Authorization") String requestKey,
            @PathVariable("schoolID") String schoolID)
    {
        if (Objects.equals(requestKey, ADMIN_KEY) || Objects.equals(requestKey, MANAGER_KEY) || Objects.equals(requestKey, STUDENT_KEY) || Objects.equals(requestKey, LEADER_KEY) || Objects.equals(requestKey, TUTOR_KEY))
        {
            List<BookingDao> bookings = bookingServ.getSchoolSessions(schoolID);

            if (bookings != null && !bookings.isEmpty())
            {
                return new ResponseEntity<>(new GenericDto<>(null, bookings, null), HttpStatus.OK);
            }
            else
            {
                return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
            }
        }
        else
        {
            return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
        }
    }

    // get booking details
    @GetMapping("/{bookingID}")
    public ResponseEntity<GenericDto<BookingDao>> getBookingDetails(
            @RequestHeader(value = "Authorization") String requestKey,
            @RequestHeader(value = "Requester") String requisterID,
            @PathVariable("bookingID") int bookingID)
    {
        if (Objects.equals(requestKey, ADMIN_KEY) || Objects.equals(requestKey, MANAGER_KEY))
        {
            BookingDao booking = bookingServ.getBookingDetails(bookingID);

            if (booking != null)
            {
                return new ResponseEntity<>(new GenericDto<>(null, booking, null), HttpStatus.OK);
            }
            else
            {
                return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
            }
        }
        else if (Objects.equals(requestKey, STUDENT_KEY))
        {
            // check if student booked, or is a member of the booking
            BookingDao retrivedBooking = bookingServ.getBookingDetails(bookingID);

            if (retrivedBooking != null)
            {
                if (Objects.equals(retrivedBooking.getStudent().getUserid(), requisterID))
                {
                    return new ResponseEntity<>(new GenericDto<>(null, retrivedBooking, null), HttpStatus.OK);
                }
                else
                {
                    // loop to see if member of
                    boolean hasAcss = false;

                    for (BookingMemberDao bookingMember : retrivedBooking.getBookingMembers())
                    {
                        if (Objects.equals(bookingMember.getStudent().getUserid(), requisterID))
                        {
                            hasAcss = true;
                            break;
                        }
                    }

                    if (hasAcss)
                    {
                        return new ResponseEntity<>(new GenericDto<>(null, retrivedBooking, null), HttpStatus.OK);
                    }
                    else
                    {
                        return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
                    }

                }
            }
            else
            {
                return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
            }
        }
        else if (Objects.equals(requestKey, LEADER_KEY))
        {
            BookingDao retrivedBooking = bookingServ.getBookingDetails(bookingID);

            if (retrivedBooking != null)
            {
                if (Objects.equals(retrivedBooking.getSlot().getLeader().getUserid(), requisterID))
                {
                    return new ResponseEntity<>(new GenericDto<>(null, retrivedBooking, null), HttpStatus.OK);
                }
                else
                {
                    return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
                }
            }
            else
            {
                return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
            }
        }
        else
        {
            return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
        }
    }

    // post a normal booking (as a student)
    @PostMapping("")
    public ResponseEntity<GenericDto<BookingDao>> createNewBooking(
            @RequestHeader(value = "Authorization") String requestKey,
            @RequestHeader(value = "Requester") String requisterID,
            @RequestBody BookingDao bookingDao)
    {
        if (Objects.equals(requestKey, STUDENT_KEY) || Objects.equals(requestKey, LEADER_KEY))
        {
            BookingDao newBooking = bookingServ.createNewBooking(bookingDao.getBookingDate(), bookingDao.getNote(), bookingDao.isIsonline(), bookingDao.getSlot().getSlotid(), requisterID, bookingDao.getCourse().getCourseid(), bookingDao.getBookingMembers(), false, null, null, null);

            if (newBooking != null)
            {
                return new ResponseEntity<>(new GenericDto<>(null , newBooking, null), HttpStatus.OK);
            }
            else
            {
                return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
            }

        }
        else
        {
            return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
        }

    }

    // post an unscheduled booking (as a leader for a student)
    @PostMapping("/unscheduled")
    public ResponseEntity<GenericDto<BookingDao>> createUnscheduledBooking(
            @RequestHeader(value = "Authorization") String requestKey,
            @RequestHeader(value = "Requester") String requisterID,
            @RequestBody BookingDao bookingDao)
    {
        if (Objects.equals(requestKey, STUDENT_KEY) || Objects.equals(requestKey, LEADER_KEY))
        {
            // TODO null checks

            if (bookingServ.createNewBooking(bookingDao.getBookingDate(), bookingDao.getNote(), bookingDao.isIsonline(), bookingDao.getSlot().getSlotid(), bookingDao.getStudent().getUserid(), bookingDao.getCourse().getCourseid(), bookingDao.getBookingMembers(), true, Timestamp.from(bookingDao.getStarttime()), Timestamp.from(bookingDao.getEndtime()), "LEADERID") != null)
            {
                return new ResponseEntity<>(null, HttpStatus.OK);
            }
            else
            {
                return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
            }

        }
        else
        {
            return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
        }

    }

    // add booking member
    @PostMapping("/{bookingID}/member")
    public ResponseEntity<GenericDto<BookingDao>> addNewMember(
            @RequestHeader(value = "Authorization") String requestKey,
            @RequestHeader(value = "Requester") String requisterID,
            @PathVariable("bookingID") int bookingID,
            @RequestAttribute(value = "studentID") String studentID)
    {
        if (Objects.equals(requestKey, STUDENT_KEY))
        {
            // TODO null checks

            // if the user is the one who booked the session
            if (Objects.equals(bookingServ.getBookingDetails(bookingID).getStudent().getUserid(), requisterID))
            {
                if (bookingMemberServ.addStudentMember(bookingID, studentID) != null)
                {
                    return new ResponseEntity<>(null, HttpStatus.OK);
                }
                else
                {
                    return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
                }
            }
            else
            {
                return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
            }
        }
        else
        {
            return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
        }

    }

    // remove booking member
    @DeleteMapping("/{bookingID}/member")
    public ResponseEntity<GenericDto<BookingDao>> removeMember(
            @RequestHeader(value = "Authorization") String requestKey,
            @RequestHeader(value = "Requester") String requisterID,
            @PathVariable("bookingID") int bookingID,
            @RequestAttribute(value = "studentID") String studentID)
    {
        if (Objects.equals(requestKey, STUDENT_KEY))
        {
            // TODO null checks

            // if the user is the one who booked the session
            if (Objects.equals(bookingServ.getBookingDetails(bookingID).getStudent().getUserid(), requisterID))
            {
                if (bookingMemberServ.removeStudentMember(bookingID, studentID))
                {
                    return new ResponseEntity<>(null, HttpStatus.OK);
                }
                else
                {
                    return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
                }
            }
            else
            {
                return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
            }
        }
        else
        {
            return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
        }

    }

    // update booking - only status / times for leader
    @PutMapping("/{bookingID}")
    public ResponseEntity<GenericDto<BookingDao>> updateBooking(
            @RequestHeader(value = "Authorization") String requestKey,
            @RequestHeader(value = "Requester") String requisterID,
            @PathVariable("bookingID") int bookingID,
            @RequestAttribute(value = "statusID") char statusID,
            @RequestAttribute(value = "startTime", required = false) Instant startTime,
            @RequestAttribute(value = "endTime", required = false) Instant endTime)
    {
        if (Objects.equals(requestKey, ADMIN_KEY) || Objects.equals(requestKey, MANAGER_KEY))
        {
            if (bookingServ.updateBooking(bookingID, statusID, false, (startTime == null ? null : Timestamp.from(startTime)), (endTime == null ? null : Timestamp.from(endTime))) != null)
            {
                return new ResponseEntity<>(null, HttpStatus.OK);
            }
            else
            {
                return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
            }
        }
        else if (Objects.equals(requestKey, LEADER_KEY))
        {
            // does the leader own this booking?

            if (Objects.equals(bookingServ.getBookingDetails(bookingID).getSlot().getLeader().getUserid(), requisterID))
            {
                if (bookingServ.updateBooking(bookingID, statusID, false, (startTime == null ? null : Timestamp.from(startTime)), (endTime == null ? null : Timestamp.from(endTime))) != null)
                {
                    return new ResponseEntity<>(null, HttpStatus.OK);
                }
                else
                {
                    return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
                }
            }
            else
            {
                return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
            }
        }
        else if (Objects.equals(requestKey, STUDENT_KEY))
        {
            // is the student the one who booked the session?

            if (Objects.equals(bookingServ.getBookingDetails(bookingID).getStudent().getUserid(), requisterID))
            {
                if (bookingServ.updateBooking(bookingID, statusID, true, (startTime == null ? null : Timestamp.from(startTime)), (endTime == null ? null : Timestamp.from(endTime))) != null)
                {
                    return new ResponseEntity<>(null, HttpStatus.OK);
                }
                else
                {
                    return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
                }
            }
            else
            {
                return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
            }
        }
        else
        {
            return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
        }
    }


    // actually delete booking - managers only
    @DeleteMapping("/{bookingID}")
    public ResponseEntity<GenericDto<BookingDao>> deleteBooking(
            @RequestHeader(value = "Authorization") String requestKey,
            @PathVariable("bookingID") int bookingID
    )
    {
        // only managers and admin are able to fully delete from the db
        if (Objects.equals(requestKey, MANAGER_KEY) || Objects.equals(requestKey, ADMIN_KEY))
        {
            if (bookingServ.deleteBooking(bookingID))
            {
                return new ResponseEntity<>(null, HttpStatus.OK);
            }
            else
            {
                return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
            }
        }
        else
        {
            return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
        }
    }
}
