package polytechnic.bh.PassPlatforms_Backend.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Cascade;
import polytechnic.bh.PassPlatforms_Backend.Dao.MajorDao;

import java.util.List;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "pp_major")
public class Major
{

    @Id
    private String majorid;
    private String majorname;
    private String majordesc;

    @ManyToOne
    @JoinColumn(name = "SCHOOLID", referencedColumnName = "SCHOOLID")
    private School school;

    // custom (multi item) entities
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE, orphanRemoval = true, mappedBy = "major")
    @Cascade(org.hibernate.annotations.CascadeType.DELETE_ORPHAN)
    private List<Course> courses;

    public Major(MajorDao majorDao)
    {
        this.majorid = majorDao.getMajorid();
        this.majorname = majorDao.getMajorname();
        this.majordesc = majorDao.getMajordesc();
        this.school = new School(majorDao.getSchool());
    }
}
