package polytechnic.bh.PassPlatforms_Backend.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import polytechnic.bh.PassPlatforms_Backend.Dao.BookingNoteDao;
import polytechnic.bh.PassPlatforms_Backend.Entity.BookingMember;
import polytechnic.bh.PassPlatforms_Backend.Entity.BookingNote;
import polytechnic.bh.PassPlatforms_Backend.Entity.Notification;
import polytechnic.bh.PassPlatforms_Backend.Repository.BookingNoteRepo;
import polytechnic.bh.PassPlatforms_Backend.Repository.BookingRepo;
import polytechnic.bh.PassPlatforms_Backend.Repository.NotificationRepo;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static polytechnic.bh.PassPlatforms_Backend.Constant.BookingTypeConstant.BKNGTYP_GROUP;
import static polytechnic.bh.PassPlatforms_Backend.Constant.BookingTypeConstant.BKNGTYP_GROUP_UNSCHEDULED;

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

    public List<BookingNoteDao> getAllBookingNotes()
    {
        List<BookingNoteDao> bookingNotes = new ArrayList<>();

        for (BookingNote retrievedBookingNote : bookingNoteRepo.findAll())
        {
            bookingNotes.add(new BookingNoteDao(retrievedBookingNote));
        }

        return bookingNotes;
    }

    public List<BookingNoteDao> getBookingNotes(int bookingID)
    {
        List<BookingNoteDao> bookingNotes = new ArrayList<>();

        for (BookingNote retrievedBookingNote : bookingNoteRepo.findBookingNotesByBooking_Bookingid(bookingID))
        {
            bookingNotes.add(new BookingNoteDao(retrievedBookingNote));
        }

        return bookingNotes;
    }

    public BookingNoteDao getBookingNoteDetails(int noteID)
    {
        Optional<BookingNote> retrievedBookingNote = bookingNoteRepo.findById(noteID);

        return retrievedBookingNote.map(BookingNoteDao::new).orElse(null);
    }

    public BookingNoteDao createBookingNote(BookingNoteDao bookingNoteDao)
    {
        BookingNote newBookingNote = new BookingNote();

        newBookingNote.setDatetime(Timestamp.from(bookingNoteDao.getDatetime()));
        newBookingNote.setNotebody(bookingNoteDao.getNotebody());
        newBookingNote.setBooking(bookingRepo.getReferenceById(bookingNoteDao.getBooking().getBookingid()));
        newBookingNote.setUser(userServ.getUser(bookingNoteDao.getUser().getUserid()));

        BookingNote addedBookingNote = bookingNoteRepo.save(newBookingNote);

        // send notification to manager or user
        Notification newNotification = new Notification();
        newNotification.setEntity("Booking");
        newNotification.setItemid(String.valueOf(addedBookingNote.getBooking().getBookingid()));
        newNotification.setNotficmsg("new note added to Booking");
        newNotification.setSeen(false);
        if (Objects.equals(addedBookingNote.getUser().getUserid(), addedBookingNote.getBooking().getSlot().getLeader().getUserid()))
        {
            // send to student
            newNotification.setUser(addedBookingNote.getBooking().getStudent());
            notificationRepo.save(newNotification);

        }
        else
        {
            // send to leader
            newNotification.setUser(addedBookingNote.getBooking().getSlot().getLeader());
            notificationRepo.save(newNotification);
        }

        // if group send to all students as well
        if (addedBookingNote.getBooking().getBookingType().getTypeid() == BKNGTYP_GROUP || addedBookingNote.getBooking().getBookingType().getTypeid() == BKNGTYP_GROUP_UNSCHEDULED)
        {
            for (BookingMember bookingMember : addedBookingNote.getBooking().getBookingMembers())
            {
                if (!Objects.equals(bookingMember.getStudent().getUserid(), bookingNoteDao.getUser().getUserid()))
                {
                    newNotification.setUser(bookingMember.getStudent());
                    notificationRepo.save(newNotification);
                }
            }
        }

        return new BookingNoteDao(addedBookingNote);
    }

    public boolean deleteBookingNote(int noteID)
    {
        bookingNoteRepo.deleteById(noteID);
        return true;
    }
}

