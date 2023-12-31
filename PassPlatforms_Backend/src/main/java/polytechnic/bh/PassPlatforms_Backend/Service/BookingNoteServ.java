package polytechnic.bh.PassPlatforms_Backend.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import polytechnic.bh.PassPlatforms_Backend.Dao.BookingNoteDao;
import polytechnic.bh.PassPlatforms_Backend.Entity.BookingMember;
import polytechnic.bh.PassPlatforms_Backend.Entity.BookingNote;
import polytechnic.bh.PassPlatforms_Backend.Entity.Notification;
import polytechnic.bh.PassPlatforms_Backend.Entity.User;
import polytechnic.bh.PassPlatforms_Backend.Repository.BookingNoteRepo;
import polytechnic.bh.PassPlatforms_Backend.Repository.BookingRepo;
import polytechnic.bh.PassPlatforms_Backend.Repository.NotificationRepo;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static polytechnic.bh.PassPlatforms_Backend.Constant.BookingTypeConstant.*;

@Service
public class BookingNoteServ
{

    @Autowired
    private BookingNoteRepo bookingNoteRepo;

    @Autowired
    private BookingRepo bookingRepo;

    @Autowired
    private UserServ userServ;

    @Autowired
    private NotificationRepo notificationRepo;

    // get all of the booking notes - not needed
    public List<BookingNoteDao> getAllBookingNotes()
    {
        List<BookingNoteDao> bookingNotes = new ArrayList<>();

        for (BookingNote retrievedBookingNote : bookingNoteRepo.findAll())
        {
            bookingNotes.add(new BookingNoteDao(retrievedBookingNote));
        }

        return bookingNotes;
    }

    // get a single booking notes
    public List<BookingNoteDao> getBookingNotes(int bookingID)
    {
        List<BookingNoteDao> bookingNotes = new ArrayList<>();

        for (BookingNote retrievedBookingNote : bookingNoteRepo.findBookingNotesByBooking_Bookingid(bookingID))
        {
            bookingNotes.add(new BookingNoteDao(retrievedBookingNote));
        }

        return bookingNotes;
    }

    // get a single booking note info - not needed
    public BookingNoteDao getBookingNoteDetails(int noteID)
    {
        Optional<BookingNote> retrievedBookingNote = bookingNoteRepo.findById(noteID);

        return retrievedBookingNote.map(BookingNoteDao::new).orElse(null);
    }

    // create a new booking by a user
    public BookingNoteDao createBookingNote(int bookingID, String bookingNote, String userID)
    {
        BookingNote newBookingNote = new BookingNote();

        newBookingNote.setDatetime(Timestamp.from(Instant.now()));
        newBookingNote.setNotebody(bookingNote);
        newBookingNote.setBooking(bookingRepo.getReferenceById(bookingID));
        newBookingNote.setUser(new User(userServ.getUser(userID)));

        BookingNote addedBookingNote = bookingNoteRepo.save(newBookingNote);

        // send notification to manager or user
        Notification newNotification = new Notification();
        newNotification.setEntity("Booking");
        newNotification.setItemid(String.valueOf(addedBookingNote.getBooking().getBookingid()));
        newNotification.setNotficmsg("new note added to Booking");
        newNotification.setSeen(false);

        if ((addedBookingNote.getBooking().getSlot() != null && Objects.equals(addedBookingNote.getUser().getUserid(), addedBookingNote.getBooking().getSlot().getLeader().getUserid())) || (addedBookingNote.getBooking().getBookingType().getTypeid() == BKNGTYP_REVISION && Objects.equals(addedBookingNote.getUser().getUserid(), addedBookingNote.getBooking().getStudent().getUserid())))
        {
            // send to student
            newNotification.setUser(addedBookingNote.getBooking().getStudent());
            notificationRepo.save(newNotification);

        }
        else
        {
            // send to leader
            if (addedBookingNote.getBooking().getBookingType().getTypeid() == BKNGTYP_REVISION)
            {
                newNotification.setUser(addedBookingNote.getBooking().getStudent());
                notificationRepo.save(newNotification);
            }
            else
            {
                newNotification.setUser(addedBookingNote.getBooking().getSlot().getLeader());
                notificationRepo.save(newNotification);
            }
        }

        // if group send to all students as well
        if (addedBookingNote.getBooking().getBookingType().getTypeid() == BKNGTYP_GROUP || addedBookingNote.getBooking().getBookingType().getTypeid() == BKNGTYP_GROUP_UNSCHEDULED)
        {
            for (BookingMember bookingMember : addedBookingNote.getBooking().getBookingMembers())
            {
                if (!Objects.equals(bookingMember.getStudent().getUserid(), userID))
                {
                    newNotification.setUser(bookingMember.getStudent());
                    notificationRepo.save(newNotification);
                }
            }
        }

        return new BookingNoteDao(addedBookingNote);
    }

    // delete a booking by id - not allowed for now
    public boolean deleteBookingNote(int noteID)
    {
        bookingNoteRepo.deleteById(noteID);
        return true;
    }
}

