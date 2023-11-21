package polytechnic.bh.PassPlatforms_Backend.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import polytechnic.bh.PassPlatforms_Backend.Dao.RecommendationDao;
import polytechnic.bh.PassPlatforms_Backend.Entity.Recommendation;
import polytechnic.bh.PassPlatforms_Backend.Repository.RecStatusRepo;
import polytechnic.bh.PassPlatforms_Backend.Repository.RecommendationRepo;
import polytechnic.bh.PassPlatforms_Backend.Repository.UserRepo;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class RecommendationServ
{

    @Autowired
    private RecommendationRepo recommendationRepo;

    @Autowired
    private RecStatusRepo recStatusRepo;

    @Autowired
    private UserRepo userRepo;

    public List<RecommendationDao> getAllRecommendations()
    {
        List<RecommendationDao> recommendations = new ArrayList<>();

        for (Recommendation retrievedRecommendation : recommendationRepo.findAll())
        {
            recommendations.add(new RecommendationDao(retrievedRecommendation));
        }

        return recommendations;
    }

    public RecommendationDao getRecommendationDetails(int recID)
    {
        Optional<Recommendation> retrievedRecommendation = recommendationRepo.findById(recID);

        return retrievedRecommendation.map(RecommendationDao::new).orElse(null);
    }

    public RecommendationDao createRecommendation(Instant datetime, String note, char recStatusID, String tutorID, String studentID)
    {
        Recommendation newRecommendation = new Recommendation();

        newRecommendation.setDatetime(Timestamp.from(datetime));
        newRecommendation.setNote(note);
        newRecommendation.setStatus(recStatusRepo.getReferenceById(recStatusID));
        newRecommendation.setTutor(userRepo.getReferenceById(tutorID));
        newRecommendation.setStudent(userRepo.getReferenceById(studentID));

        return new RecommendationDao(recommendationRepo.save(newRecommendation));
    }

    public RecommendationDao editRecommendation(RecommendationDao updatedRecommendation)
    {
        Optional<Recommendation> retrievedRecommendation = recommendationRepo.findById(updatedRecommendation.getRecid());

        if (retrievedRecommendation.isPresent())
        {
            retrievedRecommendation.get().setDatetime(Timestamp.from(updatedRecommendation.getDatetime()));
            retrievedRecommendation.get().setNote(updatedRecommendation.getNote());
            retrievedRecommendation.get().setStatus(recStatusRepo.getReferenceById(updatedRecommendation.getRecStatus().getStatusid()));

            return new RecommendationDao(recommendationRepo.save(retrievedRecommendation.get()));
        }
        else
        {
            return null;
        }
    }

    public boolean deleteRecommendation(int recID)
    {
        recommendationRepo.deleteById(recID);
        return true;
    }
}

