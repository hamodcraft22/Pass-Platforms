package polytechnic.bh.PassPlatforms_Backend.Entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import polytechnic.bh.PassPlatforms_Backend.Dao.ApplicationStatusDao;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "pp_applicationstatus")
public class ApplicationStatus {

  @Id
  private char statusid;
  private String statusname;
  private String statusdesc;

  public ApplicationStatus(ApplicationStatusDao applicationStatusDao) {
    this.statusid = applicationStatusDao.getStatusid();
    this.statusname = applicationStatusDao.getStatusname();
    this.statusdesc = applicationStatusDao.getStatusdesc();
  }
}
