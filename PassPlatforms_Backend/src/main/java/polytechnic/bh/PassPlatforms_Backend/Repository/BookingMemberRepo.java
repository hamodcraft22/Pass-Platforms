package polytechnic.bh.PassPlatforms_Backend.Repository;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import polytechnic.bh.PassPlatforms_Backend.Entity.BookingMember;

import java.sql.Timestamp;
import java.util.Date;

public interface BookingMemberRepo extends JpaRepository<BookingMember, Integer>
{
    // check if student owns this booking
    boolean existsByStudent_UseridAndBooking_Bookingid(String studentID, int bookingID);

    // check if student has a revision within the same course (active or has passed)
    @Transactional
    @Query(value = "select count(*) from pp_bookingmember m join pp_booking b on m.bookingid = b.bookingid where m.studentid = :studentID and b.courseid = :courseID and b.statusid in ('A','F') and b.typeid = 'R'", nativeQuery = true)
    int sameCourseRevisionFind(String studentID, String courseID);

    // check if user is a part of any active booking / revision at a given time allocation
    @Transactional
    @Query(value = " SELECT count(*) FROM pp_bookingmember m JOIN pp_booking b on b.bookingid = m.bookingid join pp_slot s on b.slotid = s.slotid where m.studentid = :studentID and trunc(b.bookingdate) = trunc(:bookingDate) and b.statusid = 'A' and ( (to_char(s.starttime + INTERVAL '5' MINUTE, 'HH24:MI:SS') < to_char(:startTime, 'HH24:MI:SS') and to_char(s.endtime - INTERVAL '5' MINUTE, 'HH24:MI:SS') > to_char(:startTime, 'HH24:MI:SS')) or (to_char(s.starttime + INTERVAL '5' MINUTE, 'HH24:MI:SS') < to_char(:endTime, 'HH24:MI:SS') and to_char(s.endtime - INTERVAL '5' MINUTE, 'HH24:MI:SS') > to_char(:endTime, 'HH24:MI:SS')) or (to_char(s.starttime, 'HH24:MI:SS') >= to_char(:startTime, 'HH24:MI:SS') and to_char(s.endtime, 'HH24:MI:SS') <= to_char(:endTime, 'HH24:MI:SS')) ) ", nativeQuery = true)
    int sameTimeMemberSessionsFind(String studentID, Date bookingDate, Timestamp startTime, Timestamp endTime);

    // delete by student user id and booking id
    @Transactional
    void deleteByStudent_UseridAndBooking_Bookingid(String studentID, int bookingID);
}
