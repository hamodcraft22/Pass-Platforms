package polytechnic.bh.PassPlatforms_Backend.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import polytechnic.bh.PassPlatforms_Backend.Dao.RoleDao;
import polytechnic.bh.PassPlatforms_Backend.Dao.UserDao;
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
                User newStudent = new User();
                newStudent.setUserid(userID);
                newStudent.setRole(roleRepo.getReferenceById(1));

                return userRepo.save(newStudent);
            }
            else
            {
                User newTutor = new User();
                newTutor.setUserid(userID);
                newTutor.setRole(roleRepo.getReferenceById(3));

                return userRepo.save(newTutor);
            }
        }
    }

    public List<UserDao> makeLeaders(List<String> users)
    {
        List<UserDao> newLeaders = new ArrayList<>();

        for (String userID : users)
        {
            User retirvedUser = getUser(userID);

            userRepo.makeLeaders(retirvedUser.getUserid());

            User updatedUser = getUser(retirvedUser.getUserid());

            newLeaders.add(new UserDao(updatedUser.getUserid(), new RoleDao(updatedUser.getRole()), null));
        }

        return newLeaders;
    }
}
