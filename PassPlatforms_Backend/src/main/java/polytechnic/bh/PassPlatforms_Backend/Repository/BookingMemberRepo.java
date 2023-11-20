package polytechnic.bh.PassPlatforms_Backend.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import polytechnic.bh.PassPlatforms_Backend.Entity.BookingMember;

import java.util.Date;
import java.util.List;

public interface BookingMemberRepo extends JpaRepository<BookingMember, Integer>
{
    List<BookingMember> findBookingMembersByDatetime(Date dateTime);
}
