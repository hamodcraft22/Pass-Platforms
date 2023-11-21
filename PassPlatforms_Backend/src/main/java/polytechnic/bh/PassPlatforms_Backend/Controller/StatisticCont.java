package polytechnic.bh.PassPlatforms_Backend.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import polytechnic.bh.PassPlatforms_Backend.Entity.Statistic;
import polytechnic.bh.PassPlatforms_Backend.Repository.StatisticRepo;

import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/api")
public class StatisticCont
{

    @Autowired
    StatisticRepo statisticRepo;

    @GetMapping("/Stat")
    public ResponseEntity<List<Statistic>> getAllTutorials(@RequestHeader(value = "requestKey", required = false) String requestKey)
    {
        if (Objects.equals(requestKey, "student-3e1d-4e5f-a2b1-6c7d8e9f0a1b"))
        {
            return new ResponseEntity<>(statisticRepo.findAll(), HttpStatus.OK);
        }
        else
        {
            return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
        }

    }
}
