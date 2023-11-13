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
  private java.sql.Timestamp datebooked;
  private java.sql.Date bookingdate;
  private String note;
  private java.sql.Timestamp starttime;
  private java.sql.Timestamp endtime;
  private int bookinglimit;
  private boolean isonline;
  private boolean isgroup;
  private boolean isrevision;

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
