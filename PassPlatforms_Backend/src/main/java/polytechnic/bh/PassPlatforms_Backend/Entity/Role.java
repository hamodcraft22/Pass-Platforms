package polytechnic.bh.PassPlatforms_Backend.Entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Data
@Entity
@Table(name = "role")
public class Role {

  @Id
  private int roleid;
  private String rolename;
  private String roledesc;


//
//  public String getRoleid() {
//    return roleid;
//  }
//
//  public void setRoleid(String roleid) {
//    this.roleid = roleid;
//  }
//
//
//  public String getRolename() {
//    return rolename;
//  }
//
//  public void setRolename(String rolename) {
//    this.rolename = rolename;
//  }
//
//
//  public String getRoledesc() {
//    return roledesc;
//  }
//
//  public void setRoledesc(String roledesc) {
//    this.roledesc = roledesc;
//  }

}
