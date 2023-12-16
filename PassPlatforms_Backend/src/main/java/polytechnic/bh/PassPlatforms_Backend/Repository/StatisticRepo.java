package polytechnic.bh.PassPlatforms_Backend.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import polytechnic.bh.PassPlatforms_Backend.Entity.Statistic;

import java.util.List;


public interface StatisticRepo extends JpaRepository<Statistic, Integer>
{
    @Query(value = "select * from pp_statistic order by statdate DESC FETCH first 10 rows only", nativeQuery = true)
    List<Statistic> getLatest10Stats();
}
