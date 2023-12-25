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
    @Query(value = "select count(*) from pp_booking where slotid = :slotID and statusid = 'A' and trunc(bookingdate) = trunc(:bookingdate)", nativeQuery = true)
    int activeUnderSlot(int slotID, Date bookingdate);

    // check if student has any active bookings at this time slot
    @Transactional
    @Query(value = "select count(*) from pp_booking b join pp_slot s on b.slotid = s.slotid where b.studentid = :studentID and trunc(b.bookingdate) = trunc(:bookingDate) and b.statusid = 'A' and ( (to_char(s.starttime + INTERVAL '5' MINUTE, 'HH24:MI:SS') < to_char(:startTime, 'HH24:MI:SS') and to_char(s.endtime - INTERVAL '5' MINUTE, 'HH24:MI:SS') > to_char(:startTime, 'HH24:MI:SS')) or (to_char(s.starttime + INTERVAL '5' MINUTE, 'HH24:MI:SS') < to_char(:endTime, 'HH24:MI:SS') and to_char(s.endtime - INTERVAL '5' MINUTE, 'HH24:MI:SS') > to_char(:endTime, 'HH24:MI:SS')) or (to_char(s.starttime, 'HH24:MI:SS') >= to_char(:startTime, 'HH24:MI:SS') and to_char(s.endtime, 'HH24:MI:SS') <= to_char(:endTime, 'HH24:MI:SS')) )", nativeQuery = true)
    int sameTimeSessionsFind(String studentID, Date bookingDate, Timestamp startTime, Timestamp endTime);

    // fair booking - check if student booked with this leader this week
    @Transactional
    @Query(value = "select count(*) from pp_booking b join pp_slot s on s.slotid = b.slotid where b.studentid = :studentID and b.statusid in ('A','F') and s.leaderid = :leaderID and ( trunc(b.bookingdate) <= trunc(:weekEnd) and trunc(b.bookingdate) >= trunc(:weekStart) ) ", nativeQuery = true)
    int sameLeaderBookingFind(String studentID, String leaderID, Date weekStart, Date weekEnd);

    // check if leader has revision at the same time
    @Transactional
    @Query(value = "select count(*) from pp_booking b where b.studentid = :leaderID and trunc(b.bookingdate) = trunc(:revisionDate) and b.statusid = 'A' and b.typeid = 'R' and ( (to_char(b.starttime + INTERVAL '5' MINUTE, 'HH24:MI:SS') < to_char(:startTime, 'HH24:MI:SS') and to_char(b.endtime - INTERVAL '5' MINUTE, 'HH24:MI:SS') > to_char(:startTime, 'HH24:MI:SS')) or (to_char(b.starttime + INTERVAL '5' MINUTE, 'HH24:MI:SS') < to_char(:endTime, 'HH24:MI:SS') and to_char(b.endtime - INTERVAL '5' MINUTE, 'HH24:MI:SS') > to_char(:endTime, 'HH24:MI:SS')) or (to_char(b.starttime, 'HH24:MI:SS') >= to_char(:startTime, 'HH24:MI:SS') and to_char(b.endtime, 'HH24:MI:SS') <= to_char(:endTime, 'HH24:MI:SS')) )", nativeQuery = true)
    int sameLeaderRevisionTimeFind(String leaderID, Date revisionDate, Timestamp startTime, Timestamp endTime);

}
