package polytechnic.bh.PassPlatforms_Backend.Entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import polytechnic.bh.PassPlatforms_Backend.Dao.RecStatusDao;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "pp_recstatus")
public class RecStatus {

  @Id
  private char statusid;
  private String statusname;
  private String statusdesc;

  public RecStatus(RecStatusDao recStatusDao) {
    this.statusid = recStatusDao.getStatusid();
    this.statusname = recStatusDao.getStatusname();
    this.statusdesc = recStatusDao.getStatusdesc();
  }
}
