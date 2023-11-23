package polytechnic.bh.PassPlatforms_Backend.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import polytechnic.bh.PassPlatforms_Backend.Entity.*;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

public interface BookingRepo extends JpaRepository<Booking, Integer>
{
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

    List<Booking> findBookingsByCourse_School_Schoolid(String SchoolID);

    boolean existsBySlot_SlotidAndBookingdateAndBookingStatus_Statusid(int slotID, Date date, char statusID);

    boolean existsByStudent_UseridAndSlot_Starttime(String studentID, Timestamp startTime);

    boolean existsByStudent_UseridAndStarttimeBetweenAndBookingdate(String leaderID, Timestamp startTime, Timestamp endTime, Date revisionDate);

    boolean existsByStudent_UseridAndEndtimeBetweenAndBookingdate(String leaderID, Timestamp startTime, Timestamp endTime, Date revisionDate);

    boolean existsByBookingMembersContainsAndStarttimeBetween(List<BookingMember> bookingMembers, Timestamp startTime, Timestamp endTime);

    boolean existsByBookingMembersContainsAndEndtimeBetween(List<BookingMember> bookingMembers, Timestamp startTime, Timestamp endTime);
}
