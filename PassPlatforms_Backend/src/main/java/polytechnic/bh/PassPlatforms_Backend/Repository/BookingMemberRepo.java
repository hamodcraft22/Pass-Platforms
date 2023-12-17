package polytechnic.bh.PassPlatforms_Backend.Repository;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import polytechnic.bh.PassPlatforms_Backend.Entity.BookingMember;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

public interface BookingMemberRepo extends JpaRepository<BookingMember, Integer>
{
    // check if student owns this booking
    boolean existsByStudent_UseridAndBooking_Bookingid(String studentID, int bookingID);

    // check if student has a revision within the same course (active or has passed)
    boolean existsByStudent_UseridAndBooking_Course_CourseidAndBooking_BookingStatus_StatusidAndBooking_BookingType_Typeid(String studentID, String courseID, char statusID, char typeID);

    // check if user is a part of any active booking at a given time allocation
    @Transactional
    @Query(value = " SELECT count(*) FROM pp_bookingmember m JOIN pp_booking b on b.bookingid = m.bookingid join pp_slot s on b.slotid = s.slotid where m.studentid = :studentID and trunc(b.bookingdate) = trunc(:bookingDate) and b.statusid = 'A' and ( (to_char(s.starttime + INTERVAL '5' MINUTE, 'HH24:MI:SS') < to_char(:startTime, 'HH24:MI:SS') and to_char(s.endtime - INTERVAL '5' MINUTE, 'HH24:MI:SS') > to_char(:startTime, 'HH24:MI:SS')) or (to_char(s.starttime + INTERVAL '5' MINUTE, 'HH24:MI:SS') < to_char(:endTime, 'HH24:MI:SS') and to_char(s.endtime - INTERVAL '5' MINUTE, 'HH24:MI:SS') > to_char(:endTime, 'HH24:MI:SS')) or (to_char(s.starttime, 'HH24:MI:SS') >= to_char(:startTime, 'HH24:MI:SS') and to_char(s.endtime, 'HH24:MI:SS') <= to_char(:endTime, 'HH24:MI:SS')) ) ", nativeQuery = true)
    int sameTimeMemberSessionsFind(String studentID, Date bookingDate, Timestamp startTime, Timestamp endTime);

    // check if user has a revision - they are a part off - check if or (for start and end time) TODO
    boolean existsByStudent_UseridAndBooking_BookingdateAndBooking_BookingType_TypeidAndBooking_StarttimeBetweenOrBooking_EndtimeBetween(String studentID, Date bookingDate, char typeID, Timestamp startTime, Timestamp startTime2, Timestamp endTime, Timestamp endTime2);

    // delete by student user id and booking id
    void deleteByStudent_UseridAndBooking_Bookingid(String studentID, int bookingID);
}
