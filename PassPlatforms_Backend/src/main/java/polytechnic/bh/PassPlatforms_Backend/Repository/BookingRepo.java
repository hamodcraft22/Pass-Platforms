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

    // find all bookings (or revisions) within a school
    List<Booking> findBookingsByCourse_School_SchoolidAndIsrevision(String SchoolID, boolean isRevision);

    // check if slot is used by another active booking
    boolean existsBySlot_SlotidAndBookingdateAndBookingStatus_Statusid(int slotID, Date date, char statusID);


    // check if student has session at the same time
    boolean existsByStudent_UseridAndBookingdateAndBookingStatus_StatusidAndIsrevisionAndSlot_StarttimeBetween(String studentID, Date bookingDate, char statusID, boolean isRevision, Timestamp startTime, Timestamp startTime2);
    boolean existsByStudent_UseridAndBookingdateAndBookingStatus_StatusidAndIsrevisionAndSlot_EndtimeBetween(String studentID, Date bookingDate, char statusID, boolean isRevision, Timestamp endTime, Timestamp endTime2);

    // check if student has session at the same time - check if or (for start and end time) TODO
    boolean existsByStudent_UseridAndBookingdateAndBookingStatus_StatusidAndIsrevisionAndSlot_StarttimeBetweenOrSlot_EndtimeBetween(String studentID, Date bookingDate, char statusID, boolean isRevision, Timestamp startTime, Timestamp startTime2, Timestamp endTime, Timestamp endTime2);



    // check if leader has revision at the same time
    boolean existsByStudent_UseridAndBookingdateAndBookingStatus_StatusidAndIsrevisionAndStarttimeBetween(String leaderID, Date revisionDate, char statusID, boolean isRevision, Timestamp startTime, Timestamp startTime1);
    boolean existsByStudent_UseridAndBookingdateAndBookingStatus_StatusidAndIsrevisionAndEndtimeBetween(String leaderID, Date revisionDate, char statusID, boolean isRevision, Timestamp endTime, Timestamp endTime1);

    // check if leader has revision at the same time - check if or (for start and end time) TODO
    boolean existsByStudent_UseridAndBookingdateAndBookingStatus_StatusidAndIsrevisionAndStarttimeBetweenOrEndtimeBetween(String leaderID, Date revisionDate, char statusID, boolean isRevision, Timestamp startTime, Timestamp startTime1, Timestamp endTime, Timestamp endTime1);
}
