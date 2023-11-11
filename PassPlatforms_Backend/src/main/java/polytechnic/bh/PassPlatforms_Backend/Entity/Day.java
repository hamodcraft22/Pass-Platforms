package polytechnic.bh.PassPlatforms_Backend.Entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "day")
public class Day {

  @Id
  private char dayid;
  private String dayname;

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
//  public String getDayname() {
//    return dayname;
//  }
//
//  public void setDayname(String dayname) {
//    this.dayname = dayname;
//  }

}
