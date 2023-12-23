package polytechnic.bh.PassPlatforms_Backend.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import polytechnic.bh.PassPlatforms_Backend.Dao.RoleDao;
import polytechnic.bh.PassPlatforms_Backend.Dao.UserDao;
import polytechnic.bh.PassPlatforms_Backend.Dto.GenericDto;
import polytechnic.bh.PassPlatforms_Backend.Entity.User;
import polytechnic.bh.PassPlatforms_Backend.Repository.UserRepo;
import polytechnic.bh.PassPlatforms_Backend.Service.UserServ;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static polytechnic.bh.PassPlatforms_Backend.Constant.RoleConstant.ROLE_ADMIN;
import static polytechnic.bh.PassPlatforms_Backend.Constant.RoleConstant.ROLE_MANAGER;
import static polytechnic.bh.PassPlatforms_Backend.Util.TokenValidation.isValidToken;
import static polytechnic.bh.PassPlatforms_Backend.Util.UsersService.getAzureAdName;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/user")
public class UserCont
{

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private UserServ userServ;

    // get all saved users -- tested | added
    @GetMapping("")
    public ResponseEntity<GenericDto<List<UserDao>>> getAllUsers(@RequestHeader(value = "Authorization", required = false) String requestKey)
    {
        String userID = isValidToken(requestKey);

        if (userID != null)
        {
            List<UserDao> users = new ArrayList<>();

            for (User user : userRepo.findAll())
            {
                users.add(new UserDao(user.getUserid(), new RoleDao(user.getRole()), getAzureAdName(user.getUserid()), null));
            }

            return new ResponseEntity<>(new GenericDto<>(null, users, null, null), HttpStatus.OK);
        }
        else
        {
            return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
        }
    }

    // get save user details
    @GetMapping("/{userID}")
    public ResponseEntity<GenericDto<?>> getInfo(@RequestHeader(value = "Authorization", required = false) String requestKey,
                                                 @PathVariable(value = "userID") String userID)
    {
        String thisUserID = isValidToken(requestKey);

        if (thisUserID != null)
        {
            //token is valid, get user and role
            UserDao user = userServ.getUser(userID);

            if (user.getRole().getRoleid() == ROLE_ADMIN || user.getRole().getRoleid() == ROLE_MANAGER)
            {
                Optional<User> gottenUser = userRepo.findById(userID);

                if (gottenUser.isPresent())
                {
                    return new ResponseEntity<>(new GenericDto<>(null, new UserDao(gottenUser.get()), null, null), HttpStatus.OK);
                }
                else
                {
                    return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
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

    // get user (logged in) / create if first time login -- tested | added
    @GetMapping("/userlog")
    public ResponseEntity<GenericDto<UserDao>> userLog(@RequestHeader(value = "Authorization") String barerKey)
    {
        String userID = isValidToken(barerKey);

        if (userID != null)
        {
            return new ResponseEntity<>(new GenericDto<>(null, userServ.getUser(userID), null, null), HttpStatus.OK);
        }
        else
        {
            return new ResponseEntity<>(new GenericDto<>(null, null, null, null), HttpStatus.UNAUTHORIZED);
        }
    }

    // get users based on school
    @GetMapping("/school/{schoolID}")
    public ResponseEntity<GenericDto<List<UserDao>>> schoolUsers(@RequestHeader(value = "Authorization") String barerKey,
                                                                 @PathVariable(value = "schoolID") String schoolID)
    {
        String userID = isValidToken(barerKey);

        if (userID != null)
        {
            return new ResponseEntity<>(new GenericDto<>(null, userServ.schoolLeaders(schoolID), null, null), HttpStatus.OK);
        }
        else
        {
            return new ResponseEntity<>(new GenericDto<>(null, null, null, null), HttpStatus.UNAUTHORIZED);
        }
    }

    // get users based on course
    @GetMapping("/course/{courseID}")
    public ResponseEntity<GenericDto<List<UserDao>>> courseUsers(@RequestHeader(value = "Authorization") String barerKey,
                                                                 @PathVariable(value = "courseID") String courseID)
    {
        String userID = isValidToken(barerKey);

        if (userID != null)
        {
            return new ResponseEntity<>(new GenericDto<>(null, userServ.courseLeaders(courseID), null, null), HttpStatus.OK);
        }
        else
        {
            return new ResponseEntity<>(new GenericDto<>(null, null, null, null), HttpStatus.UNAUTHORIZED);
        }
    }

    // make user into leader
    @PostMapping("/leaderify")
    public ResponseEntity<GenericDto<List<UserDao>>> makeLeaders(@RequestHeader(value = "Authorization") String barerKey, @RequestBody List<String> studentIDs)
    {
        String userID = isValidToken(barerKey);

        if (userID != null)
        {
            UserDao retrivedUser = userServ.getUser(userID);

            if (retrivedUser.getRole().getRoleid() == 4 || retrivedUser.getRole().getRoleid() == 5)
            {
                List<UserDao> newLeaders = userServ.makeLeaders(studentIDs);

                return new ResponseEntity<>(new GenericDto<>(null, newLeaders, null, null), HttpStatus.OK);
            }
            else
            {
                return new ResponseEntity<>(new GenericDto<>(null, null, null, null), HttpStatus.UNAUTHORIZED);
            }
        }
        else
        {
            return new ResponseEntity<>(new GenericDto<>(null, null, null, null), HttpStatus.UNAUTHORIZED);
        }
    }
}
