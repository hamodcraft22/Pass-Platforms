package polytechnic.bh.PassPlatforms_Backend.Entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import polytechnic.bh.PassPlatforms_Backend.Dao.SchoolDao;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "pp_school")
public class School {

  @Id
  private String schoolid;
  private String schoolname;
  private String schooldesc;

  public School(SchoolDao schoolDao) {
    this.schoolid = schoolDao.getSchoolid();
    this.schoolname = schoolDao.getSchoolname();
    this.schooldesc = schoolDao.getSchooldesc();
  }
}
