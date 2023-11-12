package polytechnic.bh.PassPlatforms_Backend.Entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "pp_bookingnote")
public class BookingNote {

  @Id
  private int noteid;
  private java.sql.Date datetime;
  private String notebody;

  @JsonBackReference
  @ManyToOne
  @JoinColumn(name = "BOOKINGID", referencedColumnName = "BOOKINGID")
  private Booking booking;

  @ManyToOne
  @JoinColumn(name = "USERID", referencedColumnName = "USERID")
  private User user;

}
