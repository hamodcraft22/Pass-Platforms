package polytechnic.bh.PassPlatforms_Backend.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import polytechnic.bh.PassPlatforms_Backend.Dto.UserInfoDto;
import polytechnic.bh.PassPlatforms_Backend.Service.LogServ;
import polytechnic.bh.PassPlatforms_Backend.Util.UsersService;

import java.util.List;

import static polytechnic.bh.PassPlatforms_Backend.Constant.AuthConstant.TENANT_ID;
import static polytechnic.bh.PassPlatforms_Backend.Constant.ManagerConst.MANGER_ID;
import static polytechnic.bh.PassPlatforms_Backend.Util.TokenValidation.isValidToken;
import static polytechnic.bh.PassPlatforms_Backend.Util.UsersService.*;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/users")
public class UsersCont
{
    @Autowired
    private LogServ logServ;

    // get all users
    @GetMapping("")
    public ResponseEntity<?> getAllADUsers(@RequestHeader(value = "Authorization", required = false) String requestKey)
    {
        String userID = isValidToken(requestKey);

        try
        {
            if (userID != null)
            {
                try
                {
                    if (allAzureAdUsers.isEmpty())
                    {
                        refreshUsers();
                    }

                    return new ResponseEntity<>(allAzureAdUsers, HttpStatus.OK);
                }
                catch (Exception e)
                {
                    return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
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

    // get all students -- tested | added
    @GetMapping("/students")
    public ResponseEntity<List<UserInfoDto>> getAllADStudents(@RequestHeader(value = "Authorization", required = false) String requestKey)
    {
        String userID = isValidToken(requestKey);

        try
        {
            if (userID != null)
            {
                try
                {
                    if (allAzureStudents.isEmpty())
                    {
                        refreshUsers();
                    }

                    return new ResponseEntity<>(allAzureStudents, HttpStatus.OK);
                }
                catch (Exception e)
                {
                    return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
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

    // refresh all users in local cache - tested
    @GetMapping("/refresh")
    public ResponseEntity<?> refreshCachedUsers(@RequestHeader(value = "Authorization", required = false) String requestKey)
    {
        String userID = isValidToken(requestKey);

        try
        {
            if (userID != null)
            {
                try
                {
                    refreshUsers();

                    return new ResponseEntity<>(null, HttpStatus.OK);
                }
                catch (Exception ex)
                {
                    return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
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
