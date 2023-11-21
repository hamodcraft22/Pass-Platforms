package polytechnic.bh.PassPlatforms_Backend.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import polytechnic.bh.PassPlatforms_Backend.Dao.MajorDao;
import polytechnic.bh.PassPlatforms_Backend.Dto.GenericDto;
import polytechnic.bh.PassPlatforms_Backend.Service.MajorServ;

import java.util.List;
import java.util.Objects;

import static polytechnic.bh.PassPlatforms_Backend.Constant.APIkeyConstant.ADMIN_KEY;
import static polytechnic.bh.PassPlatforms_Backend.Constant.APIkeyConstant.MANAGER_KEY;

@RestController
@RequestMapping("/api/major")
public class MajorCont
{

    @Autowired
    private MajorServ majorServ;

    // get all majors
    @GetMapping("")
    public ResponseEntity<GenericDto<List<MajorDao>>> getAllMajors(
            @RequestHeader(value = "Authorization", required = false) String requestKey)
    {
        // Any person can use these

        List<MajorDao> majors = majorServ.getAllMajors();

        if (majors != null && !majors.isEmpty())
        {
            return new ResponseEntity<>(new GenericDto<>(null, majors, null), HttpStatus.OK);
        }
        else
        {
            return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
        }
    }

    // get major details
    @GetMapping("/{majorID}")
    public ResponseEntity<GenericDto<MajorDao>> getMajorDetails(
            @RequestHeader(value = "Authorization", required = false) String requestKey,
            @PathVariable("majorID") String majorID)
    {
        // Any person can use these

        MajorDao major = majorServ.getMajorDetails(majorID);

        if (major != null)
        {
            return new ResponseEntity<>(new GenericDto<>(null, major, null), HttpStatus.OK);
        }
        else
        {
            return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
        }
    }

    // create major
    @PostMapping("")
    public ResponseEntity<GenericDto<MajorDao>> createMajor(
            @RequestHeader(value = "Authorization", required = false) String requestKey,
            @RequestBody MajorDao majorDao)
    {
        if (Objects.equals(requestKey, ADMIN_KEY) || Objects.equals(requestKey, MANAGER_KEY))
        {
            MajorDao createdMajor = majorServ.createMajor(
                    majorDao.getMajorid(),
                    majorDao.getMajorname(),
                    majorDao.getMajordesc(),
                    majorDao.getSchool().getSchoolid()
            );

            return new ResponseEntity<>(new GenericDto<>(null, createdMajor, null), HttpStatus.CREATED);
        }
        else
        {
            return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
        }
    }

    // edit major
    @PutMapping("")
    public ResponseEntity<GenericDto<MajorDao>> editMajor(
            @RequestHeader(value = "Authorization", required = false) String requestKey,
            @RequestBody MajorDao majorDao)
    {
        if (Objects.equals(requestKey, ADMIN_KEY) || Objects.equals(requestKey, MANAGER_KEY))
        {
            MajorDao editedMajor = majorServ.editMajor(majorDao);

            if (editedMajor != null)
            {
                return new ResponseEntity<>(new GenericDto<>(null, editedMajor, null), HttpStatus.OK);
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

    // delete major
    @DeleteMapping("/{majorID}")
    public ResponseEntity<GenericDto<Void>> deleteMajor(
            @RequestHeader(value = "Authorization", required = false) String requestKey,
            @PathVariable("majorID") String majorID)
    {
        if (Objects.equals(requestKey, ADMIN_KEY) || Objects.equals(requestKey, MANAGER_KEY))
        {
            if (majorServ.deleteMajor(majorID))
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

