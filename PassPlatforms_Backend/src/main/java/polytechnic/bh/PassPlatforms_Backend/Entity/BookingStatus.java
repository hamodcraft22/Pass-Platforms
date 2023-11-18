package polytechnic.bh.PassPlatforms_Backend.Entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import polytechnic.bh.PassPlatforms_Backend.Dao.BookingStatusDao;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "pp_bookingstatus")
public class BookingStatus {

  @Id
  private char statusid;
  private String statusname;
  private String statusdesc;

  public BookingStatus(BookingStatusDao bookingStatusDao) {
    this.statusid = bookingStatusDao.getStatusid();
    this.statusname = bookingStatusDao.getStatusname();
    this.statusdesc = bookingStatusDao.getStatusdesc();
  }
}
