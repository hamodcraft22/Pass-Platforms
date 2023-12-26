package polytechnic.bh.PassPlatforms_Backend.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import polytechnic.bh.PassPlatforms_Backend.Dao.SlotDao;
import polytechnic.bh.PassPlatforms_Backend.Dao.UserDao;
import polytechnic.bh.PassPlatforms_Backend.Dto.GenericDto;
import polytechnic.bh.PassPlatforms_Backend.Dto.LeadersSlotsDto;
import polytechnic.bh.PassPlatforms_Backend.Service.LogServ;
import polytechnic.bh.PassPlatforms_Backend.Service.SlotServ;
import polytechnic.bh.PassPlatforms_Backend.Service.UserServ;

import java.util.Date;
import java.util.List;
import java.util.Objects;

import static polytechnic.bh.PassPlatforms_Backend.Constant.RoleConstant.ROLE_LEADER;
import static polytechnic.bh.PassPlatforms_Backend.Util.TokenValidation.isValidToken;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/slot")
public class SlotCont
{

    @Autowired
    private SlotServ slotServ;

    @Autowired
    private UserServ userServ;

    @Autowired
    private LogServ logServ;

    // get all slots - not needed
    @GetMapping("")
    public ResponseEntity<GenericDto<List<SlotDao>>> getAllSlots(
            @RequestHeader(value = "Authorization") String requestKey)
    {
        String userID = isValidToken(requestKey);

        try
        {
            if (userID != null)
            {

                List<SlotDao> slots = slotServ.getAllSlots();

                if (slots != null && !slots.isEmpty())
                {
                    return new ResponseEntity<>(new GenericDto<>(null, slots, null, null), HttpStatus.OK);
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
        catch (Exception ex)
        {
            logServ.createLog(ex.getMessage(), userID);
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }

    }


    // get leaders slots -- not sure if will be used
    @GetMapping("/leaders")
    public ResponseEntity<GenericDto<List<LeadersSlotsDto>>> getLeadersSlots(
            @RequestHeader(value = "Authorization") String requestKey,
            @RequestBody List<String> leaderIDs)
    {
        String userID = isValidToken(requestKey);

        try
        {
            if (userID != null)
            {

                List<LeadersSlotsDto> slots = slotServ.getAllLeadersSlots(leaderIDs);

                if (slots != null && !slots.isEmpty())
                {
                    return new ResponseEntity<>(new GenericDto<>(null, slots, null, null), HttpStatus.OK);
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
        catch (Exception ex)
        {
            logServ.createLog(ex.getMessage(), userID);
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }


    // get leader slots -- added | tested
    @GetMapping("/leader/{leaderID}")
    public ResponseEntity<GenericDto<List<SlotDao>>> getLeaderSlots(
            @RequestHeader(value = "Authorization") String requestKey,
            @PathVariable(value = "leaderID") String leaderID)
    {
        String userID = isValidToken(requestKey);

        try
        {
            if (userID != null)
            {
                List<SlotDao> slots = slotServ.getAllLeaderSlots(leaderID);

                if (slots != null && !slots.isEmpty())
                {
                    return new ResponseEntity<>(new GenericDto<>(null, slots, null, null), HttpStatus.OK);
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
        catch (Exception ex)
        {
            logServ.createLog(ex.getMessage(), userID);
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }

    }

    // get open leader slots for a course -- tested | added
    @GetMapping("/course/{courseID}")
    public ResponseEntity<GenericDto<List<LeadersSlotsDto>>> getCourseSlots(
            @RequestHeader(value = "Authorization") String requestKey,
            @PathVariable(value = "courseID") String courseID,
            @RequestParam(value = "weekStart") Date weekStart)
    {
        String userID = isValidToken(requestKey);

        try
        {
            if (userID != null)
            {

                List<LeadersSlotsDto> slots = slotServ.getCourseSlots(courseID, weekStart);

                if (slots != null && !slots.isEmpty())
                {
                    return new ResponseEntity<>(new GenericDto<>(null, slots, null, null), HttpStatus.OK);
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
        catch (Exception ex)
        {
            logServ.createLog(ex.getMessage(), userID);
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }


    // get slot details - not needed
    @GetMapping("/{slotID}")
    public ResponseEntity<GenericDto<SlotDao>> getSlotDetails(
            @RequestHeader(value = "Authorization") String requestKey,
            @PathVariable("slotID") int slotID)
    {
        String userID = isValidToken(requestKey);

        try
        {
            if (userID != null)
            {

                SlotDao slot = slotServ.getSlotDetails(slotID);

                if (slot != null)
                {
                    return new ResponseEntity<>(new GenericDto<>(null, slot, null, null), HttpStatus.OK);
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
        catch (Exception ex)
        {
            logServ.createLog(ex.getMessage(), userID);
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }

    }

    // create slot -- added | tested
    @PostMapping("")
    public ResponseEntity<GenericDto<SlotDao>> createSlot(
            @RequestHeader(value = "Authorization") String requestKey,
            @RequestBody SlotDao slotDao)
    {
        String userID = isValidToken(requestKey);

        try
        {
            if (userID != null)
            {
                //token is valid, get user and role
                UserDao user = userServ.getUser(userID);

                if (user.getRole().getRoleid() == ROLE_LEADER)
                {
                    SlotDao createdSlot = slotServ.createSlot(
                            slotDao.getStarttime(),
                            slotDao.getEndtime(),
                            slotDao.getNote(),
                            slotDao.getSlotType().getTypeid(),
                            slotDao.getDay().getDayid(),
                            slotDao.getLeader().getUserid()
                    );

                    if (createdSlot != null)
                    {
                        return new ResponseEntity<>(new GenericDto<>(null, createdSlot, null, null), HttpStatus.CREATED);
                    }
                    else
                    {
                        return new ResponseEntity<>(new GenericDto<>(null, null, List.of("Slot clashes with another slot time"), null), HttpStatus.BAD_REQUEST);
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

    // edit slot -- added | tested
    @PutMapping("")
    public ResponseEntity<GenericDto<SlotDao>> editSlot(
            @RequestHeader(value = "Authorization") String requestKey,
            @RequestBody SlotDao slotDao)
    {
        String userID = isValidToken(requestKey);

        try
        {
            if (userID != null)
            {
                //token is valid, get user and role
                UserDao user = userServ.getUser(userID);

                if (user.getRole().getRoleid() == ROLE_LEADER)
                {
                    SlotDao editedSlots = slotServ.getSlotDetails(slotDao.getSlotid());

                    if (editedSlots != null)
                    {
                        if (Objects.equals(editedSlots.getLeader().getUserid(), userID))
                        {
                            return new ResponseEntity<>(new GenericDto<>(null, slotServ.editSlot(slotDao), null, null), HttpStatus.OK);
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

    // delete slot -- added | tested
    @DeleteMapping("/{slotID}")
    public ResponseEntity<GenericDto<Boolean>> deleteSlot(
            @RequestHeader(value = "Authorization") String requestKey,
            @PathVariable("slotID") int slotID)
    {
        String userID = isValidToken(requestKey);

        try
        {
            if (userID != null)
            {
                //token is valid, get user and role
                UserDao user = userServ.getUser(userID);

                if (user.getRole().getRoleid() == ROLE_LEADER)
                {
                    SlotDao toDeleteSlots = slotServ.getSlotDetails(slotID);

                    if (toDeleteSlots != null)
                    {
                        if (Objects.equals(toDeleteSlots.getLeader().getUserid(), userID))
                        {
                            return new ResponseEntity<>(new GenericDto<>(null, slotServ.deleteSlot(slotID), null, null), HttpStatus.OK);
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

