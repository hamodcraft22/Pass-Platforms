package polytechnic.bh.PassPlatforms_Backend.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import polytechnic.bh.PassPlatforms_Backend.Entity.BookingType;

public interface BookingTypeRepo extends JpaRepository<BookingType, Character>
{

}
