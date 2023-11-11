package polytechnic.bh.PassPlatforms_Backend.Entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "log")
public class Log {

  @Id
  private int logid;
  private String errormsg;
  private java.sql.Date datetime;

  @ManyToOne
  @JoinColumn(name = "USERID", referencedColumnName = "USERID")
  private User user;

//
//  public String getLogid() {
//    return logid;
//  }
//
//  public void setLogid(String logid) {
//    this.logid = logid;
//  }
//
//
//  public String getErrormsg() {
//    return errormsg;
//  }
//
//  public void setErrormsg(String errormsg) {
//    this.errormsg = errormsg;
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
//  public String getUserid() {
//    return userid;
//  }
//
//  public void setUserid(String userid) {
//    this.userid = userid;
//  }

}
