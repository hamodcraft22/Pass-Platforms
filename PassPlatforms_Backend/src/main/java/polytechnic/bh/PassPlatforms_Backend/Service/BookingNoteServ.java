package polytechnic.bh.PassPlatforms_Backend.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import polytechnic.bh.PassPlatforms_Backend.Dao.BookingNoteDao;
import polytechnic.bh.PassPlatforms_Backend.Entity.BookingNote;
import polytechnic.bh.PassPlatforms_Backend.Repository.BookingNoteRepo;
import polytechnic.bh.PassPlatforms_Backend.Repository.BookingRepo;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class BookingNoteServ
{

    @Autowired
    private BookingNoteRepo bookingNoteRepo;

    @Autowired
    private BookingRepo bookingRepo;

    @Autowired
    private UserServ userServ;

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

        return new BookingNoteDao(bookingNoteRepo.save(newBookingNote));
    }

    public boolean deleteBookingNote(int noteID)
    {
        bookingNoteRepo.deleteById(noteID);
        return true;
    }
}

