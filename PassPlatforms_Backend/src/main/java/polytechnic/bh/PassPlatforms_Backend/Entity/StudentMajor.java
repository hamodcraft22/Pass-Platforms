package polytechnic.bh.PassPlatforms_Backend.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import polytechnic.bh.PassPlatforms_Backend.Dao.StudentMajorDao;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "pp_studentmajor")
public class StudentMajor
{

    @Id
    private int stumajorid;
    private boolean isminor;

    @ManyToOne
    @JoinColumn(name = "USERID", referencedColumnName = "USERID")
    private User user;

    @ManyToOne
    @JoinColumn(name = "MAJORID", referencedColumnName = "MAJORID")
    private Major major;

    public StudentMajor(StudentMajorDao studentMajorDao)
    {
        this.stumajorid = studentMajorDao.getStumajorid();
        this.isminor = studentMajorDao.isIsminor();
        this.user = new User(studentMajorDao.getUser());
        this.major = new Major(studentMajorDao.getMajor());
    }
}
