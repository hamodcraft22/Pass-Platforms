package polytechnic.bh.PassPlatforms_Backend.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import polytechnic.bh.PassPlatforms_Backend.Entity.Booking;

public interface BookingRepo extends JpaRepository<Booking, Integer> {
}
