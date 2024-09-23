package polytechnic.bh.PassPlatforms_Backend.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import polytechnic.bh.PassPlatforms_Backend.Entity.Statistic;

import java.util.List;
import java.util.Optional;


public interface StatisticRepo extends JpaRepository<Statistic, Integer>
{
    @Query(value = "select * from pp_statistic order by statdate DESC FETCH first 10 rows only", nativeQuery = true)
    List<Statistic> getLatest10Stats();

    @Query(value = "select * from pp_statistic order by STATID DESC FETCH first 1 rows only", nativeQuery = true)
    Optional<Statistic> getLatest();

    @Procedure(procedureName = "fetch_stats")
    void callStats();
}
