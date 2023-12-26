package polytechnic.bh.PassPlatforms_Backend.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import polytechnic.bh.PassPlatforms_Backend.Dao.RecommendationDao;
import polytechnic.bh.PassPlatforms_Backend.Dao.UserDao;
import polytechnic.bh.PassPlatforms_Backend.Dto.GenericDto;
import polytechnic.bh.PassPlatforms_Backend.Service.LogServ;
import polytechnic.bh.PassPlatforms_Backend.Service.RecommendationServ;
import polytechnic.bh.PassPlatforms_Backend.Service.UserServ;

import java.util.List;
import java.util.Objects;

import static polytechnic.bh.PassPlatforms_Backend.Constant.RoleConstant.*;
import static polytechnic.bh.PassPlatforms_Backend.Util.TokenValidation.isValidToken;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/recommendation")
public class RecommendationCont
{

    @Autowired
    private RecommendationServ recommendationServ;

    @Autowired
    private UserServ userServ;

    @Autowired
    private LogServ logServ;

    // get all recommendations
    @GetMapping("")
    public ResponseEntity<GenericDto<List<RecommendationDao>>> getAllRecommendations(
            @RequestHeader(value = "Authorization") String requestKey)
    {
        String userID = isValidToken(requestKey);

        try
        {
            if (userID != null)
            {
                //token is valid, get user and role
                UserDao user = userServ.getUser(userID);

                if (user.getRole().getRoleid() == ROLE_ADMIN || user.getRole().getRoleid() == ROLE_MANAGER)
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
            else
            {
                return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
            }
        }
        catch (Exception ex)
        {
            logServ.createLog(ex.getMessage(), userID);
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    // get tutor recommendations -- added | tested
    @GetMapping("/tutor/{tutorID}")
    public ResponseEntity<GenericDto<List<RecommendationDao>>> getTutorRecommendations(
            @RequestHeader(value = "Authorization") String requestKey,
            @PathVariable(value = "tutorID") String tutorID)
    {
        String userID = isValidToken(requestKey);

        try
        {
            if (userID != null)
            {
                //token is valid, get user and role
                UserDao user = userServ.getUser(userID);

                if (user.getRole().getRoleid() == ROLE_TUTOR)
                {
                    List<RecommendationDao> recommendations = recommendationServ.getTutorRecommendations(tutorID);

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
            else
            {
                return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
            }
        }
        catch (Exception ex)
        {
            logServ.createLog(ex.getMessage(), userID);
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    // get recommendation details
    @GetMapping("/{recID}")
    public ResponseEntity<GenericDto<RecommendationDao>> getRecommendationDetails(
            @RequestHeader(value = "Authorization") String requestKey,
            @PathVariable("recID") int recID)
    {
        String userID = isValidToken(requestKey);

        try
        {
            if (userID != null)
            {
                //token is valid, get user and role
                UserDao user = userServ.getUser(userID);

                if (user.getRole().getRoleid() == ROLE_ADMIN || user.getRole().getRoleid() == ROLE_MANAGER)
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
                else if (user.getRole().getRoleid() == ROLE_TUTOR)
                {
                    RecommendationDao recommendation = recommendationServ.getRecommendationDetails(recID);

                    if (recommendation != null)
                    {
                        if (Objects.equals(recommendation.getTutor().getUserid(), userID))
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
            else
            {
                return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
            }

        }
        catch (Exception ex)
        {
            logServ.createLog(ex.getMessage(), userID);
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    // create recommendation -- added | tested
    @PostMapping("")
    public ResponseEntity<GenericDto<RecommendationDao>> createRecommendation(
            @RequestHeader(value = "Authorization") String requestKey,
            @RequestBody RecommendationDao recommendationDao)
    {
        String userID = isValidToken(requestKey);

        try
        {
            if (userID != null)
            {
                //token is valid, get user and role
                UserDao user = userServ.getUser(userID);

                if (user.getRole().getRoleid() == ROLE_TUTOR)
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
            else
            {
                return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
            }

        }
        catch (Exception ex)
        {
            logServ.createLog(ex.getMessage(), userID);
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    // edit recommendation
    @PutMapping("")
    public ResponseEntity<GenericDto<RecommendationDao>> editRecommendation(
            @RequestHeader(value = "Authorization") String requestKey,
            @RequestBody RecommendationDao recommendationDao)
    {
        String userID = isValidToken(requestKey);

        try
        {
            if (userID != null)
            {
                //token is valid, get user and role
                UserDao user = userServ.getUser(userID);

                if (user.getRole().getRoleid() == ROLE_ADMIN || user.getRole().getRoleid() == ROLE_MANAGER)
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
//            else if (user.getRole().getRoleid() == ROLE_TUTOR)
//            {
//                RecommendationDao editedRecommendation = recommendationServ.getRecommendationDetails(recommendationDao.getRecid());
//
//                if (editedRecommendation != null)
//                {
//                    if (Objects.equals(editedRecommendation.getTutor().getUserid(), userID))
//                    {
//                        return new ResponseEntity<>(new GenericDto<>(null, recommendationServ.editRecommendation(recommendationDao), null, null), HttpStatus.OK);
//                    }
//                    else
//                    {
//                        return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
//                    }
//                }
//                else
//                {
//                    return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
//                }
//            }
                else
                {
                    return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
                }
            }
            else
            {
                return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
            }
        }
        catch (Exception ex)
        {
            logServ.createLog(ex.getMessage(), userID);
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    // delete recommendation -- added | tested
    @DeleteMapping("/{recID}")
    public ResponseEntity<GenericDto<Void>> deleteRecommendation(
            @RequestHeader(value = "Authorization") String requestKey,
            @PathVariable("recID") int recID)
    {
        String userID = isValidToken(requestKey);

        try
        {
            if (userID != null)
            {
                //token is valid, get user and role
                UserDao user = userServ.getUser(userID);

                if (user.getRole().getRoleid() == ROLE_ADMIN || user.getRole().getRoleid() == ROLE_MANAGER)
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
                else if (user.getRole().getRoleid() == ROLE_TUTOR)
                {
                    RecommendationDao editedRecommendation = recommendationServ.getRecommendationDetails(recID);

                    if (editedRecommendation != null)
                    {
                        if (Objects.equals(editedRecommendation.getTutor().getUserid(), userID))
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
            else
            {
                return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
            }
        }
        catch (Exception ex)
        {
            logServ.createLog(ex.getMessage(), userID);
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }
}
