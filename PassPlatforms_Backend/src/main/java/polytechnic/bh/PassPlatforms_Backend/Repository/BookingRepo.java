package polytechnic.bh.PassPlatforms_Backend.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import polytechnic.bh.PassPlatforms_Backend.Entity.*;

import java.util.List;

public interface BookingRepo extends JpaRepository<Booking, Integer> {
    List<Booking> findBookingsByDatebooked(Object date);
    List<Booking> findBookingsByBookingdate(Object date);
    List<Booking> findBookingsByNoteContainsIgnoreCase(String note);
    List<Booking> findBookingsByStarttime(Object date);
    List<Booking> findBookingsByEndtime(Object date);
    List<Booking> findBookingsByBookinglimit(int limit);
    List<Booking> findBookingsByIsonline(boolean isonline);
    List<Booking> findBookingsByIsgroup(boolean isgroup);
    List<Booking> findBookingsByIsrevision(boolean isrevision);
    List<Booking> findBookingsBySlot(Slot slot);
    List<Booking> findBookingsByBookingStatus(BookingStatus status);
    List<Booking> findBookingsByStudent(User student);
    List<Booking> findBookingsByCourse(Course course);

}
