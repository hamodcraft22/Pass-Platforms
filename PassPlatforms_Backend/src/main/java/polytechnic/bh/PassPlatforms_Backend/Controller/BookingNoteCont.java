package polytechnic.bh.PassPlatforms_Backend.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import polytechnic.bh.PassPlatforms_Backend.Dao.BookingDao;
import polytechnic.bh.PassPlatforms_Backend.Dao.BookingMemberDao;
import polytechnic.bh.PassPlatforms_Backend.Dao.BookingNoteDao;
import polytechnic.bh.PassPlatforms_Backend.Dao.UserDao;
import polytechnic.bh.PassPlatforms_Backend.Dto.GenericDto;
import polytechnic.bh.PassPlatforms_Backend.Service.BookingNoteServ;
import polytechnic.bh.PassPlatforms_Backend.Service.BookingServ;
import polytechnic.bh.PassPlatforms_Backend.Service.UserServ;

import java.util.List;
import java.util.Objects;

import static polytechnic.bh.PassPlatforms_Backend.Constant.RoleConstant.*;
import static polytechnic.bh.PassPlatforms_Backend.Util.TokenValidation.isValidToken;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/bookingnote")
public class BookingNoteCont
{

    @Autowired
    private BookingNoteServ bookingNoteServ;

    @Autowired
    private BookingServ bookingServ;

    @Autowired
    private UserServ userServ;

    // get all booking notes
    @GetMapping("")
    public ResponseEntity<GenericDto<List<BookingNoteDao>>> getAllBookingNotes(
            @RequestHeader(value = "Authorization") String requestKey)
    {
        String userID = isValidToken(requestKey);

        if (userID != null)
        {
            //token is valid, get user and role
            UserDao user = userServ.getUser(userID);

            if (user.getRole().getRoleid() == ROLE_ADMIN || user.getRole().getRoleid() == ROLE_MANAGER)
            {
                List<BookingNoteDao> bookingNotes = bookingNoteServ.getAllBookingNotes();

                if (bookingNotes != null && !bookingNotes.isEmpty())
                {
                    return new ResponseEntity<>(new GenericDto<>(null, bookingNotes, null, null), HttpStatus.OK);
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

    // get all booking notes for a booking
    @GetMapping("/booking/{bookingID}")
    public ResponseEntity<GenericDto<List<BookingNoteDao>>> getBookingNotes(
            @RequestHeader(value = "Authorization") String requestKey,
            @PathVariable("bookingID") int bookingID)
    {
        String userID = isValidToken(requestKey);

        if (userID != null)
        {
            //token is valid, get user and role
            UserDao user = userServ.getUser(userID);

            if (user.getRole().getRoleid() == ROLE_ADMIN || user.getRole().getRoleid() == ROLE_MANAGER)
            {
                List<BookingNoteDao> bookingNotes = bookingNoteServ.getBookingNotes(bookingID);

                if (bookingNotes != null && !bookingNotes.isEmpty())
                {
                    return new ResponseEntity<>(new GenericDto<>(null, bookingNotes, null, null), HttpStatus.OK);
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
                        List<BookingNoteDao> bookingNotes = bookingNoteServ.getBookingNotes(bookingID);
                        return new ResponseEntity<>(new GenericDto<>(null, bookingNotes, null, null), HttpStatus.OK);
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
                            List<BookingNoteDao> bookingNotes = bookingNoteServ.getBookingNotes(bookingID);
                            return new ResponseEntity<>(new GenericDto<>(null, bookingNotes, null, null), HttpStatus.OK);
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
                    if (retrivedBooking.getSlot() != null && Objects.equals(retrivedBooking.getSlot().getLeader().getUserid(), userID))
                    {
                        List<BookingNoteDao> bookingNotes = bookingNoteServ.getBookingNotes(bookingID);
                        return new ResponseEntity<>(new GenericDto<>(null, bookingNotes, null, null), HttpStatus.OK);
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
        else
        {
            return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
        }

    }

    // get booking note details
    @GetMapping("/{noteID}")
    public ResponseEntity<GenericDto<BookingNoteDao>> getBookingNoteDetails(
            @RequestHeader(value = "Authorization") String requestKey,
            @PathVariable("noteID") int noteID)
    {
        String userID = isValidToken(requestKey);

        if (userID != null)
        {
            //token is valid, get user and role
            UserDao user = userServ.getUser(userID);

            if (user.getRole().getRoleid() == ROLE_MANAGER || user.getRole().getRoleid() == ROLE_ADMIN)
            {
                BookingNoteDao bookingNote = bookingNoteServ.getBookingNoteDetails(noteID);

                if (bookingNote != null)
                {
                    return new ResponseEntity<>(new GenericDto<>(null, bookingNote, null, null), HttpStatus.OK);
                }
                else
                {
                    return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
                }
            }
            else if (user.getRole().getRoleid() == ROLE_STUDENT || user.getRole().getRoleid() == ROLE_LEADER)
            {
                BookingNoteDao bookingNote = bookingNoteServ.getBookingNoteDetails(noteID);

                if (bookingNote != null)
                {
                    if (Objects.equals(bookingNote.getUser().getUserid(), userID))
                    {
                        return new ResponseEntity<>(new GenericDto<>(null, bookingNote, null, null), HttpStatus.OK);
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
        else
        {
            return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
        }


    }

    // create booking note
    @PostMapping("/{bookingID}")
    public ResponseEntity<GenericDto<BookingNoteDao>> createBookingNote(
            @RequestHeader(value = "Authorization") String requestKey,
            @PathVariable(value = "bookingID") int bookingID,
            @RequestBody String bookingNote)
    {
        String userID = isValidToken(requestKey);

        if (userID != null)
        {
            //token is valid, get user and role
            UserDao user = userServ.getUser(userID);

            if (user.getRole().getRoleid() == ROLE_ADMIN || user.getRole().getRoleid() == ROLE_MANAGER)
            {
                BookingDao booking = bookingServ.getBookingDetails(bookingID);

                if (booking != null)
                {
                    BookingNoteDao createdBookingNote = bookingNoteServ.createBookingNote(bookingID, bookingNote, userID);

                    return new ResponseEntity<>(new GenericDto<>(null, createdBookingNote, null, null), HttpStatus.CREATED);
                }
                else
                {
                    return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
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
                        BookingNoteDao createdBookingNote = bookingNoteServ.createBookingNote(bookingID, bookingNote, userID);

                        return new ResponseEntity<>(new GenericDto<>(null, createdBookingNote, null, null), HttpStatus.CREATED);
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
                            BookingNoteDao createdBookingNote = bookingNoteServ.createBookingNote(bookingID, bookingNote, userID);

                            return new ResponseEntity<>(new GenericDto<>(null, createdBookingNote, null, null), HttpStatus.CREATED);
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
                    if (retrivedBooking.getSlot() != null && Objects.equals(retrivedBooking.getSlot().getLeader().getUserid(), userID))
                    {
                        BookingNoteDao createdBookingNote = bookingNoteServ.createBookingNote(bookingID, bookingNote, userID);

                        return new ResponseEntity<>(new GenericDto<>(null, createdBookingNote, null, null), HttpStatus.CREATED);
                    }
                    else if (Objects.equals(retrivedBooking.getStudent().getUserid(), userID))
                    {
                        BookingNoteDao createdBookingNote = bookingNoteServ.createBookingNote(bookingID, bookingNote, userID);

                        return new ResponseEntity<>(new GenericDto<>(null, createdBookingNote, null, null), HttpStatus.CREATED);
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
                            BookingNoteDao createdBookingNote = bookingNoteServ.createBookingNote(bookingID, bookingNote, userID);

                            return new ResponseEntity<>(new GenericDto<>(null, createdBookingNote, null, null), HttpStatus.CREATED);
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

    // delete booking note
    @DeleteMapping("/{noteID}")
    public ResponseEntity<GenericDto<Void>> deleteBookingNote(
            @RequestHeader(value = "Authorization") String requestKey,
            @PathVariable("noteID") int noteID)
    {
        String userID = isValidToken(requestKey);

        if (userID != null)
        {
            //token is valid, get user and role
            UserDao user = userServ.getUser(userID);

            if (user.getRole().getRoleid() == ROLE_ADMIN || user.getRole().getRoleid() == ROLE_MANAGER)
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
            else if (user.getRole().getRoleid() == ROLE_STUDENT || user.getRole().getRoleid() == ROLE_LEADER)
            {
                BookingNoteDao noteDao = bookingNoteServ.getBookingNoteDetails(noteID);

                if (Objects.equals(noteDao.getUser().getUserid(), userID))
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
        else
        {
            return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
        }


    }
}
