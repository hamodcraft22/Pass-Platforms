package polytechnic.bh.PassPlatforms_Backend.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import polytechnic.bh.PassPlatforms_Backend.Dao.BookingDao;
import polytechnic.bh.PassPlatforms_Backend.Dao.BookingMemberDao;
import polytechnic.bh.PassPlatforms_Backend.Dao.UserDao;
import polytechnic.bh.PassPlatforms_Backend.Dto.GenericDto;
import polytechnic.bh.PassPlatforms_Backend.Service.*;

import java.sql.Timestamp;
import java.util.List;
import java.util.Objects;

import static polytechnic.bh.PassPlatforms_Backend.Constant.RoleConstant.*;
import static polytechnic.bh.PassPlatforms_Backend.Util.TokenValidation.isValidToken;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/booking")
public class BookingCont
{
    @Autowired
    private BookingServ bookingServ;

    @Autowired
    private BookingMemberServ bookingMemberServ;

    @Autowired
    private UserServ userServ;

    @Autowired
    private LogServ logServ;

    @Autowired
    private AuditServ auditServ;

    // get all bookings - only managers
    @GetMapping("")
    public ResponseEntity<GenericDto<List<BookingDao>>> getAllBookings(
            @RequestHeader(value = "Authorization") String requestKey)
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
                    List<BookingDao> bookings = bookingServ.getAllBookings();

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

    // get all bookings - per school
    @GetMapping("/school/{schoolID}")
    public ResponseEntity<GenericDto<List<BookingDao>>> getSchoolBookings(
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
                    List<BookingDao> bookings = bookingServ.getSchoolSessions(schoolID);

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

    // get all student bookings -- added | tested
    @GetMapping("/student/{studentID}")
    public ResponseEntity<GenericDto<List<BookingDao>>> getStudentBookings(
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

                if (user.getRole().getRoleid() == ROLE_STUDENT || user.getRole().getRoleid() == ROLE_LEADER || user.getRole().getRoleid() == ROLE_ADMIN || user.getRole().getRoleid() == ROLE_MANAGER)
                {
                    // check if the id is same as request id
                    if (userID.equals(studentID) || user.getRole().getRoleid() == ROLE_ADMIN || user.getRole().getRoleid() == ROLE_MANAGER)
                    {
                        List<BookingDao> bookings = bookingServ.getStudentBookings(studentID);

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

    // get all member bookings -- tested | added
    @GetMapping("/member/{studentID}")
    public ResponseEntity<GenericDto<List<BookingDao>>> getMemberBookings(
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

                if (user.getRole().getRoleid() == ROLE_STUDENT || user.getRole().getRoleid() == ROLE_LEADER || user.getRole().getRoleid() == ROLE_ADMIN || user.getRole().getRoleid() == ROLE_MANAGER)
                {
                    // check if the id is same as request id
                    if (userID.equals(studentID) || user.getRole().getRoleid() == ROLE_ADMIN || user.getRole().getRoleid() == ROLE_MANAGER)
                    {
                        List<BookingDao> bookings = bookingServ.getMemberBookings(studentID);

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

    // get all leader bookings -- added | tested
    @GetMapping("/leader/{leaderID}")
    public ResponseEntity<GenericDto<List<BookingDao>>> getLeaderBookings(
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

                if (user.getRole().getRoleid() == ROLE_LEADER || user.getRole().getRoleid() == ROLE_ADMIN || user.getRole().getRoleid() == ROLE_MANAGER)
                {
                    // check if the id is same as request id
                    if (userID.equals(leaderID) || user.getRole().getRoleid() == ROLE_ADMIN || user.getRole().getRoleid() == ROLE_MANAGER)
                    {
                        List<BookingDao> bookings = bookingServ.getLeaderBookings(leaderID);

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

    // get booking details --
    @GetMapping("/{bookingID}")
    public ResponseEntity<GenericDto<BookingDao>> getBookingDetails(
            @RequestHeader(value = "Authorization") String requestKey,
            @PathVariable("bookingID") int bookingID)
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
                    BookingDao booking = bookingServ.getBookingDetails(bookingID);

                    if (booking != null)
                    {
                        return new ResponseEntity<>(new GenericDto<>(null, booking, null, null), HttpStatus.OK);
                    }
                    else
                    {
                        return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
                    }
                }
                else if (user.getRole().getRoleid() == ROLE_STUDENT)
                {
                    // check if student booked, or is a member of the booking
                    BookingDao retrivedBooking = bookingServ.getBookingDetails(bookingID);

                    if (retrivedBooking != null)
                    {
                        if (Objects.equals(retrivedBooking.getStudent().getUserid(), userID))
                        {
                            return new ResponseEntity<>(new GenericDto<>(null, retrivedBooking, null, null), HttpStatus.OK);
                        }
                        else
                        {
                            // loop to see if member of
                            boolean hasAcss = false;

                            for (BookingMemberDao bookingMember : retrivedBooking.getBookingMembers())
                            {
                                if (Objects.equals(bookingMember.getStudent().getUserid(), userID))
                                {
                                    hasAcss = true;
                                    break;
                                }
                            }

                            if (hasAcss)
                            {
                                return new ResponseEntity<>(new GenericDto<>(null, retrivedBooking, null, null), HttpStatus.OK);
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
                else if (user.getRole().getRoleid() == ROLE_LEADER)
                {
                    BookingDao retrivedBooking = bookingServ.getBookingDetails(bookingID);

                    if (retrivedBooking != null)
                    {
                        // check if leader owns the booking
                        if (Objects.equals(retrivedBooking.getSlot().getLeader().getUserid(), userID))
                        {
                            return new ResponseEntity<>(new GenericDto<>(null, retrivedBooking, null, null), HttpStatus.OK);
                        }
                        // check if the leader is a student of the booking
                        else if (Objects.equals(retrivedBooking.getStudent().getUserid(), userID))
                        {
                            return new ResponseEntity<>(new GenericDto<>(null, retrivedBooking, null, null), HttpStatus.OK);
                        }
                        else
                        {
                            // check if the leader is a member of this booking

                            // loop to see if member of
                            boolean hasAcss = false;

                            for (BookingMemberDao bookingMember : retrivedBooking.getBookingMembers())
                            {
                                if (Objects.equals(bookingMember.getStudent().getUserid(), userID))
                                {
                                    hasAcss = true;
                                    break;
                                }
                            }

                            if (hasAcss)
                            {
                                return new ResponseEntity<>(new GenericDto<>(null, retrivedBooking, null, null), HttpStatus.OK);
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

    // post a normal booking (as a student) -- tested | added
    @PostMapping("")
    public ResponseEntity<GenericDto<BookingDao>> createNewBooking(
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

                if (user.getRole().getRoleid() == ROLE_STUDENT || user.getRole().getRoleid() == ROLE_LEADER)
                {
                    GenericDto<BookingDao> newBooking = bookingServ.createNewBooking(bookingDao.getBookingDate(), bookingDao.getNote(), bookingDao.isIsonline(), bookingDao.getSlot().getSlotid(), userID, bookingDao.getCourse().getCourseid(), bookingDao.getBookingMembers(), false, null, null, null);

                    if (newBooking != null)
                    {
                        if (newBooking.getTransObject() != null && newBooking.getError() == null)
                        {
                            return new ResponseEntity<>(new GenericDto<>(null, newBooking.getTransObject(), null, newBooking.getWarnings()), HttpStatus.OK);
                        }
                        else
                        {
                            return new ResponseEntity<>(new GenericDto<>(null, null, newBooking.getError(), null), HttpStatus.BAD_REQUEST);
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

    // post an unscheduled booking (as a leader for a student)
    @PostMapping("/unscheduled")
    public ResponseEntity<GenericDto<BookingDao>> createUnscheduledBooking(
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

                if (user.getRole().getRoleid() == ROLE_STUDENT || user.getRole().getRoleid() == ROLE_LEADER)
                {
                    GenericDto<BookingDao> newBooking = bookingServ.createNewBooking(bookingDao.getBookingDate(), bookingDao.getNote(), bookingDao.isIsonline(), bookingDao.getSlot().getSlotid(), bookingDao.getStudent().getUserid(), bookingDao.getCourse().getCourseid(), bookingDao.getBookingMembers(), true, Timestamp.from(bookingDao.getStarttime()), Timestamp.from(bookingDao.getEndtime()), "LEADERID");

                    if (newBooking != null)
                    {
                        if (newBooking.getTransObject() != null && newBooking.getError() == null)
                        {
                            return new ResponseEntity<>(new GenericDto<>(null, newBooking.getTransObject(), null, newBooking.getWarnings()), HttpStatus.OK);
                        }
                        else
                        {
                            return new ResponseEntity<>(new GenericDto<>(null, null, newBooking.getError(), null), HttpStatus.BAD_REQUEST);
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

    // add booking member
    @PostMapping("/{bookingID}/member")
    public ResponseEntity<GenericDto<BookingMemberDao>> addNewMember(
            @RequestHeader(value = "Authorization") String requestKey,
            @PathVariable("bookingID") int bookingID,
            @RequestAttribute(value = "studentID") String studentID)
    {
        String userID = isValidToken(requestKey);

        try
        {
            if (userID != null)
            {
                //token is valid, get user and role
                UserDao user = userServ.getUser(userID);

                if (user.getRole().getRoleid() == ROLE_STUDENT)
                {
                    // if the user is the one who booked the session
                    if (Objects.equals(bookingServ.getBookingDetails(bookingID).getStudent().getUserid(), userID))
                    {
                        GenericDto<BookingMemberDao> newMember = bookingMemberServ.addStudentMember(bookingID, studentID);

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

    // remove booking member
    @DeleteMapping("/{bookingID}/member")
    public ResponseEntity<GenericDto<BookingDao>> removeMember(
            @RequestHeader(value = "Authorization") String requestKey,
            @PathVariable("bookingID") int bookingID,
            @RequestAttribute(value = "studentID") String studentID)
    {
        String userID = isValidToken(requestKey);

        try
        {
            if (userID != null)
            {
                //token is valid, get user and role
                UserDao user = userServ.getUser(userID);

                if (user.getRole().getRoleid() == ROLE_STUDENT)
                {
                    // if the user is the one who booked the session
                    if (Objects.equals(bookingServ.getBookingDetails(bookingID).getStudent().getUserid(), userID))
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

    // update booking - only status / times for leader -- added | tested
    @PutMapping("")
    public ResponseEntity<GenericDto<BookingDao>> updateBooking(
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
                    if (bookingServ.updateBooking(bookingDao.getBookingid(), bookingDao.getBookingStatus().getStatusid(), false, (bookingDao.getStarttime() == null ? null : Timestamp.from(bookingDao.getStarttime())), (bookingDao.getEndtime() == null ? null : Timestamp.from(bookingDao.getEndtime()))) != null)
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

                    if (Objects.equals(bookingServ.getBookingDetails(bookingDao.getBookingid()).getSlot().getLeader().getUserid(), userID))
                    {
                        if (bookingServ.updateBooking(bookingDao.getBookingid(), bookingDao.getBookingStatus().getStatusid(), false, (bookingDao.getStarttime() == null ? null : Timestamp.from(bookingDao.getStarttime())), (bookingDao.getEndtime() == null ? null : Timestamp.from(bookingDao.getEndtime()))) != null)
                        {
                            return new ResponseEntity<>(null, HttpStatus.OK);
                        }
                        else
                        {
                            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
                        }
                    }
                    else if (Objects.equals(bookingServ.getBookingDetails(bookingDao.getBookingid()).getStudent().getUserid(), userID))
                    {
                        // did the leader make this booking?
                        if (bookingServ.updateBooking(bookingDao.getBookingid(), bookingDao.getBookingStatus().getStatusid(), true, null, null) != null)
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
                else if (user.getRole().getRoleid() == ROLE_STUDENT)
                {
                    // is the student the one who booked the session?

                    if (Objects.equals(bookingServ.getBookingDetails(bookingDao.getBookingid()).getStudent().getUserid(), userID))
                    {
                        if (bookingServ.updateBooking(bookingDao.getBookingid(), bookingDao.getBookingStatus().getStatusid(), true, null, null) != null)
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


    // actually delete booking - managers only -- added |
    @DeleteMapping("/{bookingID}")
    public ResponseEntity<GenericDto<BookingDao>> deleteBooking(
            @RequestHeader(value = "Authorization") String requestKey,
            @PathVariable("bookingID") int bookingID
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
                    if (bookingServ.deleteBooking(bookingID))
                    {
                        // delete audit
                        auditServ.createAudit('D',"Booking",null,null,userID);
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
}
