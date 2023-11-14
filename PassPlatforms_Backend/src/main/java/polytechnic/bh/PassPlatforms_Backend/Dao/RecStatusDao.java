package polytechnic.bh.PassPlatforms_Backend.Dao;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import polytechnic.bh.PassPlatforms_Backend.Entity.RecStatus;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RecStatusDao {
    private char statusid;
    private String statusname;
    private String statusdesc;

    public RecStatusDao(RecStatus status) {
        this.statusid = status.getStatusid();
        this.statusname = status.getStatusname();
        this.statusdesc = status.getStatusdesc();
    }
}
