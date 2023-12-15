package polytechnic.bh.PassPlatforms_Backend.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import polytechnic.bh.PassPlatforms_Backend.Entity.Child.Student;
import polytechnic.bh.PassPlatforms_Backend.Entity.Child.Tutor;
import polytechnic.bh.PassPlatforms_Backend.Entity.User;
import polytechnic.bh.PassPlatforms_Backend.Repository.RoleRepo;
import polytechnic.bh.PassPlatforms_Backend.Repository.UserRepo;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

// first time sign in / members adding
@Service
public class UserServ
{
    @Autowired
    private UserRepo userRepo;

    @Autowired
    private RoleRepo roleRepo;

    // create user if it's not registered in the system
    public User getUser(String userID)
    {
        // first get user from db
        Optional<User> wantedUser = userRepo.findById(userID);

        if (wantedUser.isPresent())
        {
            return wantedUser.get();
        }
        else
        {
            // logic is if user id start with a 2 (start of our student ids, it's a student, in any other case - it's a tutor)
            if (userID.startsWith("20"))
            {
                Student newStudent = new Student();
                newStudent.setUserid(userID);

                return userRepo.save(newStudent);
            }
            else
            {
                Tutor newTutor = new Tutor();
                newTutor.setUserid(userID);

                return userRepo.save(newTutor);
            }
        }
    }

    public List<User> makeLeaders(List<String> users)
    {
        List<User> newLeaders = new ArrayList<>();

        for (String userID : users)
        {
            User retirvedUser = getUser(userID);
            retirvedUser.setRole(roleRepo.getReferenceById(2));

            userRepo.save(retirvedUser);
        }

        return newLeaders;
    }
}
