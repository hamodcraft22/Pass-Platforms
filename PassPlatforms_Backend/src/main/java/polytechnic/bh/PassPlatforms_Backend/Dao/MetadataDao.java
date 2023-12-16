package polytechnic.bh.PassPlatforms_Backend.Dao;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import polytechnic.bh.PassPlatforms_Backend.Entity.Metadata;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MetadataDao
{
    private int metadataid;

    private Date mrwstart; // mid revision week start
    private Date mrwend;   // mid revision week end
    private Date mwstart;  // mid week start
    private Date mwend;    // mid week end

    private Date frwstart;
    private Date frwend;
    private Date fwstart;
    private Date fwend;

    private boolean enabled;
    private boolean booking;

    public MetadataDao(Metadata metadata)
    {
        this.metadataid = metadata.getMetadataid();
        this.mrwstart = metadata.getMrwstart();
        this.mrwend = metadata.getMrwend();
        this.mwstart = metadata.getMwstart();
        this.mwend = metadata.getMwend();
        this.frwstart = metadata.getFrwstart();
        this.frwend = metadata.getFrwend();
        this.fwstart = metadata.getFwstart();
        this.fwend = metadata.getFwend();
        this.enabled = metadata.isEnabled();
        this.booking = metadata.isBooking();
    }
}
