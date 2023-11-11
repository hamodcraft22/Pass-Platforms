package polytechnic.bh.PassPlatforms_Backend.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import polytechnic.bh.PassPlatforms_Backend.Entity.Statistic;

import java.util.List;


public interface StatisticRepo extends JpaRepository<Statistic, Integer> {
    List<Statistic> findByStatID(int ID);
}
