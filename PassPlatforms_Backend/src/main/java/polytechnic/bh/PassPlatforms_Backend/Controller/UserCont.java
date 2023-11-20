package polytechnic.bh.PassPlatforms_Backend.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import polytechnic.bh.PassPlatforms_Backend.Entity.User;
import polytechnic.bh.PassPlatforms_Backend.Repository.StudentRepo;
import polytechnic.bh.PassPlatforms_Backend.Repository.UserRepo;

import java.util.Objects;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class UserCont
{

    @Autowired
    UserRepo userRepo;

    @Autowired
    StudentRepo studentRepo;

    @GetMapping("/User")
    public ResponseEntity<Optional<User>> getAllTutorials(@RequestHeader(value = "requestKey", required = false) String requestKey)
    {
        if (Objects.equals(requestKey, "student-3e1d-4e5f-a2b1-6c7d8e9f0a1b"))
        {
            //List<User> users = userRepo.findAll();

            Optional<User> student = userRepo.findById("201900500");

            return new ResponseEntity<>(student, HttpStatus.OK);
        }
        else
        {
            return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
        }

    }
}
