package polytechnic.bh.PassPlatforms_Backend.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import polytechnic.bh.PassPlatforms_Backend.Dao.ApplicationDao;
import polytechnic.bh.PassPlatforms_Backend.Dto.GenericDto;
import polytechnic.bh.PassPlatforms_Backend.Entity.Application;
import polytechnic.bh.PassPlatforms_Backend.Repository.ApplicationRepo;
import polytechnic.bh.PassPlatforms_Backend.Repository.ApplicationStatusRepo;
import polytechnic.bh.PassPlatforms_Backend.Repository.UserRepo;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.*;

@RestController
@RequestMapping("/api/application")
public class ApplicationCont<T> {

    @Autowired
    ApplicationRepo applicationRepo;

    @Autowired
    ApplicationStatusRepo applicationStatusRepo;

    @Autowired
    UserRepo userRepo;

    @GetMapping("/")
    public ResponseEntity<GenericDto<T>> getAllApplications(@RequestHeader(value = "requestKey", required = false) String requestKey)
    {
        if (Objects.equals(requestKey, "student-3e1d-4e5f-a2b1-6c7d8e9f0a1b"))
        {
            //retrieve
            List<Application> applications = applicationRepo.findAll();
            List<ApplicationDao> applicationDtos = new ArrayList<>();

            GenericDto<T> genericDto = new GenericDto<>();

            if(!applications.isEmpty())
            {
                for (Application application : applications)
                {
                    applicationDtos.add(new ApplicationDao(application));
                }

                genericDto.setTransObject((T) applicationDtos);

                return new ResponseEntity<>(genericDto, HttpStatus.OK);
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

//    @GetMapping("/{applicationID}")
//    public ResponseEntity<ApplicationDto> getApplication(@RequestHeader(value = "requestKey", required = false) String requestKey, @PathVariable("applicationID") int applicationID)
//    {
//        if (Objects.equals(requestKey, "student-3e1d-4e5f-a2b1-6c7d8e9f0a1b"))
//        {
//            Optional<Application> application = applicationRepo.findById(applicationID);
//
//            if(application.isPresent())
//            {
//                return new ResponseEntity<>(new ApplicationDto(application.get()), HttpStatus.OK);
//            }
//            else
//            {
//                return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
//            }
//
//        }
//        else
//        {
//            return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
//        }
//
//    }
//
//    @PostMapping("/")
//    public ResponseEntity<ApplicationDto> createApplication(@RequestBody ApplicationDto applicationDto)
//    {
//        Application newapplicationton = new Application();
//
//        //automate id creation
//        //newapplicationton.setApplicationid(2);
//
//        newapplicationton.setDatetime(Timestamp.from(Instant.now()));
//        newapplicationton.setNote("new has been created");
//        newapplicationton.setApplicationStatus(applicationStatusRepo.getReferenceById('c'));
//        newapplicationton.setUser(userRepo.getReferenceById("202002789"));
//
//        applicationRepo.save(newapplicationton);
//
//        return new ResponseEntity<>(null,HttpStatus.OK);
//    }
//
//    @PutMapping("/")
//    public ResponseEntity<ApplicationDto> updateApplication()
//    {
//        Application applicationtoUpdate = applicationRepo.getReferenceById(1);
//
//        applicationtoUpdate.setNote("has been updated chile");
//        applicationRepo.save(applicationtoUpdate);
//
//        return new ResponseEntity<>(null,HttpStatus.OK);
//    }
//
//    @DeleteMapping("/")
//    public ResponseEntity<ApplicationDto> deleteApplication()
//    {
//
//
//        return new ResponseEntity<>(null,HttpStatus.OK);
//    }
}
