package polytechnic.bh.PassPlatforms_Backend.Entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "slot")
public class Slot {

  @Id
  private String slotid;
  private java.sql.Timestamp starttime;
  private java.sql.Timestamp endtime;
  private String note;
  private String isrevision;
  private String isonline;
  private String dayid;
  private String leaderid;

//
//  public String getSlotid() {
//    return slotid;
//  }
//
//  public void setSlotid(String slotid) {
//    this.slotid = slotid;
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
//  public String getNote() {
//    return note;
//  }
//
//  public void setNote(String note) {
//    this.note = note;
//  }
//
//
//  public String getIsrevision() {
//    return isrevision;
//  }
//
//  public void setIsrevision(String isrevision) {
//    this.isrevision = isrevision;
//  }
//
//
//  public String getIsonline() {
//    return isonline;
//  }
//
//  public void setIsonline(String isonline) {
//    this.isonline = isonline;
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
//  public String getLeaderid() {
//    return leaderid;
//  }
//
//  public void setLeaderid(String leaderid) {
//    this.leaderid = leaderid;
//  }

}
