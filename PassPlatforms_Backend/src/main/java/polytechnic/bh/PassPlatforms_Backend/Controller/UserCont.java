package polytechnic.bh.PassPlatforms_Backend.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import polytechnic.bh.PassPlatforms_Backend.Dao.*;
import polytechnic.bh.PassPlatforms_Backend.Dto.GenericDto;
import polytechnic.bh.PassPlatforms_Backend.Entity.User;
import polytechnic.bh.PassPlatforms_Backend.Repository.UserRepo;
import polytechnic.bh.PassPlatforms_Backend.Service.UserServ;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

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

    // get all saved users
    @GetMapping("")
    public ResponseEntity<GenericDto<List<UserDao>>> getAllUsers(@RequestHeader(value = "Authorization", required = false) String requestKey)
    {
        if (Objects.equals(requestKey, ADMIN_KEY) || Objects.equals(requestKey, MANAGER_KEY))
        {
            List<UserDao> users = new ArrayList<>();

            for (User user : userRepo.findAll())
            {
                users.add(new UserDao(user.getUserid(), new RoleDao(user.getRole()), null));
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
        if (Objects.equals(requestKey, ADMIN_KEY) || Objects.equals(requestKey, MANAGER_KEY))
        {
            Optional<User> user = userRepo.findById(userID);

            if (user.isPresent())
            {
                if (user.get().getRole().getRoleid() == 1)
                {
                    // student
                    return new ResponseEntity<>(new GenericDto<>(null, new StudentDao(user.get()), null, null), HttpStatus.OK);
                }
                else if (user.get().getRole().getRoleid() == 2)
                {
                    // leader
                    return new ResponseEntity<>(new GenericDto<>(null, new LeaderDao(user.get()), null, null), HttpStatus.OK);
                }
                else if (user.get().getRole().getRoleid() == 3)
                {
                    // Tutor
                    return new ResponseEntity<>(new GenericDto<>(null, new TutorDao(user.get()), null, null), HttpStatus.OK);
                }
                else
                {
                    // Admin | Manager
                    return new ResponseEntity<>(new GenericDto<>(null, new UserDao(user.get()), null, null), HttpStatus.OK);
                }
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

    // get user (logged in) / create if first time login
    @GetMapping("/userlog")
    public ResponseEntity<GenericDto<User>> userLog(@RequestHeader(value = "Authorization") String barerKey)
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

    // make user into leader
    @PostMapping("/leaderify")
    public ResponseEntity<GenericDto<List<UserDao>>> makeLeaders(@RequestHeader(value = "Authorization") String barerKey, @RequestBody List<String> studentIDs)
    {
        String userID = isValidToken(barerKey);

        if (userID != null)
        {
            User retrivedUser = userServ.getUser(userID);

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
