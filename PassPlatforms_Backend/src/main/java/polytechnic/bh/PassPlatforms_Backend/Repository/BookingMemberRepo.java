package polytechnic.bh.PassPlatforms_Backend.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import polytechnic.bh.PassPlatforms_Backend.Entity.BookingMember;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

public interface BookingMemberRepo extends JpaRepository<BookingMember, Integer>
{
    List<BookingMember> findBookingMembersByDatetime(Date dateTime);

//    // check if the student is a member of another session at the same time
//    boolean existsByStudent_UseridAndBooking_BookingdateAndBooking_BookingStatus_StatusidAndBooking_IsgroupAndBooking_Slot_StarttimeBetween(String studentID, Date bookingDate, char statusID, boolean isGroup, Timestamp startTime, Timestamp startTime2);
//    boolean existsByStudent_UseridAndBooking_BookingdateAndBooking_BookingStatus_StatusidAndBooking_IsgroupAndBooking_Slot_EndtimeBetween(String studentID, Date bookingDate, char statusID, boolean isGroup, Timestamp endTime, Timestamp endTime2);
//
//    // check if the student is a member of another session at the same time - check if or (for start and end time) TODO
//    boolean existsByStudent_UseridAndBooking_BookingdateAndBooking_BookingStatus_StatusidAndBooking_IsgroupAndBooking_Slot_StarttimeBetweenOrBooking_Slot_EndtimeBetween(String studentID, Date bookingDate, char statusID, boolean isGroup, Timestamp startTime, Timestamp startTime2, Timestamp endTime, Timestamp endTime2);
//
//
//
//    // check if student is a member is a part of a revision at the same time
//    boolean existsByStudent_UseridAndBooking_BookingdateAndBooking_BookingStatus_StatusidAndBooking_IsgroupAndBookingStarttimeBetween(String studentID, Date bookingDate, char statusID, boolean isGroup, Timestamp startTime, Timestamp startTime2);
//    boolean existsByStudent_UseridAndBooking_BookingdateAndBooking_BookingStatus_StatusidAndBooking_IsgroupAndBookingEndtimeBetween(String studentID, Date bookingDate, char statusID, boolean isGroup, Timestamp endTime, Timestamp endTime2);
//
//    // check if the student is a member of another revision at the same time - check if or (for start and end time) TODO
//    boolean existsByStudent_UseridAndBooking_BookingdateAndBooking_BookingStatus_StatusidAndBooking_IsgroupAndBookingStarttimeBetweenOrBookingEndtimeBetween(String studentID, Date bookingDate, char statusID, boolean isGroup, Timestamp startTime, Timestamp startTime2, Timestamp endTime, Timestamp endTime2);


    // check if student is a part of this booking
    boolean existsByStudent_UseridAndBooking_Bookingid(String studentID, int bookingID);

    // check if student has a revision within the same course (active or has passed)
    boolean existsByStudent_UseridAndBooking_Course_CourseidAndBooking_BookingStatus_StatusidAndBooking_BookingType_Typeid(String studentID, String courseID, char statusID, char typeID);


    // check if user has a booking - they are a part off at this time
//    boolean existsByStudent_UseridAndBooking_BookingdateAndBooking_BookingStatus_StatusidAndBooking_IsgroupAndBooking_IsrevisionAndBooking_Slot_StarttimeBetween(String studentID, Date bookingDate, char statusID, boolean isGroup, boolean isRevision, Timestamp startTime, Timestamp startTime2);
//
//    boolean existsByStudent_UseridAndBooking_BookingdateAndBooking_BookingStatus_StatusidAndBooking_IsgroupAndBooking_IsrevisionAndBooking_Slot_EndtimeBetween(String studentID, Date bookingDate, char statusID, boolean isGroup, boolean isRevision, Timestamp endTime, Timestamp endTime2);

    // check if user has a booking - they are a part off - check if or (for start and end time) TODO
    boolean existsByStudent_UseridAndBooking_BookingdateAndBooking_BookingStatus_StatusidAndBooking_BookingType_TypeidAndBooking_Slot_StarttimeBetweenOrBooking_Slot_EndtimeBetween(String studentID, Date bookingDate, char statusID, char typeID, Timestamp startTime, Timestamp startTime2, Timestamp endTime, Timestamp endTime2);


    // check if user has a revision - they are a part off at this time
//    boolean existsByStudent_UseridAndBooking_BookingdateAndBooking_IsrevisionAndBooking_StarttimeBetween(String studentID, Date bookingDate, boolean isRevision, Timestamp startTime, Timestamp startTime2);
//
//    boolean existsByStudent_UseridAndBooking_BookingdateAndBooking_IsrevisionAndBooking_EndtimeBetween(String studentID, Date bookingDate, boolean isRevision, Timestamp endTime, Timestamp endTime2);

    // check if user has a revision - they are a part off - check if or (for start and end time) TODO
    boolean existsByStudent_UseridAndBooking_BookingdateAndBooking_BookingType_TypeidAndBooking_StarttimeBetweenOrBooking_EndtimeBetween(String studentID, Date bookingDate, char typeID, Timestamp startTime, Timestamp startTime2, Timestamp endTime, Timestamp endTime2);


    // delete by student user id and booking id
    void deleteByStudent_UseridAndBooking_Bookingid(String studentID, int bookingID);
}
