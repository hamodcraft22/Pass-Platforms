package polytechnic.bh.PassPlatforms_Backend.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import polytechnic.bh.PassPlatforms_Backend.Dao.ApplicationDao;
import polytechnic.bh.PassPlatforms_Backend.Dao.UserDao;
import polytechnic.bh.PassPlatforms_Backend.Dto.ApplicationDto;
import polytechnic.bh.PassPlatforms_Backend.Dto.GenericDto;
import polytechnic.bh.PassPlatforms_Backend.Service.ApplicationServ;
import polytechnic.bh.PassPlatforms_Backend.Service.TranscriptServ;
import polytechnic.bh.PassPlatforms_Backend.Service.UserServ;

import java.util.List;
import java.util.Objects;

import static polytechnic.bh.PassPlatforms_Backend.Constant.ApplicationStatusConstant.*;
import static polytechnic.bh.PassPlatforms_Backend.Constant.RoleConstant.*;
import static polytechnic.bh.PassPlatforms_Backend.Util.TokenValidation.isValidToken;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/application")
public class ApplicationCont
{

    @Autowired
    private ApplicationServ applicationServ;

    @Autowired
    private UserServ userServ;

    @Autowired
    private TranscriptServ transcriptServ;

    @GetMapping("")
    public ResponseEntity<GenericDto<List<ApplicationDao>>> getAllApplications(
            @RequestHeader(value = "Authorization") String requestKey)
    {
        String userID = isValidToken(requestKey);

        if (userID != null)
        {
            // token is valid, get user and role
            UserDao user = userServ.getUser(userID);

            // check roles for api
            if (user.getRole().getRoleid() == 4 || user.getRole().getRoleid() == 5)
            {
                //retrieve from service
                List<ApplicationDao> applicationDaos = applicationServ.getAllApplications(false);

                if (applicationDaos != null && !applicationDaos.isEmpty())
                {
                    return new ResponseEntity<>(new GenericDto<>(null, applicationDaos, null, null), HttpStatus.OK);
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
        else
        {
            return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
        }

    }

    @GetMapping("/{applicationID}")
    public ResponseEntity<GenericDto<ApplicationDao>> getApplication(
            @RequestHeader(value = "Authorization") String requestKey,
            @PathVariable("applicationID") int applicationID)
    {
        String userID = isValidToken(requestKey);

        if (userID != null)
        {
            //token is valid, get user and role
            UserDao user = userServ.getUser(userID);

            // if it is an admin or manager, return anyway
            if (user.getRole().getRoleid() == ROLE_MANAGER || user.getRole().getRoleid() == ROLE_ADMIN)
            {
                ApplicationDao applicationDao = applicationServ.getApplicationDetailsByID(applicationID);

                if (applicationDao != null)
                {
                    return new ResponseEntity<>(new GenericDto<>(null, applicationDao, null, null), HttpStatus.OK);
                }
                else
                {
                    return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
                }
            }
            //if it is a student, check if it is their application
            else if (user.getRole().getRoleid() == ROLE_STUDENT)
            {
                ApplicationDao applicationDao = applicationServ.getApplicationDetailsByID(applicationID);

                if (applicationDao != null)
                {
                    if (Objects.equals(applicationDao.getUser().getUserid(), userID))
                    {
                        return new ResponseEntity<>(new GenericDto<>(null, applicationDao, null, null), HttpStatus.OK);
                    }
                    else
                    {
                        return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
                    }
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
        else
        {
            return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
        }


    }


    // get application by student
    @GetMapping("/student/{studentID}")
    public ResponseEntity<GenericDto<ApplicationDao>> getStudentApplication(
            @RequestHeader(value = "Authorization") String requestKey,
            @PathVariable("studentID") String studentID)
    {
        String userID = isValidToken(requestKey);

        if (userID != null)
        {
            //token is valid, get user and role
            UserDao user = userServ.getUser(userID);

            //if it is a student, get their
            if (user.getRole().getRoleid() == ROLE_STUDENT)
            {
                ApplicationDao applicationDao = applicationServ.getApplicationDetailsByUser(studentID);

                if (applicationDao != null)
                {
                    if (Objects.equals(applicationDao.getUser().getUserid(), userID))
                    {
                        return new ResponseEntity<>(new GenericDto<>(null, applicationDao, null, null), HttpStatus.OK);
                    }
                    else
                    {
                        return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
                    }
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
        else
        {
            return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
        }


    }

    // creating an application (by student);
    @PostMapping("")
    public ResponseEntity<GenericDto<ApplicationDao>> createApplication(
            @RequestHeader(value = "Authorization") String requestKey,
            @RequestBody ApplicationDto applicationDto)
    {
        String userID = isValidToken(requestKey);

        if (userID != null)
        {
            //token is valid, get user and role
            UserDao user = userServ.getUser(userID);

            if (user.getRole().getRoleid() == ROLE_STUDENT)
            {
                // check if user has an application first
                ApplicationDao retrivedApplicationDao = applicationServ.getApplicationDetailsByUser(userID);

                if (retrivedApplicationDao == null)
                {
                    // user has no application, can create a new one

                    // multi transcript upload
                    transcriptServ.createMultiTranscript(applicationDto.getTranscripts());


                    if (applicationServ.createApplication(userID, applicationDto.getNote()) != null)
                    {
                        return new ResponseEntity<>(null, HttpStatus.OK);
                    }
                    else
                    {
                        return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
                    }
                }
                else
                {
                    // user already has application
                    return new ResponseEntity<>(null, HttpStatus.FORBIDDEN);
                }
            }
            else
            {
                return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
            }
        }
        else
        {
            return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
        }
    }

    // update an application (by manager)
    @PutMapping("")
    public ResponseEntity<GenericDto<ApplicationDao>> updateApplication(
            @RequestHeader(value = "Authorization") String requestKey,
            @RequestBody ApplicationDao applicationGotten)
    {
        String userID = isValidToken(requestKey);

        if (userID != null)
        {
            //token is valid, get user and role
            UserDao user = userServ.getUser(userID);

            // if it is a student requesting an update, update y student id, if it is a manager update by passed in application id
            if (user.getRole().getRoleid() == ROLE_STUDENT)
            {
                ApplicationDao retrivedApplicationDao = applicationServ.getApplicationDetailsByUser(userID);

                if (retrivedApplicationDao != null)
                {
                    // check if it is the student application (extra verification) - did the student pass in his own application
                    if (applicationGotten.getUser().getUserid().equals(userID))
                    {
                        // update application (status only to canceled or reopened by student)
                        char applicationStatus = applicationGotten.getApplicationStatus().getStatusid();

                        if ((retrivedApplicationDao.getApplicationStatus().getStatusid() == APLC_CANCLED && applicationGotten.getApplicationStatus().getStatusid() == APLC_REOPENED) || ((retrivedApplicationDao.getApplicationStatus().getStatusid() != APLC_ACCEPTED && retrivedApplicationDao.getApplicationStatus().getStatusid() != APLC_REJECTED && retrivedApplicationDao.getApplicationStatus().getStatusid() != APLC_CANCLED) && (applicationGotten.getApplicationStatus().getStatusid() == APLC_CANCLED)))
                        {
                            ApplicationDao responseAplDao = applicationServ.updateApplication(retrivedApplicationDao.getApplicationid(), applicationStatus, true);

                            if (responseAplDao != null)
                            {
                                return new ResponseEntity<>(new GenericDto<>(null, responseAplDao, null, null), HttpStatus.OK);
                            }
                            else
                            {
                                return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
                            }
                        }
                        else
                        {
                            return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
                        }
                    }
                    else
                    {
                        return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
                    }
                }
                else
                {
                    // no application found to update
                    return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
                }
            }
            else if (user.getRole().getRoleid() == ROLE_MANAGER || user.getRole().getRoleid() == ROLE_ADMIN)
            {
                // get application based on the passed in application info
                ApplicationDao retrivedApplicationDao = applicationServ.getApplicationDetailsByID(applicationGotten.getApplicationid());

                if (retrivedApplicationDao != null)
                {
                    char applicationStatus = applicationGotten.getApplicationStatus().getStatusid();

                    if (applicationStatus != APLC_CREATED && applicationStatus != APLC_CANCLED && applicationStatus != APLC_REOPENED)
                    {
                        ApplicationDao responseAplDao = applicationServ.updateApplication(retrivedApplicationDao.getApplicationid(), applicationStatus, false);

                        if (responseAplDao != null)
                        {
                            return new ResponseEntity<>(new GenericDto<>(null, responseAplDao, null, null), HttpStatus.OK);
                        }
                        else
                        {
                            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
                        }
                    }
                    else
                    {
                        return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
                    }
                }
                else
                {
                    // no application found to update
                    return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
                }
            }
            else
            {
                return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
            }
        }
        else
        {
            return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
        }
    }

    @DeleteMapping("/{applicationID}")
    public ResponseEntity<GenericDto<ApplicationDao>> deleteApplication(
            @RequestHeader(value = "Authorization") String requestKey,
            @PathVariable("applicationID") int applicationID
    )
    {
        String userID = isValidToken(requestKey);

        if (userID != null)
        {
            //token is valid, get user and role
            UserDao user = userServ.getUser(userID);

            // only managers and admin are able to fully delete from the db
            if (user.getRole().getRoleid() == ROLE_MANAGER || user.getRole().getRoleid() == ROLE_ADMIN)
            {
                if (applicationServ.deleteApplication(applicationID))
                {
                    return new ResponseEntity<>(null, HttpStatus.OK);
                }
                else
                {
                    return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
                }
            }
            else
            {
                return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
            }
        }
        else
        {
            return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
        }

    }
}
