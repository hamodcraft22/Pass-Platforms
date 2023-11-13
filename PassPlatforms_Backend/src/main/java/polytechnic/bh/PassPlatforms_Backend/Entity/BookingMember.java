package polytechnic.bh.PassPlatforms_Backend.Entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "pp_bookingmember")
public class BookingMember {

  @Id
  private int memberid;
  private java.sql.Timestamp datetime;

  @ManyToOne
  @JoinColumn(name = "STUDENTID", referencedColumnName = "USERID")
  private User student;

  @ManyToOne
  @JoinColumn(name = "BOOKINGID", referencedColumnName = "BOOKINGID")
  private Booking booking;

  @ManyToOne
  @JoinColumn(name = "STATUSID", referencedColumnName = "STATUSID")
  private MemberStatus status;

}
