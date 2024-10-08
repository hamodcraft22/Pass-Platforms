package polytechnic.bh.PassPlatforms_Backend.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import polytechnic.bh.PassPlatforms_Backend.Dao.NotificationDao;
import polytechnic.bh.PassPlatforms_Backend.Dao.UserDao;
import polytechnic.bh.PassPlatforms_Backend.Dto.GenericDto;
import polytechnic.bh.PassPlatforms_Backend.Service.LogServ;
import polytechnic.bh.PassPlatforms_Backend.Service.NotificationServ;
import polytechnic.bh.PassPlatforms_Backend.Service.UserServ;

import java.util.List;

import static polytechnic.bh.PassPlatforms_Backend.Constant.RoleConstant.ROLE_ADMIN;
import static polytechnic.bh.PassPlatforms_Backend.Util.TokenValidation.isValidToken;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/notification")
public class NotificationCont
{
    @Autowired
    private NotificationServ notificationServ;

    @Autowired
    private UserServ userServ;

    @Autowired
    private LogServ logServ;

    private static String globalNotification;

    // get all of the notification for the logged in user
    @GetMapping("")
    public ResponseEntity<GenericDto<List<NotificationDao>>> getUserNotifications(
            @RequestHeader(value = "Authorization") String requestKey)
    {
        String userID = isValidToken(requestKey);

        try
        {
            if (userID != null)
            {
                //token is valid, get user and role
                UserDao user = userServ.getUser(userID);


                List<NotificationDao> notifications = notificationServ.getUserNotfs(user.getUserid());

                if (notifications != null && !notifications.isEmpty())
                {
                    return new ResponseEntity<>(new GenericDto<>(null, notifications, null, null), HttpStatus.OK);
                }
                else
                {
                    return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
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

    // set the notification and seen
    @GetMapping("/{notficID}")
    public ResponseEntity<GenericDto<NotificationDao>> getNotificationDetails(
            @RequestHeader(value = "Authorization") String requestKey,
            @PathVariable("notficID") int notficID)
    {
        String userID = isValidToken(requestKey);

        try
        {
            if (userID != null)
            {
                NotificationDao notification = notificationServ.getNotifc(notficID);

                if (notification != null)
                {
                    return new ResponseEntity<>(new GenericDto<>(null, notification, null, null), HttpStatus.OK);
                }
                else
                {
                    return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
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

    // get global notification of the platform
    @GetMapping("/global")
    public ResponseEntity<String> getGlobalNotification(
            @RequestHeader(value = "Authorization") String requestKey
    )
    {
        String userID = isValidToken(requestKey);

        if (userID != null)
        {
            if (globalNotification != null)
            {
                return new ResponseEntity<>(globalNotification, HttpStatus.OK);
            }
            else
            {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
        }
        else
        {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }

    // set global notification of the platform TODO - add admin auth
    @PostMapping("/global")
    public ResponseEntity<String> setGlobalNotification(
            @RequestHeader(value = "Authorization") String requestKey,
            @RequestBody String notificationInput
    )
    {
        String userID = isValidToken(requestKey);

        if (userID != null)
        {
            UserDao user = userServ.getUser(userID);

            if (user != null && user.getRole().getRoleid() == ROLE_ADMIN)
            {
                globalNotification = notificationInput;
                return new ResponseEntity<>(globalNotification, HttpStatus.OK);
            }
            else
            {
                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
            }
        }
        else
        {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }

    // delete global notification of the platform TODO - add admin auth
    @DeleteMapping("/global")
    public ResponseEntity<?> deleteGlobalNotification(
            @RequestHeader(value = "Authorization") String requestKey
    )
    {
        String userID = isValidToken(requestKey);

        if (userID != null)
        {
            UserDao user = userServ.getUser(userID);

            if (user != null && user.getRole().getRoleid() == ROLE_ADMIN)
            {
                if (globalNotification != null)
                {
                    globalNotification = null;
                    return new ResponseEntity<>(HttpStatus.OK);
                }
                else
                {
                    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
                }
            }
            else
            {
                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
            }
        }
        else
        {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }
}
