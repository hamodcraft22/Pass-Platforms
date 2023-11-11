package polytechnic.bh.PassPlatforms_Backend.Entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "audit")
public class Audit {

  @Id
  private int auditid;
  private String changetype;
  private String entityname;
  private java.sql.Date datetime;
  private String oldvalue;
  private String newvalue;

  @ManyToOne
  @JoinColumn(name = "USERID", referencedColumnName = "USERID")
  private User user;

//
//  public String getAuditid() {
//    return auditid;
//  }
//
//  public void setAuditid(String auditid) {
//    this.auditid = auditid;
//  }
//
//
//  public String getChangetype() {
//    return changetype;
//  }
//
//  public void setChangetype(String changetype) {
//    this.changetype = changetype;
//  }
//
//
//  public String getEntityname() {
//    return entityname;
//  }
//
//  public void setEntityname(String entityname) {
//    this.entityname = entityname;
//  }
//
//
//  public java.sql.Date getDatetime() {
//    return datetime;
//  }
//
//  public void setDatetime(java.sql.Date datetime) {
//    this.datetime = datetime;
//  }
//
//
//  public String getOldvalue() {
//    return oldvalue;
//  }
//
//  public void setOldvalue(String oldvalue) {
//    this.oldvalue = oldvalue;
//  }
//
//
//  public String getNewvalue() {
//    return newvalue;
//  }
//
//  public void setNewvalue(String newvalue) {
//    this.newvalue = newvalue;
//  }
//
//
//  public String getUserid() {
//    return userid;
//  }
//
//  public void setUserid(String userid) {
//    this.userid = userid;
//  }

}
