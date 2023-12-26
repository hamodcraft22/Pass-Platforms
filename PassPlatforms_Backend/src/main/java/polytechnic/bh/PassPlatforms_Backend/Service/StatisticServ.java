package polytechnic.bh.PassPlatforms_Backend.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import polytechnic.bh.PassPlatforms_Backend.Dao.StatisticDao;
import polytechnic.bh.PassPlatforms_Backend.Entity.Statistic;
import polytechnic.bh.PassPlatforms_Backend.Repository.StatisticRepo;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class StatisticServ
{
    @Autowired
    private StatisticRepo statisticRepo;

    public StatisticDao getLatest()
    {
        Optional<Statistic> retrivedStat = statisticRepo.getLatest();

        return retrivedStat.map(StatisticDao::new).orElse(null);
    }
}
