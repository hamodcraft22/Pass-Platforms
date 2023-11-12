package polytechnic.bh.PassPlatforms_Backend.Entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Data
@Entity
@Table(name = "pp_booking")
public class Booking {

  @Id
  private int bookingid;
  private java.sql.Date datebooked;
  private java.sql.Date date;
  private String note;
  private java.sql.Timestamp starttime;
  private java.sql.Timestamp endtime;
  private String bookinglimit;
  private String isonline;
  private String isgroup;
  private String isrevision;

  @ManyToOne
  @JoinColumn(name = "SLOTID", referencedColumnName = "SLOTID")
  private Slot slot;

  @ManyToOne
  @JoinColumn(name = "STATUSID", referencedColumnName = "STATUSID")
  private BookingStatus bookingStatus;

  @ManyToOne
  @JoinColumn(name = "STUDENTID", referencedColumnName = "USERID")
  private User student;

  @ManyToOne
  @JoinColumn(name = "COURSEID", referencedColumnName = "COURSEID")
  private Course course;

  // custom (multi item) entities
  @JsonManagedReference
  @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
  @JoinColumn(name = "BOOKINGID", referencedColumnName = "BOOKINGID")
  private List<BookingMember> bookingMembers;

  @JsonManagedReference
  @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
  @JoinColumn(name = "BOOKINGID", referencedColumnName = "BOOKINGID")
  private List<BookingNote> bookingNotes;

}
