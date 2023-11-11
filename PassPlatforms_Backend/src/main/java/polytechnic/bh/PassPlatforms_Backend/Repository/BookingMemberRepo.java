package polytechnic.bh.PassPlatforms_Backend.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import polytechnic.bh.PassPlatforms_Backend.Entity.BookingMember;

public interface BookingMemberRepo extends JpaRepository<BookingMember, Integer> {
}
