package polytechnic.bh.PassPlatforms_Backend.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import polytechnic.bh.PassPlatforms_Backend.Entity.*;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

public interface BookingRepo extends JpaRepository<Booking, Integer>
{
    // find all bookings (or revisions) within a school
    List<Booking> findBookingsByCourse_School_SchoolidAndBookingType_Typeid(String SchoolID, char typeID);

    // check if slot is used by another active booking
    boolean existsBySlot_SlotidAndBookingdateAndBookingStatus_Statusid(int slotID, Date date, char statusID);


    // check if student has session at the same time
//    boolean existsByStudent_UseridAndBookingdateAndBookingStatus_StatusidAndIsrevisionAndSlot_StarttimeBetween(String studentID, Date bookingDate, char statusID, boolean isRevision, Timestamp startTime, Timestamp startTime2);
//
//    boolean existsByStudent_UseridAndBookingdateAndBookingStatus_StatusidAndIsrevisionAndSlot_EndtimeBetween(String studentID, Date bookingDate, char statusID, boolean isRevision, Timestamp endTime, Timestamp endTime2);

    // check if student has session at the same time - check if or (for start and end time) TODO
    boolean existsByStudent_UseridAndBookingdateAndBookingStatus_StatusidAndBookingType_TypeidAndSlot_StarttimeBetweenOrSlot_EndtimeBetween(String studentID, Date bookingDate, char statusID, char typeID, Timestamp startTime, Timestamp startTime2, Timestamp endTime, Timestamp endTime2);


    // check if leader has revision at the same time
//    boolean existsByStudent_UseridAndBookingdateAndBookingStatus_StatusidAndIsrevisionAndStarttimeBetween(String leaderID, Date revisionDate, char statusID, boolean isRevision, Timestamp startTime, Timestamp startTime1);
//
//    boolean existsByStudent_UseridAndBookingdateAndBookingStatus_StatusidAndIsrevisionAndEndtimeBetween(String leaderID, Date revisionDate, char statusID, boolean isRevision, Timestamp endTime, Timestamp endTime1);

    // check if leader has revision at the same time - check if or (for start and end time) TODO
    boolean existsByStudent_UseridAndBookingdateAndBookingStatus_StatusidAndBookingType_TypeidAndStarttimeBetweenOrEndtimeBetween(String leaderID, Date revisionDate, char statusID, char typeID, Timestamp startTime, Timestamp startTime1, Timestamp endTime, Timestamp endTime1);
}
