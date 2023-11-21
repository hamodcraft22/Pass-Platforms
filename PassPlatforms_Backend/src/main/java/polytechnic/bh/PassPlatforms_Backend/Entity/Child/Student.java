package polytechnic.bh.PassPlatforms_Backend.Entity.Child;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import polytechnic.bh.PassPlatforms_Backend.Entity.*;

import java.util.List;

@Data
@Entity
@DiscriminatorValue("1")
@EqualsAndHashCode(callSuper = true)
public class Student extends User
{
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "STUDENTID", referencedColumnName = "USERID")
    private List<Booking> bookings;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "STUDENTID", referencedColumnName = "USERID")
    private List<BookingMember> groupbookings;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "USERID", referencedColumnName = "USERID")
    private List<BookingNote> bookingNotes;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "USERID", referencedColumnName = "USERID")
    private List<Schedule> schedules;
}
