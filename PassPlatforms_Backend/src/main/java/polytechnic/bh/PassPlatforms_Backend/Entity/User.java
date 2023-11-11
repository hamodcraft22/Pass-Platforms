package polytechnic.bh.PassPlatforms_Backend.Entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "user")
public class User {

  @Id
  private String userid;
  private String roleid;

//
//  public String getUserid() {
//    return userid;
//  }
//
//  public void setUserid(String userid) {
//    this.userid = userid;
//  }
//
//
//  public String getRoleid() {
//    return roleid;
//  }
//
//  public void setRoleid(String roleid) {
//    this.roleid = roleid;
//  }

}
