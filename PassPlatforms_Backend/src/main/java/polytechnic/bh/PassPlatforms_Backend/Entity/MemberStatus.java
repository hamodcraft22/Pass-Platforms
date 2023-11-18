package polytechnic.bh.PassPlatforms_Backend.Entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import polytechnic.bh.PassPlatforms_Backend.Dao.MemberStatusDao;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "pp_memberstatus")
public class MemberStatus {

  @Id
  private char statusid;
  private String statusname;
  private String statusdesc;

  public MemberStatus(MemberStatusDao memberStatusDao) {
    this.statusid = memberStatusDao.getStatusid();
    this.statusname = memberStatusDao.getStatusname();
    this.statusdesc = memberStatusDao.getStatusdesc();
  }
}
