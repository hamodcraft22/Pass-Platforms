package polytechnic.bh.PassPlatforms_Backend.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import polytechnic.bh.PassPlatforms_Backend.Dao.RecommendationDao;
import polytechnic.bh.PassPlatforms_Backend.Dto.GenericDto;
import polytechnic.bh.PassPlatforms_Backend.Service.RecommendationServ;

import java.util.List;
import java.util.Objects;

import static polytechnic.bh.PassPlatforms_Backend.Constant.APIkeyConstant.*;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/recommendation")
public class RecommendationCont
{

    @Autowired
    private RecommendationServ recommendationServ;

    // get all recommendations
    @GetMapping("")
    public ResponseEntity<GenericDto<List<RecommendationDao>>> getAllRecommendations(
            @RequestHeader(value = "Authorization") String requestKey)
    {

        if (Objects.equals(requestKey, ADMIN_KEY) || Objects.equals(requestKey, MANAGER_KEY))
        {
            List<RecommendationDao> recommendations = recommendationServ.getAllRecommendations();

            if (recommendations != null && !recommendations.isEmpty())
            {
                return new ResponseEntity<>(new GenericDto<>(null, recommendations, null, null), HttpStatus.OK);
            }
            else
            {
                return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
            }
        }
        else
        {
            return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
        }
    }

    // get recommendation details
    @GetMapping("/{recID}")
    public ResponseEntity<GenericDto<RecommendationDao>> getRecommendationDetails(
            @RequestHeader(value = "Authorization") String requestKey,
            @RequestHeader(value = "Requester") String requisterID,
            @PathVariable("recID") int recID)
    {
        if (Objects.equals(requestKey, ADMIN_KEY) || Objects.equals(requestKey, MANAGER_KEY))
        {
            RecommendationDao recommendation = recommendationServ.getRecommendationDetails(recID);

            if (recommendation != null)
            {
                return new ResponseEntity<>(new GenericDto<>(null, recommendation, null, null), HttpStatus.OK);
            }
            else
            {
                return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
            }
        }
        else if (Objects.equals(requestKey, TUTOR_KEY))
        {
            RecommendationDao recommendation = recommendationServ.getRecommendationDetails(recID);

            if (recommendation != null)
            {
                if (Objects.equals(recommendation.getTutor().getUserid(), requisterID))
                {
                    return new ResponseEntity<>(new GenericDto<>(null, recommendation, null, null), HttpStatus.OK);
                }
                else
                {
                    return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
                }
            }
            else
            {
                return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
            }
        }
        else
        {
            return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
        }
    }

    // create recommendation
    @PostMapping("")
    public ResponseEntity<GenericDto<RecommendationDao>> createRecommendation(
            @RequestHeader(value = "Authorization") String requestKey,
            @RequestBody RecommendationDao recommendationDao)
    {
        if (Objects.equals(requestKey, TUTOR_KEY))
        {
            RecommendationDao createdRecommendation = recommendationServ.createRecommendation(
                    recommendationDao.getDatetime(),
                    recommendationDao.getNote(),
                    recommendationDao.getTutor().getUserid(),
                    recommendationDao.getStudent().getUserid()
            );

            return new ResponseEntity<>(new GenericDto<>(null, createdRecommendation, null, null), HttpStatus.CREATED);
        }
        else
        {
            return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
        }
    }

    // edit recommendation
    @PutMapping("")
    public ResponseEntity<GenericDto<RecommendationDao>> editRecommendation(
            @RequestHeader(value = "Authorization") String requestKey,
            @RequestHeader(value = "Requester") String requisterID,
            @RequestBody RecommendationDao recommendationDao)
    {
        if (Objects.equals(requestKey, ADMIN_KEY) || Objects.equals(requestKey, MANAGER_KEY))
        {
            RecommendationDao editedRecommendation = recommendationServ.editRecommendation(recommendationDao);

            if (editedRecommendation != null)
            {
                return new ResponseEntity<>(new GenericDto<>(null, editedRecommendation, null, null), HttpStatus.OK);
            }
            else
            {
                return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
            }
        }
        else if (Objects.equals(requestKey, TUTOR_KEY))
        {
            RecommendationDao editedRecommendation = recommendationServ.getRecommendationDetails(recommendationDao.getRecid());

            if (editedRecommendation != null)
            {
                if (Objects.equals(editedRecommendation.getTutor().getUserid(), requisterID))
                {
                    return new ResponseEntity<>(new GenericDto<>(null, recommendationServ.editRecommendation(recommendationDao), null, null), HttpStatus.OK);
                }
                else
                {
                    return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
                }
            }
            else
            {
                return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
            }
        }
        else
        {
            return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
        }
    }

    // delete recommendation
    @DeleteMapping("/{recID}")
    public ResponseEntity<GenericDto<Void>> deleteRecommendation(
            @RequestHeader(value = "Authorization") String requestKey,
            @RequestHeader(value = "Requester") String requisterID,
            @PathVariable("recID") int recID)
    {
        if (Objects.equals(requestKey, ADMIN_KEY) || Objects.equals(requestKey, MANAGER_KEY))
        {
            if (recommendationServ.deleteRecommendation(recID))
            {
                return new ResponseEntity<>(null, HttpStatus.OK);
            }
            else
            {
                return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
            }
        }
        else if (Objects.equals(requestKey, TUTOR_KEY))
        {
            RecommendationDao editedRecommendation = recommendationServ.getRecommendationDetails(recID);

            if (editedRecommendation != null)
            {
                if (Objects.equals(editedRecommendation.getTutor().getUserid(), requisterID))
                {
                    if (recommendationServ.deleteRecommendation(recID))
                    {
                        return new ResponseEntity<>(null, HttpStatus.OK);
                    }
                    else
                    {
                        return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
                    }
                }
                else
                {
                    return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
                }
            }
            else
            {
                return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
            }
        }
        else
        {
            return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
        }
    }
}
