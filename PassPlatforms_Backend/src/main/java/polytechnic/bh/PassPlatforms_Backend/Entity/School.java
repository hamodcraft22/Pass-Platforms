package polytechnic.bh.PassPlatforms_Backend.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Cascade;
import polytechnic.bh.PassPlatforms_Backend.Dao.SchoolDao;

import java.util.List;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "pp_school")
public class School
{

    @Id
    private String schoolid;
    private String schoolname;
    private String schooldesc;

    // custom (multi item) entities
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE, orphanRemoval = true, mappedBy = "school")
    @Cascade(org.hibernate.annotations.CascadeType.DELETE_ORPHAN)
    private List<Major> majors;

    public School(SchoolDao schoolDao)
    {
        this.schoolid = schoolDao.getSchoolid();
        this.schoolname = schoolDao.getSchoolname();
        this.schooldesc = schoolDao.getSchooldesc();
    }
}
