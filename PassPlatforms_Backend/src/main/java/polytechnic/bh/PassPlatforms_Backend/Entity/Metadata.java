package polytechnic.bh.PassPlatforms_Backend.Entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import polytechnic.bh.PassPlatforms_Backend.Dao.MetadataDao;

import java.sql.Date;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "pp_metadata")
public class Metadata
{
    @Id
    private int metadataid;

    private java.sql.Date mrwstart; // mid revision week start
    private java.sql.Date mrwend;   // mid revision week end
    private java.sql.Date mwstart;  // mid week start
    private java.sql.Date mwend;    // mid week end

    private java.sql.Date frwstart;
    private java.sql.Date frwend;
    private java.sql.Date fwstart;
    private java.sql.Date fwend;

    private boolean enabled;
    private boolean booking;

    public Metadata(MetadataDao metadataDao)
    {
        this.metadataid = metadataDao.getMetadataid();
        this.mrwstart = new Date(metadataDao.getMrwstart().getTime());
        this.mrwend = new Date(metadataDao.getMrwend().getTime());
        this.mwstart = new Date(metadataDao.getMwstart().getTime());
        this.mwend = new Date(metadataDao.getMwend().getTime());
        this.frwstart = new Date(metadataDao.getFrwstart().getTime());
        this.frwend = new Date(metadataDao.getFrwend().getTime());
        this.fwstart = new Date(metadataDao.getFwstart().getTime());
        this.fwend = new Date(metadataDao.getFwend().getTime());
        this.enabled = metadataDao.isEnabled();
        this.booking = metadataDao.isBooking();
    }
}
