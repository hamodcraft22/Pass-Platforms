package polytechnic.bh.PassPlatforms_Backend.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import polytechnic.bh.PassPlatforms_Backend.Dao.RecommendationDao;
import polytechnic.bh.PassPlatforms_Backend.Entity.Notification;
import polytechnic.bh.PassPlatforms_Backend.Entity.Recommendation;
import polytechnic.bh.PassPlatforms_Backend.Entity.User;
import polytechnic.bh.PassPlatforms_Backend.Repository.NotificationRepo;
import polytechnic.bh.PassPlatforms_Backend.Repository.RecStatusRepo;
import polytechnic.bh.PassPlatforms_Backend.Repository.RecommendationRepo;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static polytechnic.bh.PassPlatforms_Backend.Constant.ManagerConst.MANGER_ID;
import static polytechnic.bh.PassPlatforms_Backend.Constant.RecommendationStatusConstant.RECSTS_CREATED;

@Service
public class RecommendationServ
{

    @Autowired
    private RecommendationRepo recommendationRepo;

    @Autowired
    private RecStatusRepo recStatusRepo;

    @Autowired
    private UserServ userServ;

    @Autowired
    private NotificationRepo notificationRepo;

    public List<RecommendationDao> getAllRecommendations()
    {
        List<RecommendationDao> recommendations = new ArrayList<>();

        for (Recommendation retrievedRecommendation : recommendationRepo.findAll())
        {
            recommendations.add(new RecommendationDao(retrievedRecommendation));
        }

        return recommendations;
    }

    public List<RecommendationDao> getTutorRecommendations(String tutorID)
    {
        List<RecommendationDao> recommendations = new ArrayList<>();

        for (Recommendation retrievedRecommendation : recommendationRepo.findAllByTutor_Userid(tutorID))
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

    public RecommendationDao createRecommendation(Instant datetime, String note, String tutorID, String studentID)
    {
        Recommendation newRecommendation = new Recommendation();

        newRecommendation.setDatetime(Timestamp.from(datetime));
        newRecommendation.setNote(note);
        newRecommendation.setStatus(recStatusRepo.getReferenceById(RECSTS_CREATED));
        newRecommendation.setTutor(new User(userServ.getUser(tutorID)));
        newRecommendation.setStudent(new User(userServ.getUser(studentID)));

        Recommendation savedRec = recommendationRepo.save(newRecommendation);

        // notify manager
        Notification newNotification = new Notification();
        newNotification.setEntity("Recommendation");
        newNotification.setItemid(String.valueOf(savedRec.getRecid()));
        newNotification.setNotficmsg("new recommendation by tutor");
        newNotification.setUser(new User(userServ.getUser(MANGER_ID)));
        newNotification.setSeen(false);

        notificationRepo.save(newNotification);

        return new RecommendationDao(savedRec);
    }

    public RecommendationDao editRecommendation(RecommendationDao updatedRecommendation)
    {
        Optional<Recommendation> retrievedRecommendation = recommendationRepo.findById(updatedRecommendation.getRecid());

        if (retrievedRecommendation.isPresent())
        {
            // retrievedRecommendation.get().setDatetime(Timestamp.from(updatedRecommendation.getDatetime()));
            // retrievedRecommendation.get().setNote(updatedRecommendation.getNote());
            retrievedRecommendation.get().setStatus(recStatusRepo.getReferenceById(updatedRecommendation.getRecStatus().getStatusid()));

            // notify tutor
            Notification newNotification = new Notification();
            newNotification.setEntity("Recommendation");
            newNotification.setItemid(String.valueOf(retrievedRecommendation.get().getRecid()));
            newNotification.setNotficmsg("recommendation stats updated");
            newNotification.setUser(retrievedRecommendation.get().getTutor());
            newNotification.setSeen(false);

            notificationRepo.save(newNotification);

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

