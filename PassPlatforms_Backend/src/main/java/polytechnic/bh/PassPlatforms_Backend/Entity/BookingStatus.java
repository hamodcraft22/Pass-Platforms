package polytechnic.bh.PassPlatforms_Backend.Entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "pp_bookingstatus")
public class BookingStatus {

  @Id
  private char statusid;
  private String statusname;
  private String statusdesc;

}
