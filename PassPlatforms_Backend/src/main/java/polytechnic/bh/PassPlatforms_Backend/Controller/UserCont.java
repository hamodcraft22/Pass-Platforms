package polytechnic.bh.PassPlatforms_Backend.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import polytechnic.bh.PassPlatforms_Backend.Dao.UserDao;
import polytechnic.bh.PassPlatforms_Backend.Dto.GenericDto;
import polytechnic.bh.PassPlatforms_Backend.Entity.User;
import polytechnic.bh.PassPlatforms_Backend.Repository.UserRepo;
import polytechnic.bh.PassPlatforms_Backend.Service.UserServ;

import java.util.List;
import java.util.Objects;

import static polytechnic.bh.PassPlatforms_Backend.Constant.APIkeyConstant.ADMIN_KEY;
import static polytechnic.bh.PassPlatforms_Backend.Constant.APIkeyConstant.MANAGER_KEY;
import static polytechnic.bh.PassPlatforms_Backend.Util.TokenValidation.isValidToken;

@RestController
@RequestMapping("/api/user")
public class UserCont
{

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private UserServ userServ;

    // get all users
    @GetMapping("")
    public ResponseEntity<GenericDto<List<User>>> getAllUsers(@RequestHeader(value = "Authorization", required = false) String requestKey)
    {
        if (Objects.equals(requestKey, ADMIN_KEY) || Objects.equals(requestKey, MANAGER_KEY))
        {
            List<User> users = userRepo.findAll();

            return new ResponseEntity<>(new GenericDto<>(null, users, null), HttpStatus.OK);
        }
        else
        {
            return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
        }
    }

    // get user
    @GetMapping("/userlog")
    public ResponseEntity<GenericDto<User>> userLog(@RequestHeader(value = "Authorization") String barerKey)
    {
        String userID = isValidToken(barerKey);

        if (userID != null)
        {
            return new ResponseEntity<>(new GenericDto<>(null, userServ.getUser(userID), null), HttpStatus.OK);
        }
        else
        {
            return new ResponseEntity<>(new GenericDto<>(null, null , null), HttpStatus.UNAUTHORIZED);
        }
    }
}
