package polytechnic.bh.PassPlatforms_Backend.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import polytechnic.bh.PassPlatforms_Backend.Dao.NotificationDao;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "pp_notification")
public class Notification
{
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "pp_notfic_SEQ")
    @SequenceGenerator(name = "pp_notfic_SEQ", sequenceName = "pp_notfic_SEQ", allocationSize = 1)
    private int notficid;
    private String entity;
    private String itemid;
    private String notficmsg;
    private boolean seen;

    @ManyToOne
    @JoinColumn(name = "USERID", referencedColumnName = "USERID")
    private User user;

    public Notification(NotificationDao notificationDao)
    {
        this.notficid = notificationDao.getNotficid();
        this.entity = notificationDao.getEntity();
        this.itemid = notificationDao.getItemid();
        this.notficmsg = notificationDao.getNotficmsg();
        this.seen = notificationDao.isSeen();
        this.user = new User(notificationDao.getUser());
    }
}
