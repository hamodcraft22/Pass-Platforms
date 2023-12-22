package polytechnic.bh.PassPlatforms_Backend.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import polytechnic.bh.PassPlatforms_Backend.Dao.StatisticDao;
import polytechnic.bh.PassPlatforms_Backend.Dto.GenericDto;
import polytechnic.bh.PassPlatforms_Backend.Service.StatisticServ;

import java.util.List;

import static polytechnic.bh.PassPlatforms_Backend.Util.TokenValidation.isValidToken;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/Stat")
public class StatisticCont<T>
{

    @Autowired
    StatisticServ statisticServ;

    @GetMapping("")
    public ResponseEntity<GenericDto<List<StatisticDao>>> getAllStats(
            @RequestHeader(value = "Authorization") String requestKey
    )
    {
        String userID = isValidToken(requestKey);

        if (userID != null)
        {
            return new ResponseEntity<>(new GenericDto<>(null, statisticServ.getLatest10(), null, null), HttpStatus.OK);
        }
        else
        {
            return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
        }

    }
}
