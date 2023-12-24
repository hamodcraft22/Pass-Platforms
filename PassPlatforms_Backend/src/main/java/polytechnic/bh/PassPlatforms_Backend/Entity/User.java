package polytechnic.bh.PassPlatforms_Backend.Entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Cascade;
import polytechnic.bh.PassPlatforms_Backend.Dao.UserDao;

import java.util.List;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "pp_user")
public class User
{

    @Id
    private String userid;

    @ManyToOne
    @JoinColumn(name = "roleid", referencedColumnName = "roleid")
    private Role role;

    @OneToMany
    @JoinColumn(name = "USERID", referencedColumnName = "USERID")
    private List<Notification> notifications;


    // student items
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE, orphanRemoval = true, mappedBy = "student")
    @Cascade(org.hibernate.annotations.CascadeType.DELETE_ORPHAN)
    private List<Booking> bookings; // bookings a student made

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE, orphanRemoval = true, mappedBy = "student")
    @Cascade(org.hibernate.annotations.CascadeType.DELETE_ORPHAN)
    private List<BookingMember> groupbookings; // bookings a student is a part of

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE, orphanRemoval = true, mappedBy = "user")
    @Cascade(org.hibernate.annotations.CascadeType.DELETE_ORPHAN)
    private List<Schedule> schedules; // student schedules

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE, orphanRemoval = true, mappedBy = "user")
    @Cascade(org.hibernate.annotations.CascadeType.DELETE_ORPHAN)
    private Application application; // student application

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE, orphanRemoval = true, mappedBy = "student")
    @Cascade(org.hibernate.annotations.CascadeType.DELETE_ORPHAN)
    private List<Transcript> transcripts; // trans if there is an application


    // leader items
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE, orphanRemoval = true, mappedBy = "leader")
    @Cascade(org.hibernate.annotations.CascadeType.DELETE_ORPHAN)
    private List<Slot> slots;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE, orphanRemoval = true, mappedBy = "leader")
    @Cascade(org.hibernate.annotations.CascadeType.DELETE_ORPHAN)
    private List<OfferedCourse> offeredCourses;


    // tutor items
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE, orphanRemoval = true, mappedBy = "tutor")
    @Cascade(org.hibernate.annotations.CascadeType.DELETE_ORPHAN)
    private List<Recommendation> recommendations;

    public User(UserDao userDao)
    {
        this.userid = userDao.getUserid();
        this.role = new Role(userDao.getRole());
    }
}
