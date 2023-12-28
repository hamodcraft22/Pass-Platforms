package polytechnic.bh.PassPlatforms_Backend.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import polytechnic.bh.PassPlatforms_Backend.Dao.MetadataDao;
import polytechnic.bh.PassPlatforms_Backend.Entity.Metadata;
import polytechnic.bh.PassPlatforms_Backend.Repository.MetadataRepo;
import polytechnic.bh.PassPlatforms_Backend.Repository.SchoolRepo;
import polytechnic.bh.PassPlatforms_Backend.Repository.StatisticRepo;
import polytechnic.bh.PassPlatforms_Backend.Repository.UserRepo;

import java.sql.Date;
import java.util.Optional;

import static polytechnic.bh.PassPlatforms_Backend.Constant.RoleConstant.*;

@Service
public class MetadataServ
{

    @Autowired
    private MetadataRepo metadataRepo;

    @Autowired
    private SchoolRepo schoolRepo;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private StatisticRepo statisticRepo;

    // Get metadata
    public MetadataDao getMetadata()
    {
        Optional<Metadata> retrievedMetadata = metadataRepo.findById(1);
        return retrievedMetadata.map(MetadataDao::new).orElse(null);
    }

    // get if disabled
    public boolean getDisable()
    {
        Optional<Metadata> retrievedMetadata = metadataRepo.findById(1);

        return retrievedMetadata.map(Metadata::isEnabled).orElse(false);
    }

    // Update metadata
    public MetadataDao updateMetadata(MetadataDao metadataDao)
    {

        Metadata updatedMetadata = new Metadata();
        updatedMetadata.setMetadataid(1);
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

    // reset the whole system (delete everything other than the managers and admins accounts)
    // resets the whole system for a new semaster
    public boolean resetSystem()
    {
        try
        {
            // delete all users - automatically deletes everything
            userRepo.deleteAllByRole_Roleid(ROLE_STUDENT);
            userRepo.deleteAllByRole_Roleid(ROLE_LEADER);
            userRepo.deleteAllByRole_Roleid(ROLE_TUTOR);

            // delete all schools
            schoolRepo.deleteAll();

            // delete stats
            statisticRepo.deleteAll();

            // delete metadata
            metadataRepo.deleteAll();

            return true;
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
            return false;
        }
    }
}
