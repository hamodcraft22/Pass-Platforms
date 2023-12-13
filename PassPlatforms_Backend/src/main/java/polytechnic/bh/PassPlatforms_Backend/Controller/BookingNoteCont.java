package polytechnic.bh.PassPlatforms_Backend.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import polytechnic.bh.PassPlatforms_Backend.Dao.BookingDao;
import polytechnic.bh.PassPlatforms_Backend.Dao.BookingMemberDao;
import polytechnic.bh.PassPlatforms_Backend.Dao.BookingNoteDao;
import polytechnic.bh.PassPlatforms_Backend.Dto.GenericDto;
import polytechnic.bh.PassPlatforms_Backend.Service.BookingNoteServ;
import polytechnic.bh.PassPlatforms_Backend.Service.BookingServ;

import java.util.List;
import java.util.Objects;

import static polytechnic.bh.PassPlatforms_Backend.Constant.APIkeyConstant.*;

@RestController
@RequestMapping("/api/bookingnote")
public class BookingNoteCont
{

    @Autowired
    private BookingNoteServ bookingNoteServ;

    @Autowired
    private BookingServ bookingServ;


    // get all booking notes
    @GetMapping("")
    public ResponseEntity<GenericDto<List<BookingNoteDao>>> getAllBookingNotes(
            @RequestHeader(value = "Authorization") String requestKey)
    {
        if (Objects.equals(requestKey, ADMIN_KEY) || Objects.equals(requestKey, MANAGER_KEY))
        {
            List<BookingNoteDao> bookingNotes = bookingNoteServ.getAllBookingNotes();

            if (bookingNotes != null && !bookingNotes.isEmpty())
            {
                return new ResponseEntity<>(new GenericDto<>(null, bookingNotes, null), HttpStatus.OK);
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

    // get all booking notes for a booking
    @GetMapping("/booking/{bookingID}")
    public ResponseEntity<GenericDto<List<BookingNoteDao>>> getBookingNotes(
            @RequestHeader(value = "Authorization") String requestKey,
            @RequestHeader(value = "Requester") String requisterID,
            @PathVariable("bookingID") int bookingID)
    {
        if (Objects.equals(requestKey, ADMIN_KEY) || Objects.equals(requestKey, MANAGER_KEY))
        {
            List<BookingNoteDao> bookingNotes = bookingNoteServ.getBookingNotes(bookingID);

            if (bookingNotes != null && !bookingNotes.isEmpty())
            {
                return new ResponseEntity<>(new GenericDto<>(null, bookingNotes, null), HttpStatus.OK);
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
                    List<BookingNoteDao> bookingNotes = bookingNoteServ.getBookingNotes(bookingID);
                    return new ResponseEntity<>(new GenericDto<>(null, bookingNotes, null), HttpStatus.OK);
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
                        List<BookingNoteDao> bookingNotes = bookingNoteServ.getBookingNotes(bookingID);
                        return new ResponseEntity<>(new GenericDto<>(null, bookingNotes, null), HttpStatus.OK);
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
                    List<BookingNoteDao> bookingNotes = bookingNoteServ.getBookingNotes(bookingID);
                    return new ResponseEntity<>(new GenericDto<>(null, bookingNotes, null), HttpStatus.OK);
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

    // get booking note details
    @GetMapping("/{noteID}")
    public ResponseEntity<GenericDto<BookingNoteDao>> getBookingNoteDetails(
            @RequestHeader(value = "Authorization") String requestKey,
            @RequestHeader(value = "Requester") String requisterID,
            @PathVariable("noteID") int noteID)
    {
        if (Objects.equals(requestKey, MANAGER_KEY) || Objects.equals(requestKey, ADMIN_KEY))
        {
            BookingNoteDao bookingNote = bookingNoteServ.getBookingNoteDetails(noteID);

            if (bookingNote != null)
            {
                return new ResponseEntity<>(new GenericDto<>(null, bookingNote, null), HttpStatus.OK);
            }
            else
            {
                return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
            }
        }
        else if (Objects.equals(requestKey, STUDENT_KEY) || Objects.equals(requestKey, LEADER_KEY))
        {
            BookingNoteDao bookingNote = bookingNoteServ.getBookingNoteDetails(noteID);

            if (bookingNote != null)
            {
                if (Objects.equals(bookingNote.getUser().getUserid(), requisterID))
                {
                    return new ResponseEntity<>(new GenericDto<>(null, bookingNote, null), HttpStatus.OK);
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

    // create booking note
    @PostMapping("")
    public ResponseEntity<GenericDto<BookingNoteDao>> createBookingNote(
            @RequestHeader(value = "Authorization") String requestKey,
            @RequestHeader(value = "Requester") String requisterID,
            @RequestBody BookingNoteDao bookingNoteDao)
    {
        if (Objects.equals(requestKey, ADMIN_KEY) || Objects.equals(requestKey, MANAGER_KEY))
        {
            BookingDao booking = bookingServ.getBookingDetails(bookingNoteDao.getBooking().getBookingid());

            if (booking != null)
            {
                BookingNoteDao createdBookingNote = bookingNoteServ.createBookingNote(bookingNoteDao);

                return new ResponseEntity<>(new GenericDto<>(null, createdBookingNote, null), HttpStatus.CREATED);
            }
            else
            {
                return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
            }
        }
        else if (Objects.equals(requestKey, STUDENT_KEY))
        {
            // check if student booked, or is a member of the booking
            BookingDao retrivedBooking = bookingServ.getBookingDetails(bookingNoteDao.getBooking().getBookingid());

            if (retrivedBooking != null)
            {
                if (Objects.equals(retrivedBooking.getStudent().getUserid(), requisterID))
                {
                    BookingNoteDao createdBookingNote = bookingNoteServ.createBookingNote(bookingNoteDao);

                    return new ResponseEntity<>(new GenericDto<>(null, createdBookingNote, null), HttpStatus.CREATED);
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
                        BookingNoteDao createdBookingNote = bookingNoteServ.createBookingNote(bookingNoteDao);

                        return new ResponseEntity<>(new GenericDto<>(null, createdBookingNote, null), HttpStatus.CREATED);
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
            BookingDao retrivedBooking = bookingServ.getBookingDetails(bookingNoteDao.getBooking().getBookingid());

            if (retrivedBooking != null)
            {
                if (Objects.equals(retrivedBooking.getSlot().getLeader().getUserid(), requisterID))
                {
                    BookingNoteDao createdBookingNote = bookingNoteServ.createBookingNote(bookingNoteDao);

                    return new ResponseEntity<>(new GenericDto<>(null, createdBookingNote, null), HttpStatus.CREATED);
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

    // delete booking note
    @DeleteMapping("/{noteID}")
    public ResponseEntity<GenericDto<Void>> deleteBookingNote(
            @RequestHeader(value = "Authorization") String requestKey,
            @RequestHeader(value = "Requester") String requisterID,
            @PathVariable("noteID") int noteID)
    {
        if (Objects.equals(requestKey, ADMIN_KEY) || Objects.equals(requestKey, MANAGER_KEY))
        {
            if (bookingNoteServ.deleteBookingNote(noteID))
            {
                return new ResponseEntity<>(null, HttpStatus.OK);
            }
            else
            {
                return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
            }
        }
        else if (Objects.equals(requestKey, STUDENT_KEY) || Objects.equals(requestKey, LEADER_KEY))
        {
            BookingNoteDao noteDao = bookingNoteServ.getBookingNoteDetails(noteID);

            if (Objects.equals(noteDao.getUser().getUserid(), requisterID))
            {
                if (bookingNoteServ.deleteBookingNote(noteID))
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
