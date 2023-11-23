package polytechnic.bh.PassPlatforms_Backend.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import polytechnic.bh.PassPlatforms_Backend.Entity.BookingMember;

import java.util.Date;
import java.util.List;

public interface BookingMemberRepo extends JpaRepository<BookingMember, Integer>
{
    List<BookingMember> findBookingMembersByDatetime(Date dateTime);

    List<BookingMember> findBookingMembersByStudent_UseridAndBooking_BookingStatus_StatusidAndBooking_Bookingdate(String studentID, char statusID, Date revisionDate);

    List<BookingMember> findBookingMembersByStudent_UseridAndBooking_Course_CourseidAndBooking_BookingStatus_StatusidAndBooking_Isrevision(String studentID, String courseID, char statusID, boolean isRevision);
}
