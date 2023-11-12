package polytechnic.bh.PassPlatforms_Backend.Dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import polytechnic.bh.PassPlatforms_Backend.Entity.ApplicationStatus;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApplicationStatusDto {
    private char statusid;
    private String statusname;
    private String statusdesc;

    public ApplicationStatusDto(ApplicationStatus applicationStatus) {
        this.statusid = applicationStatus.getStatusid();
        this.statusname = applicationStatus.getStatusname();
        this.statusdesc = applicationStatus.getStatusdesc();
    }
}
