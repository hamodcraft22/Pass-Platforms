package polytechnic.bh.PassPlatforms_Backend.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import polytechnic.bh.PassPlatforms_Backend.Dao.BookingDao;
import polytechnic.bh.PassPlatforms_Backend.Dto.GenericDto;
import polytechnic.bh.PassPlatforms_Backend.Service.BookingMemberServ;
import polytechnic.bh.PassPlatforms_Backend.Service.BookingServ;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import java.util.Objects;

import static polytechnic.bh.PassPlatforms_Backend.Constant.APIkeyConstant.*;

@RestController
@RequestMapping("/api/revision")
public class RevisionCont
{
    @Autowired
    private BookingServ bookingServ;

    @Autowired
    private BookingMemberServ bookingMemberServ;

    // get all revisions - per school
    @GetMapping("/{schoolID}")
    public ResponseEntity<GenericDto<List<BookingDao>>> getSchoolRevisions(
            @RequestHeader(value = "Authorization") String requestKey,
            @PathVariable("schoolID") String schoolID)
    {
        if (Objects.equals(requestKey, ADMIN_KEY) || Objects.equals(requestKey, MANAGER_KEY) || Objects.equals(requestKey, STUDENT_KEY) || Objects.equals(requestKey, LEADER_KEY) || Objects.equals(requestKey, TUTOR_KEY))
        {
            List<BookingDao> bookings = bookingServ.getSchoolRevisions(schoolID);

            if (bookings != null && !bookings.isEmpty())
            {
                return new ResponseEntity<>(new GenericDto<>(null, bookings, null, null), HttpStatus.OK);
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

    // get revision details - anyone
    @GetMapping("/{revisionID}")
    public ResponseEntity<GenericDto<BookingDao>> getRevisionDetails(
            @RequestHeader(value = "Authorization") String requestKey,
            @PathVariable("revisionID") int revisionID)
    {
        if (Objects.equals(requestKey, ADMIN_KEY) || Objects.equals(requestKey, MANAGER_KEY) || Objects.equals(requestKey, STUDENT_KEY) || Objects.equals(requestKey, LEADER_KEY))
        {
            BookingDao booking = bookingServ.getBookingDetails(revisionID);

            if (booking != null)
            {
                return new ResponseEntity<>(new GenericDto<>(null, booking, null, null), HttpStatus.OK);
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

    // post a revision - only leaders
    @PostMapping("")
    public ResponseEntity<GenericDto<BookingDao>> createNewRevision(
            @RequestHeader(value = "Authorization") String requestKey,
            @RequestHeader(value = "Requester") String requisterID,
            @RequestBody BookingDao bookingDao)
    {
        if (Objects.equals(requestKey, LEADER_KEY))
        {
            if (bookingServ.createNewRevision(bookingDao.getBookingDate(), bookingDao.getNote(), Timestamp.from(bookingDao.getStarttime()), Timestamp.from(bookingDao.getEndtime()), bookingDao.getBookinglimit(), bookingDao.isIsonline(), bookingDao.getCourse().getCourseid(), requisterID) != null)
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

    // register in revision
    @PostMapping("/{revisionID}/member")
    public ResponseEntity<GenericDto<BookingDao>> registerMember(
            @RequestHeader(value = "Authorization") String requestKey,
            @RequestHeader(value = "Requester") String requisterID,
            @PathVariable("revisionID") int revisionID)
    {
        if (Objects.equals(requestKey, STUDENT_KEY))
        {
            if (bookingMemberServ.revisionSignUp(revisionID, requisterID) != null)
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

    // de-register in revision
    @DeleteMapping("/{revisionID}/member")
    public ResponseEntity<GenericDto<BookingDao>> unregisterMember(
            @RequestHeader(value = "Authorization") String requestKey,
            @RequestHeader(value = "Requester") String requisterID,
            @PathVariable("revisionID") int revisionID)
    {
        if (Objects.equals(requestKey, STUDENT_KEY))
        {
            if (bookingMemberServ.removeStudentMember(revisionID, requisterID))
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

    // update a revision
    @PutMapping("/{revisionID}")
    public ResponseEntity<GenericDto<BookingDao>> updateRevision(
            @RequestHeader(value = "Authorization") String requestKey,
            @RequestHeader(value = "Requester") String requisterID,
            @PathVariable("revisionID") int revisionID,
            @RequestAttribute(value = "statusID") char statusID,
            @RequestAttribute(value = "startTime", required = false) Instant startTime,
            @RequestAttribute(value = "endTime", required = false) Instant endTime)
    {
        if (Objects.equals(requestKey, ADMIN_KEY) || Objects.equals(requestKey, MANAGER_KEY))
        {
            if (bookingServ.updateBooking(revisionID, statusID, false, (startTime == null ? null : Timestamp.from(startTime)), (endTime == null ? null : Timestamp.from(endTime))) != null)
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
            if (Objects.equals(bookingServ.getBookingDetails(revisionID).getStudent().getUserid(), requisterID))
            {
                if (bookingServ.updateBooking(revisionID, statusID, false, (startTime == null ? null : Timestamp.from(startTime)), (endTime == null ? null : Timestamp.from(endTime))) != null)
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

    // delete revision
    @DeleteMapping("/{revisionID}")
    public ResponseEntity<GenericDto<BookingDao>> deleteRevision(
            @RequestHeader(value = "Authorization") String requestKey,
            @PathVariable("revisionID") int revisionID
    )
    {
        // only managers and admin are able to fully delete from the db
        if (Objects.equals(requestKey, MANAGER_KEY) || Objects.equals(requestKey, ADMIN_KEY))
        {
            if (bookingServ.deleteBooking(revisionID))
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
