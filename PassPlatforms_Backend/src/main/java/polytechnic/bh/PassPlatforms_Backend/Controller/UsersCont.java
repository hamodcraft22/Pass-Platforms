package polytechnic.bh.PassPlatforms_Backend.Controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static polytechnic.bh.PassPlatforms_Backend.Util.TokenValidation.isValidToken;
import static polytechnic.bh.PassPlatforms_Backend.Util.UsersService.allAzureAdUsers;
import static polytechnic.bh.PassPlatforms_Backend.Util.UsersService.refreshUsers;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/users")
public class UsersCont
{
    // get all users
    @GetMapping("")
    public ResponseEntity<?> getAllADUsers(@RequestHeader(value = "Authorization", required = false) String requestKey)
    {
        String userID = isValidToken(requestKey);

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

    @GetMapping("/refresh")
    public ResponseEntity<?> refreshCachedUsers(@RequestHeader(value = "Authorization", required = false) String requestKey)
    {
        String userID = isValidToken(requestKey);

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
}
