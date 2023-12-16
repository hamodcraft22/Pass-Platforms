package polytechnic.bh.PassPlatforms_Backend.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import polytechnic.bh.PassPlatforms_Backend.Dao.StatisticDao;
import polytechnic.bh.PassPlatforms_Backend.Entity.Statistic;
import polytechnic.bh.PassPlatforms_Backend.Repository.StatisticRepo;

import java.util.ArrayList;
import java.util.List;

@Service
public class StatisticServ
{
    @Autowired
    private StatisticRepo statisticRepo;

    // get last 10 - includes for everything;
    public List<StatisticDao> getLatest10()
    {
        List<StatisticDao> statistics = new ArrayList<>();

        for(Statistic statistic : statisticRepo.getLatest10Stats())
        {
            statistics.add(new StatisticDao(statistic));
        }

        return statistics;
    }
}
