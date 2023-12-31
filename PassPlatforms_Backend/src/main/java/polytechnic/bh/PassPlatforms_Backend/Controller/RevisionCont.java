package polytechnic.bh.PassPlatforms_Backend.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import polytechnic.bh.PassPlatforms_Backend.Dao.BookingDao;
import polytechnic.bh.PassPlatforms_Backend.Dao.BookingMemberDao;
import polytechnic.bh.PassPlatforms_Backend.Dao.UserDao;
import polytechnic.bh.PassPlatforms_Backend.Dto.GenericDto;
import polytechnic.bh.PassPlatforms_Backend.Dto.RevisionSlotsDto;
import polytechnic.bh.PassPlatforms_Backend.Service.BookingMemberServ;
import polytechnic.bh.PassPlatforms_Backend.Service.BookingServ;
import polytechnic.bh.PassPlatforms_Backend.Service.LogServ;
import polytechnic.bh.PassPlatforms_Backend.Service.UserServ;

import java.sql.Timestamp;
import java.util.Date;
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

    @Autowired
    private LogServ logServ;

    // get all revisions - per school
    @GetMapping("/school/{schoolID}")
    public ResponseEntity<GenericDto<List<BookingDao>>> getSchoolRevisions(
            @RequestHeader(value = "Authorization") String requestKey,
            @PathVariable("schoolID") String schoolID)
    {
        String userID = isValidToken(requestKey);

        try
        {
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
        catch (Exception ex)
        {
            logServ.createLog(ex.getMessage(), userID);
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    // get revision details - anyone
    @GetMapping("/{revisionID}")
    public ResponseEntity<GenericDto<BookingDao>> getRevisionDetails(
            @RequestHeader(value = "Authorization") String requestKey,
            @PathVariable("revisionID") int revisionID)
    {
        String userID = isValidToken(requestKey);

        try
        {
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
        catch (Exception ex)
        {
            logServ.createLog(ex.getMessage(), userID);
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    // get open revisions for a course
    @GetMapping("/course/{courseID}")
    public ResponseEntity<GenericDto<List<RevisionSlotsDto>>> getCourseRevisions(
            @RequestHeader(value = "Authorization") String requestKey,
            @PathVariable(value = "courseID") String courseID,
            @RequestParam(value = "weekStart") Date weekStart)
    {
        String userID = isValidToken(requestKey);

        try
        {
            if (userID != null)
            {

                List<RevisionSlotsDto> revisions = bookingServ.getCourseRevs(courseID, weekStart);

                if (revisions != null && !revisions.isEmpty())
                {
                    return new ResponseEntity<>(new GenericDto<>(null, revisions, null, null), HttpStatus.OK);
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
        catch (Exception ex)
        {
            logServ.createLog(ex.getMessage(), userID);
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    // get student revisions
    @GetMapping("/student/{studentID}")
    public ResponseEntity<GenericDto<List<BookingDao>>> getStudentRevisions(
            @RequestHeader(value = "Authorization") String requestKey,
            @PathVariable("studentID") String studentID)
    {
        String userID = isValidToken(requestKey);

        try
        {
            if (userID != null)
            {
                //token is valid, get user and role
                UserDao user = userServ.getUser(userID);

                if (user.getRole().getRoleid() == ROLE_STUDENT || user.getRole().getRoleid() == ROLE_LEADER || user.getRole().getRoleid() == ROLE_MANAGER || user.getRole().getRoleid() == ROLE_ADMIN)
                {
                    List<BookingDao> bookings = bookingServ.getStudentRevisions(studentID);

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
        catch (Exception ex)
        {
            logServ.createLog(ex.getMessage(), userID);
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }


    // get student revisions
    @GetMapping("/leader/{leaderID}")
    public ResponseEntity<GenericDto<List<BookingDao>>> getLeaderRevisions(
            @RequestHeader(value = "Authorization") String requestKey,
            @PathVariable("leaderID") String leaderID)
    {
        String userID = isValidToken(requestKey);

        try
        {
            if (userID != null)
            {
                //token is valid, get user and role
                UserDao user = userServ.getUser(userID);

                if (user.getRole().getRoleid() == ROLE_STUDENT || user.getRole().getRoleid() == ROLE_LEADER || user.getRole().getRoleid() == ROLE_MANAGER || user.getRole().getRoleid() == ROLE_ADMIN)
                {
                    List<BookingDao> bookings = bookingServ.getLeaderRevisions(leaderID);

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
        catch (Exception ex)
        {
            logServ.createLog(ex.getMessage(), userID);
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }


    // create a revision - only leaders -- tested | added
    @PostMapping("")
    public ResponseEntity<GenericDto<BookingDao>> createNewRevision(
            @RequestHeader(value = "Authorization") String requestKey,
            @RequestBody BookingDao bookingDao)
    {
        String userID = isValidToken(requestKey);

        try
        {
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
        catch (Exception ex)
        {
            logServ.createLog(ex.getMessage(), userID);
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }

    }

    // register in revision
    @PostMapping("/{revisionID}/member")
    public ResponseEntity<GenericDto<BookingMemberDao>> registerMember(
            @RequestHeader(value = "Authorization") String requestKey,
            @PathVariable("revisionID") int revisionID)
    {
        String userID = isValidToken(requestKey);

        try
        {
            if (userID != null)
            {
                //token is valid, get user and role
                UserDao user = userServ.getUser(userID);

                if (user.getRole().getRoleid() == ROLE_STUDENT || user.getRole().getRoleid() == ROLE_LEADER)
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
        catch (Exception ex)
        {
            logServ.createLog(ex.getMessage(), userID);
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    // de-register in revision
    @DeleteMapping("/{revisionID}/member")
    public ResponseEntity<GenericDto<BookingDao>> unregisterMember(
            @RequestHeader(value = "Authorization") String requestKey,
            @PathVariable("revisionID") int revisionID)
    {
        String userID = isValidToken(requestKey);

        try
        {
            if (userID != null)
            {
                //token is valid, get user and role
                UserDao user = userServ.getUser(userID);

                if (user.getRole().getRoleid() == ROLE_STUDENT || user.getRole().getRoleid() == ROLE_LEADER)
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
        catch (Exception ex)
        {
            logServ.createLog(ex.getMessage(), userID);
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    // update a revision
    @PutMapping("")
    public ResponseEntity<GenericDto<BookingDao>> updateRevision(
            @RequestHeader(value = "Authorization") String requestKey,
            @RequestBody BookingDao bookingDao)
    {
        String userID = isValidToken(requestKey);

        try
        {
            if (userID != null)
            {
                //token is valid, get user and role
                UserDao user = userServ.getUser(userID);

                if (user.getRole().getRoleid() == ROLE_ADMIN || user.getRole().getRoleid() == ROLE_MANAGER)
                {
                    if (bookingServ.updateBooking(bookingDao.getBookingid(), bookingDao.getBookingStatus().getStatusid(), false, null, null) != null)
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
                    if (Objects.equals(bookingServ.getBookingDetails(bookingDao.getBookingid()).getStudent().getUserid(), userID))
                    {
                        if (bookingServ.updateBooking(bookingDao.getBookingid(), bookingDao.getBookingStatus().getStatusid(), false, null, null) != null)
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
        catch (Exception ex)
        {
            logServ.createLog(ex.getMessage(), userID);
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
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

        try
        {
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
                else if (user.getRole().getRoleid() == ROLE_LEADER)
                {
                    BookingDao retrivedRevision = bookingServ.getBookingDetails(revisionID);

                    if (retrivedRevision != null)
                    {
                        if (Objects.equals(retrivedRevision.getStudent().getUserid(), userID))
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
                        return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
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
        catch (Exception ex)
        {
            logServ.createLog(ex.getMessage(), userID);
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }
}
