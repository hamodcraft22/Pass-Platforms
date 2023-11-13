package polytechnic.bh.PassPlatforms_Backend.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import polytechnic.bh.PassPlatforms_Backend.Dto.ApplicationDto;
import polytechnic.bh.PassPlatforms_Backend.Entity.Application;
import polytechnic.bh.PassPlatforms_Backend.Repository.ApplicationRepo;
import polytechnic.bh.PassPlatforms_Backend.Repository.ApplicationStatusRepo;
import polytechnic.bh.PassPlatforms_Backend.Repository.UserRepo;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.*;

@RestController
@RequestMapping("/api")
public class ApplicationCont {

    @Autowired
    ApplicationRepo applicationRepo;

    @Autowired
    ApplicationStatusRepo applicationStatusRepo;

    @Autowired
    UserRepo userRepo;

    @GetMapping("/application")
    public ResponseEntity<List<ApplicationDto>> getAllApplications(@RequestHeader(value = "requestKey", required = false) String requestKey)
    {
        if (Objects.equals(requestKey, "student-3e1d-4e5f-a2b1-6c7d8e9f0a1b"))
        {
            List<Application> applications = applicationRepo.findAll();
            List<ApplicationDto> applicationDtos = new ArrayList<>();

            if(!applications.isEmpty())
            {
                for (Application application : applications)
                {
                    applicationDtos.add(new ApplicationDto(application));
                }

                return new ResponseEntity<>(applicationDtos, HttpStatus.OK);
            }
            else
            {
                return new ResponseEntity<>(null, HttpStatus.OK);
            }

        }
        else
        {
            return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
        }

    }

    @GetMapping("/application/{applicationID}")
    public ResponseEntity<ApplicationDto> getApplication(@RequestHeader(value = "requestKey", required = false) String requestKey, @PathVariable("applicationID") int applicationID)
    {
        if (Objects.equals(requestKey, "student-3e1d-4e5f-a2b1-6c7d8e9f0a1b"))
        {
            Optional<Application> application = applicationRepo.findById(applicationID);


            if(application.isPresent())
            {
                return new ResponseEntity<>(new ApplicationDto(application.get()), HttpStatus.OK);
            }
            else
            {
                return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
            }

        }
        else
        {
            return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
        }

    }

    @PostMapping("/applicationCreate")
    public ResponseEntity<ApplicationDto> createApplication()
    {
        Application newapplicationton = new Application();

        newapplicationton.setApplicationid(2);
        newapplicationton.setDatetime(Timestamp.from(Instant.now()));
        newapplicationton.setNote("new has been created");
        newapplicationton.setApplicationStatus(applicationStatusRepo.getReferenceById('c'));
        newapplicationton.setUser(userRepo.getReferenceById("202002789"));

        applicationRepo.save(newapplicationton);

        return new ResponseEntity<>(null,HttpStatus.OK);
    }

    @PutMapping("/applicationUpdate")
    public ResponseEntity<ApplicationDto> updateApplication()
    {
        Application applicationtoUpdate = applicationRepo.getReferenceById(1);

        applicationtoUpdate.setNote("has been updated chile");
        applicationRepo.save(applicationtoUpdate);

        return new ResponseEntity<>(null,HttpStatus.OK);
    }
}
