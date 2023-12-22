package polytechnic.bh.PassPlatforms_Backend.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import polytechnic.bh.PassPlatforms_Backend.Dao.BookingDao;
import polytechnic.bh.PassPlatforms_Backend.Dao.BookingMemberDao;
import polytechnic.bh.PassPlatforms_Backend.Dao.UserDao;
import polytechnic.bh.PassPlatforms_Backend.Dto.GenericDto;
import polytechnic.bh.PassPlatforms_Backend.Service.BookingMemberServ;
import polytechnic.bh.PassPlatforms_Backend.Service.BookingServ;
import polytechnic.bh.PassPlatforms_Backend.Service.UserServ;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import java.util.Objects;

import static polytechnic.bh.PassPlatforms_Backend.Constant.RoleConstant.*;
import static polytechnic.bh.PassPlatforms_Backend.Util.TokenValidation.isValidToken;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/revision")
public class RevisionCont
{
    @Autowired
    private BookingServ bookingServ;

    @Autowired
    private BookingMemberServ bookingMemberServ;

    @Autowired
    private UserServ userServ;

    // get all revisions - per school
    @GetMapping("/{schoolID}")
    public ResponseEntity<GenericDto<List<BookingDao>>> getSchoolRevisions(
            @RequestHeader(value = "Authorization") String requestKey,
            @PathVariable("schoolID") String schoolID)
    {
        String userID = isValidToken(requestKey);

        if (userID != null)
        {
            //token is valid, get user and role
            UserDao user = userServ.getUser(userID);

            if (user.getRole().getRoleid() == ROLE_ADMIN || user.getRole().getRoleid() == ROLE_MANAGER || user.getRole().getRoleid() == ROLE_STUDENT || user.getRole().getRoleid() == ROLE_LEADER || user.getRole().getRoleid() == ROLE_TUTOR)
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
        String userID = isValidToken(requestKey);

        if (userID != null)
        {
            //token is valid, get user and role
            UserDao user = userServ.getUser(userID);

            if (user.getRole().getRoleid() == ROLE_ADMIN || user.getRole().getRoleid() == ROLE_MANAGER || user.getRole().getRoleid() == ROLE_STUDENT || user.getRole().getRoleid() == ROLE_LEADER)
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
        else
        {
            return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
        }

    }

    // post a revision - only leaders
    @PostMapping("")
    public ResponseEntity<GenericDto<BookingDao>> createNewRevision(
            @RequestHeader(value = "Authorization") String requestKey,
            @RequestBody BookingDao bookingDao)
    {
        String userID = isValidToken(requestKey);

        if (userID != null)
        {
            //token is valid, get user and role
            UserDao user = userServ.getUser(userID);

            if (user.getRole().getRoleid() == ROLE_LEADER)
            {
                GenericDto<BookingDao> newRevision = bookingServ.createNewRevision(bookingDao.getBookingDate(), bookingDao.getNote(), Timestamp.from(bookingDao.getStarttime()), Timestamp.from(bookingDao.getEndtime()), bookingDao.getBookinglimit(), bookingDao.isIsonline(), bookingDao.getCourse().getCourseid(), userID);

                if (newRevision != null)
                {
                    if (newRevision.getTransObject() != null && newRevision.getError() == null)
                    {
                        return new ResponseEntity<>(new GenericDto<>(null, newRevision.getTransObject(), null, newRevision.getWarnings()), HttpStatus.OK);
                    }
                    else
                    {
                        return new ResponseEntity<>(new GenericDto<>(null, null, newRevision.getError(), null), HttpStatus.BAD_REQUEST);
                    }
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

    // register in revision
    @PostMapping("/{revisionID}/member")
    public ResponseEntity<GenericDto<BookingMemberDao>> registerMember(
            @RequestHeader(value = "Authorization") String requestKey,
            @PathVariable("revisionID") int revisionID)
    {
        String userID = isValidToken(requestKey);

        if (userID != null)
        {
            //token is valid, get user and role
            UserDao user = userServ.getUser(userID);

            if (user.getRole().getRoleid() == ROLE_STUDENT)
            {
                GenericDto<BookingMemberDao> newMember = bookingMemberServ.revisionSignUp(revisionID, userID);

                if (newMember != null)
                {
                    if (newMember.getTransObject() != null && newMember.getError() == null)
                    {
                        return new ResponseEntity<>(new GenericDto<>(null, newMember.getTransObject(), null, null), HttpStatus.OK);
                    }
                    else
                    {
                        return new ResponseEntity<>(new GenericDto<>(null, null, newMember.getError(), null), HttpStatus.BAD_REQUEST);
                    }
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

    // de-register in revision
    @DeleteMapping("/{revisionID}/member")
    public ResponseEntity<GenericDto<BookingDao>> unregisterMember(
            @RequestHeader(value = "Authorization") String requestKey,
            @PathVariable("revisionID") int revisionID)
    {
        String userID = isValidToken(requestKey);

        if (userID != null)
        {
            //token is valid, get user and role
            UserDao user = userServ.getUser(userID);

            if (user.getRole().getRoleid() == ROLE_STUDENT)
            {
                if (bookingMemberServ.removeStudentMember(revisionID, userID))
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

    // update a revision
    @PutMapping("/{revisionID}")
    public ResponseEntity<GenericDto<BookingDao>> updateRevision(
            @RequestHeader(value = "Authorization") String requestKey,
            @PathVariable("revisionID") int revisionID,
            @RequestAttribute(value = "statusID") char statusID,
            @RequestAttribute(value = "startTime", required = false) Instant startTime,
            @RequestAttribute(value = "endTime", required = false) Instant endTime)
    {
        String userID = isValidToken(requestKey);

        if (userID != null)
        {
            //token is valid, get user and role
            UserDao user = userServ.getUser(userID);

            if (user.getRole().getRoleid() == ROLE_ADMIN || user.getRole().getRoleid() == ROLE_MANAGER)
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
            else if (user.getRole().getRoleid() == ROLE_LEADER)
            {
                // does the leader own this booking?
                if (Objects.equals(bookingServ.getBookingDetails(revisionID).getStudent().getUserid(), userID))
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
        String userID = isValidToken(requestKey);

        if (userID != null)
        {
            //token is valid, get user and role
            UserDao user = userServ.getUser(userID);

            // only managers and admin are able to fully delete from the db
            if (user.getRole().getRoleid() == ROLE_MANAGER || user.getRole().getRoleid() == ROLE_ADMIN)
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
        else
        {
            return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
        }

    }
}
