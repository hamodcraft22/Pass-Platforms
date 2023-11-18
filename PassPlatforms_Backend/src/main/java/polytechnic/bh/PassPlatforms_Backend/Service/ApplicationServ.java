package polytechnic.bh.PassPlatforms_Backend.Service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import polytechnic.bh.PassPlatforms_Backend.Dao.ApplicationDao;
import polytechnic.bh.PassPlatforms_Backend.Dao.ApplicationStatusDao;
import polytechnic.bh.PassPlatforms_Backend.Entity.Application;
import polytechnic.bh.PassPlatforms_Backend.Entity.User;
import polytechnic.bh.PassPlatforms_Backend.Repository.ApplicationRepo;
import polytechnic.bh.PassPlatforms_Backend.Repository.ApplicationStatusRepo;
import polytechnic.bh.PassPlatforms_Backend.Repository.UserRepo;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ApplicationServ {

    @Autowired
    ApplicationRepo applicationRepo;

    @Autowired
    ApplicationStatusRepo applicationStatusRepo;

    @Autowired
    UserRepo userRepo;

    public List<ApplicationDao> getAllApplications(Boolean details)
    {
        List<ApplicationDao> applications = new ArrayList<>();

        if (!details)
        {
            for (Application retrivedApplication : applicationRepo.findAll())
            {
                applications.add(new ApplicationDao(
                        retrivedApplication.getApplicationid(),
                        retrivedApplication.getDatetime().toInstant(),
                        retrivedApplication.getNote(),
                        new ApplicationStatusDao(retrivedApplication.getApplicationStatus()),
                        null,
                        null));
            }
        }
        else
        {
            for (Application retrivedApplication : applicationRepo.findAll())
            {
                applications.add(new ApplicationDao(retrivedApplication));
            }
        }

        return applications;
    }

    public ApplicationDao getApplicationDetailsByID(int applcationID)
    {
        Optional<Application> retrivedApplication = applicationRepo.findById(applcationID);

        return retrivedApplication.map(ApplicationDao::new).orElse(null);
    }

    public ApplicationDao getApplicationDetailsByUser(String userID)
    {
        Optional<Application> retrivedApplication = applicationRepo.findApplicationByUser_Userid(userID);

        return retrivedApplication.map(ApplicationDao::new).orElse(null);
    }

    public ApplicationDao createApplication(String studentID, String applicationNote)
    {
        Application newapplicationton = new Application();

        newapplicationton.setDatetime(Timestamp.from(Instant.now()));
        newapplicationton.setNote(applicationNote);
        newapplicationton.setApplicationStatus(applicationStatusRepo.getReferenceById('c'));
        newapplicationton.setUser(userRepo.getReferenceById(studentID));

        return new ApplicationDao(applicationRepo.save(newapplicationton));
    }
}
