package polytechnic.bh.PassPlatforms_Backend.Entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "schedule")
public class Schedule {

  @Id
  private int scheduleid;
  private java.sql.Timestamp starttime;
  private java.sql.Timestamp endtime;

  @ManyToOne
  @JoinColumn(name = "DAYID", referencedColumnName = "DAYID")
  private Day day;

  @ManyToOne
  @JoinColumn(name = "USERID", referencedColumnName = "USERID")
  private User user;

//
//  public String getScheduleid() {
//    return scheduleid;
//  }
//
//  public void setScheduleid(String scheduleid) {
//    this.scheduleid = scheduleid;
//  }
//
//
//  public java.sql.Timestamp getStarttime() {
//    return starttime;
//  }
//
//  public void setStarttime(java.sql.Timestamp starttime) {
//    this.starttime = starttime;
//  }
//
//
//  public java.sql.Timestamp getEndtime() {
//    return endtime;
//  }
//
//  public void setEndtime(java.sql.Timestamp endtime) {
//    this.endtime = endtime;
//  }
//
//
//  public String getDayid() {
//    return dayid;
//  }
//
//  public void setDayid(String dayid) {
//    this.dayid = dayid;
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
