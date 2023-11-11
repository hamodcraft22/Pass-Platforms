package polytechnic.bh.PassPlatforms_Backend.Entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "pp_user")
public class User {

  @Id
  private String userid;

  @ManyToOne
  @JoinColumn(name = "roleid", referencedColumnName = "roleid")
  private Role role;


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
