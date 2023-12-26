package polytechnic.bh.PassPlatforms_Backend.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import polytechnic.bh.PassPlatforms_Backend.Dao.SchoolDao;
import polytechnic.bh.PassPlatforms_Backend.Dao.UserDao;
import polytechnic.bh.PassPlatforms_Backend.Dto.GenericDto;
import polytechnic.bh.PassPlatforms_Backend.Service.LogServ;
import polytechnic.bh.PassPlatforms_Backend.Service.SchoolServ;
import polytechnic.bh.PassPlatforms_Backend.Service.UserServ;

import java.util.List;

import static polytechnic.bh.PassPlatforms_Backend.Constant.RoleConstant.ROLE_ADMIN;
import static polytechnic.bh.PassPlatforms_Backend.Constant.RoleConstant.ROLE_MANAGER;
import static polytechnic.bh.PassPlatforms_Backend.Util.TokenValidation.isValidToken;


@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/school")
public class SchoolCont
{

    @Autowired
    private SchoolServ schoolServ;

    @Autowired
    private UserServ userServ;

    @Autowired
    private LogServ logServ;

    // get all schools -- tested | added
    @GetMapping("")
    public ResponseEntity<GenericDto<List<SchoolDao>>> getAllSchools(
            @RequestHeader(value = "Authorization") String requestKey
    )
    {
        String userID = isValidToken(requestKey);

        try
        {
            if (userID != null)
            {
                List<SchoolDao> schools = schoolServ.getAllSchools();

                if (schools != null && !schools.isEmpty())
                {
                    return new ResponseEntity<>(new GenericDto<>(null, schools, null, null), HttpStatus.OK);
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
        catch (Exception ex)
        {
            logServ.createLog(ex.getMessage(), userID);
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    // get revision schools -- tested
    @GetMapping("/revisions")
    public ResponseEntity<GenericDto<List<SchoolDao>>> getRevSchools(
            @RequestHeader(value = "Authorization") String requestKey
    )
    {
        String userID = isValidToken(requestKey);

        try
        {
            if (userID != null)
            {
                List<SchoolDao> schools = schoolServ.getAllRevSchools();

                if (schools != null && !schools.isEmpty())
                {
                    return new ResponseEntity<>(new GenericDto<>(null, schools, null, null), HttpStatus.OK);
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
        catch (Exception ex)
        {
            logServ.createLog(ex.getMessage(), userID);
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    // get available schools -- tested | added
    @GetMapping("/schools")
    public ResponseEntity<GenericDto<List<SchoolDao>>> getAvlbSchools(
            @RequestHeader(value = "Authorization") String requestKey
    )
    {
        String userID = isValidToken(requestKey);

        try
        {
            if (userID != null)
            {
                List<SchoolDao> schools = schoolServ.getAllAvlbSchools();

                if (schools != null && !schools.isEmpty())
                {
                    return new ResponseEntity<>(new GenericDto<>(null, schools, null, null), HttpStatus.OK);
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
        catch (Exception ex)
        {
            logServ.createLog(ex.getMessage(), userID);
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    // get school details - not needed?
    @GetMapping("/{schoolID}")
    public ResponseEntity<GenericDto<SchoolDao>> getSchoolDetails(
            @RequestHeader(value = "Authorization") String requestKey,
            @PathVariable("schoolID") String schoolID)
    {
        String userID = isValidToken(requestKey);

        try
        {
            if (userID != null)
            {
                SchoolDao school = schoolServ.getSchoolDetails(schoolID);

                if (school != null)
                {
                    return new ResponseEntity<>(new GenericDto<>(null, school, null, null), HttpStatus.OK);
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
        catch (Exception ex)
        {
            logServ.createLog(ex.getMessage(), userID);
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    // create multi school -- tested | added
    @PostMapping("/multi")
    public ResponseEntity<GenericDto<List<SchoolDao>>> createMultiSchool(
            @RequestHeader(value = "Authorization") String requestKey,
            @RequestBody List<SchoolDao> schoolsDao)
    {
        String userID = isValidToken(requestKey);

        try
        {
            if (userID != null)
            {
                //token is valid, get user and role
                UserDao user = userServ.getUser(userID);

                if (user.getRole().getRoleid() == ROLE_ADMIN || user.getRole().getRoleid() == ROLE_MANAGER)
                {
                    List<SchoolDao> createdSchool = schoolServ.createMultiSchool(schoolsDao);

                    return new ResponseEntity<>(new GenericDto<>(null, createdSchool, null, null), HttpStatus.CREATED);
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
        catch (Exception ex)
        {
            logServ.createLog(ex.getMessage(), userID);
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    // create school -- tested | added
    @PostMapping("")
    public ResponseEntity<GenericDto<SchoolDao>> createSchool(
            @RequestHeader(value = "Authorization") String requestKey,
            @RequestBody SchoolDao schoolDao)
    {
        String userID = isValidToken(requestKey);

        try
        {
            if (userID != null)
            {
                //token is valid, get user and role
                UserDao user = userServ.getUser(userID);

                if (user.getRole().getRoleid() == ROLE_ADMIN || user.getRole().getRoleid() == ROLE_MANAGER)
                {
                    if (schoolServ.getSchoolDetails(schoolDao.getSchoolid()) == null)
                    {
                        SchoolDao createdSchool = schoolServ.createSchool(schoolDao.getSchoolid(), schoolDao.getSchoolname(), schoolDao.getCourses());

                        return new ResponseEntity<>(new GenericDto<>(null, createdSchool, null, null), HttpStatus.CREATED);
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
        catch (Exception ex)
        {
            logServ.createLog(ex.getMessage(), userID);
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    // edit school -- tested | added
    @PutMapping("")
    public ResponseEntity<GenericDto<SchoolDao>> editSchool(
            @RequestHeader(value = "Authorization") String requestKey,
            @RequestBody SchoolDao schoolDao)
    {
        String userID = isValidToken(requestKey);

        try
        {
            if (userID != null)
            {
                //token is valid, get user and role
                UserDao user = userServ.getUser(userID);

                if (user.getRole().getRoleid() == ROLE_ADMIN || user.getRole().getRoleid() == ROLE_MANAGER)
                {
                    SchoolDao editedSchool = schoolServ.editSchool(schoolDao);

                    if (editedSchool != null)
                    {
                        return new ResponseEntity<>(new GenericDto<>(null, editedSchool, null, null), HttpStatus.OK);
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
        catch (Exception ex)
        {
            logServ.createLog(ex.getMessage(), userID);
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }

    }

    // delete school -- tested | added
    @DeleteMapping("/{schoolID}")
    public ResponseEntity<GenericDto<Void>> deleteSchool(
            @RequestHeader(value = "Authorization") String requestKey,
            @PathVariable("schoolID") String schoolID)
    {
        String userID = isValidToken(requestKey);

        try
        {
            if (userID != null)
            {
                //token is valid, get user and role
                UserDao user = userServ.getUser(userID);

                if (user.getRole().getRoleid() == ROLE_ADMIN || user.getRole().getRoleid() == ROLE_MANAGER)
                {
                    if (schoolServ.deleteSchool(schoolID))
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
        catch (Exception ex)
        {
            logServ.createLog(ex.getMessage(), userID);
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }
}

