package polytechnic.bh.PassPlatforms_Backend.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import polytechnic.bh.PassPlatforms_Backend.Dao.MetadataDao;
import polytechnic.bh.PassPlatforms_Backend.Entity.Metadata;
import polytechnic.bh.PassPlatforms_Backend.Repository.MetadataRepo;

import java.sql.Date;
import java.util.Optional;

@Service
public class MetadataServ
{

    @Autowired
    private MetadataRepo metadataRepo;

    // Get metadata
    public MetadataDao getMetadata()
    {
        Optional<Metadata> retrievedMetadata = metadataRepo.findById(1);
        return retrievedMetadata.map(MetadataDao::new).orElse(null);
    }

    // Update metadata
    public MetadataDao updateMetadata(MetadataDao metadataDao)
    {
        Optional<Metadata> existingMetadata = metadataRepo.findById(1);

        if (existingMetadata.isPresent())
        {
            Metadata updatedMetadata = existingMetadata.get();
            updatedMetadata.setMrwstart(new Date(metadataDao.getMrwstart().getTime()));
            updatedMetadata.setMrwend(new Date(metadataDao.getMrwend().getTime()));
            updatedMetadata.setMwstart(new Date(metadataDao.getMwstart().getTime()));
            updatedMetadata.setMwend(new Date(metadataDao.getMwend().getTime()));
            updatedMetadata.setFrwstart(new Date(metadataDao.getFrwstart().getTime()));
            updatedMetadata.setFrwend(new Date(metadataDao.getFrwend().getTime()));
            updatedMetadata.setFwstart(new Date(metadataDao.getFwstart().getTime()));
            updatedMetadata.setFwend(new Date(metadataDao.getFwend().getTime()));
            updatedMetadata.setEnabled(metadataDao.isEnabled());
            updatedMetadata.setBooking(metadataDao.isBooking());

            return new MetadataDao(metadataRepo.save(updatedMetadata));
        }

        return null;
    }
}
