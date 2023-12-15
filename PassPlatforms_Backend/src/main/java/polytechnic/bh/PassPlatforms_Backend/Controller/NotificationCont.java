package polytechnic.bh.PassPlatforms_Backend.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import polytechnic.bh.PassPlatforms_Backend.Dao.NotificationDao;
import polytechnic.bh.PassPlatforms_Backend.Dto.GenericDto;
import polytechnic.bh.PassPlatforms_Backend.Service.NotificationServ;

import java.util.List;
import java.util.Objects;

import static polytechnic.bh.PassPlatforms_Backend.Constant.APIkeyConstant.ADMIN_KEY;

@RestController
@RequestMapping("/api/notification")
public class NotificationCont
{
    @Autowired
    private NotificationServ notificationServ;

    @GetMapping("")
    public ResponseEntity<GenericDto<List<NotificationDao>>> getUserNotifications(
            @RequestHeader(value = "Authorization") String requestKey)
    {
        if (Objects.equals(requestKey, ADMIN_KEY))
        {
            List<NotificationDao> notifications = notificationServ.getUserNotfs("FROMAZURE");

            if (notifications != null && !notifications.isEmpty())
            {
                return new ResponseEntity<>(new GenericDto<>(null, notifications, null), HttpStatus.OK);
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

    @GetMapping("/{notficID}")
    public ResponseEntity<GenericDto<NotificationDao>> getNotificationDetails(
            @RequestHeader(value = "Authorization") String requestKey,
            @PathVariable("notficID") int notficID)
    {
        if (Objects.equals(requestKey, ADMIN_KEY))
        {
            NotificationDao notification = notificationServ.getNotifc(notficID);

            if (notification != null)
            {
                return new ResponseEntity<>(new GenericDto<>(null, notification, null), HttpStatus.OK);
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
}
