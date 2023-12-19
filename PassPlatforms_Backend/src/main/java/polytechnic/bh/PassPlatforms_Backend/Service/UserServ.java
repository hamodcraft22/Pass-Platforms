package polytechnic.bh.PassPlatforms_Backend.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import polytechnic.bh.PassPlatforms_Backend.Dao.NotificationDao;
import polytechnic.bh.PassPlatforms_Backend.Dao.RoleDao;
import polytechnic.bh.PassPlatforms_Backend.Dao.UserDao;
import polytechnic.bh.PassPlatforms_Backend.Entity.Notification;
import polytechnic.bh.PassPlatforms_Backend.Entity.User;
import polytechnic.bh.PassPlatforms_Backend.Repository.RoleRepo;
import polytechnic.bh.PassPlatforms_Backend.Repository.UserRepo;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static polytechnic.bh.PassPlatforms_Backend.Constant.ManagerConst.MANGER_ID;
import static polytechnic.bh.PassPlatforms_Backend.Util.UsersService.getAzureAdName;

// first time sign in / members adding
@Service
public class UserServ
{
    @Autowired
    private UserRepo userRepo;

    @Autowired
    private RoleRepo roleRepo;

    // create user if it's not registered in the system
    public UserDao getUser(String userID)
    {
        // first get user from db
        Optional<User> wantedUser = userRepo.findById(userID);

        if (wantedUser.isPresent())
        {
            List<NotificationDao> newNotifications = null;

            if (wantedUser.get().getNotifications() != null)
            {
                newNotifications = new ArrayList<>();
                for (Notification notification : wantedUser.get().getNotifications())
                {
                    newNotifications.add(new NotificationDao(notification));
                }
            }

            return new UserDao(wantedUser.get().getUserid(), new RoleDao(wantedUser.get().getRole()), getAzureAdName(wantedUser.get().getUserid()), newNotifications);
        }
        else
        {
            // logic is if user id start with a 2 (start of our student ids, it's a student, in any other case - it's a tutor)
            if (userID.startsWith("20"))
            {
                User newStudent = new User();
                newStudent.setUserid(userID);
                newStudent.setRole(roleRepo.getReferenceById(1));

                User savedStudent = userRepo.save(newStudent);

                return new UserDao(savedStudent.getUserid(), new RoleDao(newStudent.getRole()), getAzureAdName(savedStudent.getUserid()), null);
            }
            else if (userID.equals(MANGER_ID))
            {
                User manager = new User();
                manager.setUserid(userID);
                manager.setRole(roleRepo.getReferenceById(4));

                User savedStudent = userRepo.save(manager);

                return new UserDao(savedStudent.getUserid(), new RoleDao(manager.getRole()), getAzureAdName(savedStudent.getUserid()), null);
            }
            else
            {
                User newTutor = new User();
                newTutor.setUserid(userID);
                newTutor.setRole(roleRepo.getReferenceById(3));

                User savedTutor = userRepo.save(newTutor);

                return new UserDao(savedTutor.getUserid(), new RoleDao(savedTutor.getRole()), getAzureAdName(savedTutor.getUserid()), null);
            }
        }
    }

    // get school leaders
    public List<UserDao> schoolLeaders(String schoolID)
    {
        List<UserDao> leaders = new ArrayList<>();
        for (User user : userRepo.getSchoolLeaders(schoolID))
        {
            UserDao leaderDao = getUser(user.getUserid());
            leaderDao.setNotifications(null);

            leaders.add(leaderDao);
        }
        return leaders;
    }

    // get course leaders
    public List<UserDao> courseLeaders(String courseID)
    {
        List<UserDao> leaders = new ArrayList<>();
        for (User user : userRepo.getCourseLeaders(courseID))
        {
            UserDao leaderDao = getUser(user.getUserid());
            leaderDao.setNotifications(null);

            leaders.add(leaderDao);
        }
        return leaders;
    }

    public List<UserDao> makeLeaders(List<String> users)
    {
        List<UserDao> newLeaders = new ArrayList<>();

        for (String userID : users)
        {
            UserDao retirvedUser = getUser(userID);

            userRepo.makeLeaders(retirvedUser.getUserid());

            UserDao updatedUser = getUser(retirvedUser.getUserid());

            newLeaders.add(new UserDao(updatedUser.getUserid(), updatedUser.getRole(), getAzureAdName(updatedUser.getUserid()), null));
        }

        return newLeaders;
    }
}
