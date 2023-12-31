package polytechnic.bh.PassPlatforms_Backend.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import polytechnic.bh.PassPlatforms_Backend.Entity.BookingNote;

import java.util.List;

public interface BookingNoteRepo extends JpaRepository<BookingNote, Integer>
{
    List<BookingNote> findBookingNotesByBooking_Bookingid(int bookingID);
}
