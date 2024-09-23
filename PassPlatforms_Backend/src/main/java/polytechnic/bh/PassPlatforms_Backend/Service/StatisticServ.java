package polytechnic.bh.PassPlatforms_Backend.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import polytechnic.bh.PassPlatforms_Backend.Dao.StatisticDao;
import polytechnic.bh.PassPlatforms_Backend.Entity.Statistic;
import polytechnic.bh.PassPlatforms_Backend.Repository.StatisticRepo;

import java.util.Optional;

@Service
public class StatisticServ
{
    @Autowired
    private StatisticRepo statisticRepo;

    // get the latest stat from the db
    public StatisticDao getLatest()
    {
        Optional<Statistic> retrivedStat = statisticRepo.getLatest();

        return retrivedStat.map(StatisticDao::new).orElse(null);
    }

    // call the stats refresh daily at 8
    @Scheduled(cron = "0 0 20 * * *")
    public void refreshStats()
    {
        System.out.println("called refresh");
        statisticRepo.callStats();
    }
}
