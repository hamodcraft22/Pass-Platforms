package polytechnic.bh.PassPlatforms_Backend.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import polytechnic.bh.PassPlatforms_Backend.Dao.StatisticDao;
import polytechnic.bh.PassPlatforms_Backend.Dto.GenericDto;
import polytechnic.bh.PassPlatforms_Backend.Service.StatisticServ;

import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/Stat")
public class StatisticCont<T>
{

    @Autowired
    StatisticServ statisticServ;

    @GetMapping("")
    public ResponseEntity<GenericDto<List<StatisticDao>>> getAllTutorials()
    {
        return new ResponseEntity<>(new GenericDto<>(null, statisticServ.getLatest10(), null, null), HttpStatus.OK);
    }
}
