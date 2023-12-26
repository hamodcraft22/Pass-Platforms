package polytechnic.bh.PassPlatforms_Backend.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import polytechnic.bh.PassPlatforms_Backend.Dao.BookingDao;
import polytechnic.bh.PassPlatforms_Backend.Service.MailServ;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/mail")
public class MailCont
{
    @Autowired
    private MailServ mailServ;

    @PostMapping
    public ResponseEntity<?> sendInvite(
            @RequestBody BookingDao bookingDao)
    {
        mailServ.sendInvite(bookingDao);
        return new ResponseEntity<>("sent", HttpStatus.OK);
    }
}
