package polytechnic.bh.PassPlatforms_Backend.Repository;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import polytechnic.bh.PassPlatforms_Backend.Entity.Booking;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

public interface BookingRepo extends JpaRepository<Booking, Integer>
{
    // find all bookings (or revisions) within a school
    List<Booking> findBookingsByCourse_School_SchoolidAndBookingType_Typeid(String SchoolID, char typeID);

//    // check if slot is used by another active booking
//    boolean existsBySlot_SlotidAndBookingdateAndBookingStatus_Statusid(int slotID, Date date, char statusID);

    // get a course booking within a range
    @Transactional
    @Query(value = "SELECT * FROM pp_booking " +
            "WHERE typeid = 'R' " +
            "AND statusid = 'A' " +
            "AND courseid = :courseID " +
            "AND CAST(bookingdate AS date) <= CAST(:endDate AS date)" +
            "AND CAST(bookingdate AS date) >= CAST(:startDate AS date)", nativeQuery = true)
    List<Booking> findAllCourseRevisions(String courseID, Date startDate, Date endDate);

    @Transactional
    @Query(value = "select * from pp_booking where STUDENTID = :studentID and TYPEID not in ('R')", nativeQuery = true)
    List<Booking> findAllStudentBookings(String studentID);

    @Transactional
    @Query(value = "select * from pp_booking WHERE typeid IN ('G', 'Z') AND bookingid in (select bookingid from pp_bookingmember WHERE studentid = :studentID)", nativeQuery = true)
    List<Booking> findAllMemberBookings(String studentID);

    @Transactional
    @Query(value = "select * from pp_booking WHERE typeid IN ('N', 'U', 'G', 'Z') and slotid IN (select slotid from pp_slot WHERE leaderid = :leaderID)", nativeQuery = true)
    List<Booking> findAllLeaderBookings(String leaderID);

    @Transactional
    @Query(value = "select * from pp_booking WHERE typeid = 'R' and bookingid IN (select bookingid from pp_bookingmember where studentid = :studentID)", nativeQuery = true)
    List<Booking> findAllStudentRevisions(String studentID);

    @Transactional
    @Query(value = "select * from pp_booking WHERE typeid = 'R' and studentid = :leaderID", nativeQuery = true)
    List<Booking> findAllLeaderRevisions(String leaderID);

    @Transactional
    @Query(value = "SELECT count(*) FROM pp_booking " +
            "WHERE slotid = :slotID " +
            "AND statusid = 'A' " +
            "AND CAST(bookingdate AS date) = CAST(:bookingdate AS date)", nativeQuery = true)
    int activeUnderSlot(int slotID, Date bookingdate);

    // check if student has any active bookings at this time slot
    @Transactional
    @Query(value = "SELECT count(*) FROM pp_booking b " +
            "JOIN pp_slot s ON b.slotid = s.slotid " +
            "WHERE b.studentid = :studentID " +
            "AND CAST(b.bookingdate AS date) = CAST(:bookingDate AS date) " +
            "AND b.statusid = 'A' " +
            "AND ( " +
            "  (CAST(s.starttime AS time) + INTERVAL '5' minute < CAST(:startTime AS time) " +
            "   AND CAST(s.endtime AS time) - INTERVAL '5' minute > CAST(:startTime AS time)) " +
            "  OR " +
            "  (CAST(s.starttime AS time) + INTERVAL '5' minute < CAST(:endTime AS time) " +
            "   AND CAST(s.endtime AS time) - INTERVAL '5' minute > CAST(:endTime AS time)) " +
            "  OR " +
            "  (CAST(s.starttime AS time) >= CAST(:startTime AS time) AND CAST(s.endtime AS time) <= CAST(:endTime AS time)) " +
            ")", nativeQuery = true)
    int sameTimeSessionsFind(String studentID, Date bookingDate, Timestamp startTime, Timestamp endTime);

    // fair booking - check if student booked with this leader this week
    @Transactional
    @Query(value = "SELECT count(*) FROM pp_booking b " +
            "JOIN pp_slot s ON s.slotid = b.slotid " +
            "WHERE b.studentid = :studentID " +
            "AND b.statusid IN ('A', 'F') " +
            "AND s.leaderid = :leaderID " +
            "AND (CAST(b.bookingdate AS date) <= CAST(:weekEnd AS date) " +
            "AND CAST(b.bookingdate AS date) >= CAST(:weekStart AS date))", nativeQuery = true)
    int sameLeaderBookingFind(String studentID, String leaderID, Date weekStart, Date weekEnd);

    // check if leader has revision at the same time
    @Transactional
    @Query(value = "SELECT count(*) FROM pp_booking b " +
            "WHERE b.studentid = :leaderID " +
            "AND CAST(b.bookingdate AS date) = CAST(:revisionDate AS date) " +
            "AND b.statusid = 'A' " +
            "AND b.typeid = 'R' " +
            "AND ( " +
            "  (CAST(b.starttime AS time) + INTERVAL '5' minute < CAST(:startTime AS time) " +
            "   AND CAST(b.endtime AS time) - INTERVAL '5' minute > CAST(:startTime AS time)) " +
            "  OR " +
            "  (CAST(b.starttime AS time) + INTERVAL '5' minute < CAST(:endTime AS time) " +
            "   AND CAST(b.endtime AS time) - INTERVAL '5' minute > CAST(:endTime AS time)) " +
            "  OR " +
            "  (CAST(b.starttime AS time) >= CAST(:startTime AS time) AND CAST(b.endtime AS time) <= CAST(:endTime AS time)) " +
            ")", nativeQuery = true)
    int sameLeaderRevisionTimeFind(String leaderID, Date revisionDate, Timestamp startTime, Timestamp endTime);

}
