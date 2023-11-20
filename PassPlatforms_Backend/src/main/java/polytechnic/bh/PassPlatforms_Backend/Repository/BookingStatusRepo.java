package polytechnic.bh.PassPlatforms_Backend.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import polytechnic.bh.PassPlatforms_Backend.Entity.BookingStatus;

public interface BookingStatusRepo extends JpaRepository<BookingStatus, Character>
{
}
