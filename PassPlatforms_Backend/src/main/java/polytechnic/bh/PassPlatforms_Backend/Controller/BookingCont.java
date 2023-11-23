package polytechnic.bh.PassPlatforms_Backend.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import polytechnic.bh.PassPlatforms_Backend.Dao.BookingDao;
import polytechnic.bh.PassPlatforms_Backend.Dao.BookingMemberDao;
import polytechnic.bh.PassPlatforms_Backend.Dto.GenericDto;
import polytechnic.bh.PassPlatforms_Backend.Repository.UserRepo;
import polytechnic.bh.PassPlatforms_Backend.Service.BookingServ;

import java.util.List;
import java.util.Objects;

import static polytechnic.bh.PassPlatforms_Backend.Constant.APIkeyConstant.*;

@RestController
@RequestMapping("/api")
public class BookingCont
{
    @Autowired
    private BookingServ bookingServ;

    @Autowired
    private UserRepo userRepo;

    // get all bookings - only managers
    @GetMapping("/booking")
    public ResponseEntity<GenericDto<List<BookingDao>>> getAllBookings(
            @RequestHeader(value = "Authorization", required = false) String requestKey)
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

    // get booking details
    @GetMapping("/booking/{bookingID}")
    public ResponseEntity<GenericDto<BookingDao>> getBookingDetails(
            @RequestHeader(value = "Authorization", required = false) String requestKey,
            @RequestHeader(value = "Requester", required = false) String requisterID,
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

    // get all revisions - per school
    @GetMapping("/revision/{schoolID}")
    public ResponseEntity<GenericDto<List<BookingDao>>> getSchoolRevisions(
            @RequestHeader(value = "Authorization", required = false) String requestKey,
            @PathVariable("schoolID") String schoolID)
    {
        if (Objects.equals(requestKey, ADMIN_KEY) || Objects.equals(requestKey, MANAGER_KEY) || Objects.equals(requestKey, STUDENT_KEY) || Objects.equals(requestKey, LEADER_KEY))
        {
            List<BookingDao> bookings = bookingServ.getSchoolRevisions(schoolID);

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

    // post a booking


    // post a revision

    // update booking - only status

    // register in revision

    // delete booking - only managers
}
