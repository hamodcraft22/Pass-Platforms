package polytechnic.bh.PassPlatforms_Backend.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import polytechnic.bh.PassPlatforms_Backend.Entity.Recommendation;

import java.util.Date;
import java.util.List;

public interface RecommendationRepo extends JpaRepository<Recommendation, Integer>
{
    List<Recommendation> findRecommendationsByDatetime(Date date);

    List<Recommendation> findRecommendationsByNoteContainsIgnoreCase(String note);
}
