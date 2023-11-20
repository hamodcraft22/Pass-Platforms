package polytechnic.bh.PassPlatforms_Backend.Dao;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import polytechnic.bh.PassPlatforms_Backend.Entity.MemberStatus;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MemberStatusDao
{
    private char statusid;
    private String statusname;
    private String statusdesc;

    public MemberStatusDao(MemberStatus memberStatus)
    {
        this.statusid = memberStatus.getStatusid();
        this.statusname = memberStatus.getStatusname();
        this.statusdesc = memberStatus.getStatusdesc();
    }
}
