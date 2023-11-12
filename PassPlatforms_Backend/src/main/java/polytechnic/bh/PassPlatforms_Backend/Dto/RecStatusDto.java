package polytechnic.bh.PassPlatforms_Backend.Dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import polytechnic.bh.PassPlatforms_Backend.Entity.RecStatus;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RecStatusDto {
    private char statusid;
    private String statusname;
    private String statusdesc;

    public RecStatusDto(RecStatus status) {
        this.statusid = status.getStatusid();
        this.statusname = status.getStatusname();
        this.statusdesc = status.getStatusdesc();
    }
}
