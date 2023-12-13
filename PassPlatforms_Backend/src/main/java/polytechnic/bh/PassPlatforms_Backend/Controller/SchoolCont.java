package polytechnic.bh.PassPlatforms_Backend.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import polytechnic.bh.PassPlatforms_Backend.Dao.SchoolDao;
import polytechnic.bh.PassPlatforms_Backend.Dto.GenericDto;
import polytechnic.bh.PassPlatforms_Backend.Service.SchoolServ;

import java.util.List;
import java.util.Objects;

import static polytechnic.bh.PassPlatforms_Backend.Constant.APIkeyConstant.ADMIN_KEY;
import static polytechnic.bh.PassPlatforms_Backend.Constant.APIkeyConstant.MANAGER_KEY;
import static polytechnic.bh.PassPlatforms_Backend.Util.TokenValidation.isValidToken;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/school")
public class SchoolCont
{

    @Autowired
    private SchoolServ schoolServ;

    // get all schools
    @GetMapping("")
    public ResponseEntity<GenericDto<List<SchoolDao>>> getAllSchools(@RequestHeader(value = "Authorization") String authToken)
    {
        // Any person can use these

        // testing the auth token thing
        if (isValidToken(authToken))
        {
            System.out.println("it work i guess");
        }

        List<SchoolDao> schools = schoolServ.getAllSchools();

        if (schools != null && !schools.isEmpty())
        {
            return new ResponseEntity<>(new GenericDto<>(null, schools, null), HttpStatus.OK);
        }
        else
        {
            return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
        }
    }

    // get school details
    @GetMapping("/{schoolID}")
    public ResponseEntity<GenericDto<SchoolDao>> getSchoolDetails(
            @PathVariable("schoolID") String schoolID)
    {
        // Any person can use these

        SchoolDao school = schoolServ.getSchoolDetails(schoolID);

        if (school != null)
        {
            return new ResponseEntity<>(new GenericDto<>(null, school, null), HttpStatus.OK);
        }
        else
        {
            return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
        }
    }

    // create school
    @PostMapping("")
    public ResponseEntity<GenericDto<SchoolDao>> createSchool(
            @RequestHeader(value = "Authorization") String requestKey,
            @RequestBody SchoolDao schoolDao)
    {
        if (Objects.equals(requestKey, ADMIN_KEY) || Objects.equals(requestKey, MANAGER_KEY))
        {
            SchoolDao createdSchool = schoolServ.createSchool(schoolDao.getSchoolid(), schoolDao.getSchoolname(), schoolDao.getSchooldesc(), schoolDao.getCourses());

            return new ResponseEntity<>(new GenericDto<>(null, createdSchool, null), HttpStatus.CREATED);
        }
        else
        {
            return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
        }
    }

    // edit school
    @PutMapping("")
    public ResponseEntity<GenericDto<SchoolDao>> editSchool(
            @RequestHeader(value = "Authorization") String requestKey,
            @RequestBody SchoolDao schoolDao)
    {

        if (Objects.equals(requestKey, ADMIN_KEY) || Objects.equals(requestKey, MANAGER_KEY))
        {
            SchoolDao editedSchool = schoolServ.editSchool(schoolDao);

            if (editedSchool != null)
            {
                return new ResponseEntity<>(new GenericDto<>(null, editedSchool, null), HttpStatus.OK);
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

    // delete school
    @DeleteMapping("/{schoolID}")
    public ResponseEntity<GenericDto<Void>> deleteSchool(
            @RequestHeader(value = "Authorization") String requestKey,
            @PathVariable("schoolID") String schoolID)
    {
        if (Objects.equals(requestKey, ADMIN_KEY) || Objects.equals(requestKey, MANAGER_KEY))
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
}

