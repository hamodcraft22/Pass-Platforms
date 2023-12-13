package polytechnic.bh.PassPlatforms_Backend.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import polytechnic.bh.PassPlatforms_Backend.Dao.SlotDao;
import polytechnic.bh.PassPlatforms_Backend.Dao.UserDao;
import polytechnic.bh.PassPlatforms_Backend.Dto.GenericDto;
import polytechnic.bh.PassPlatforms_Backend.Service.SlotServ;

import java.util.List;
import java.util.Map;
import java.util.Objects;

import static polytechnic.bh.PassPlatforms_Backend.Constant.APIkeyConstant.*;

@RestController
@RequestMapping("/api/slot")
public class SlotCont
{

    @Autowired
    private SlotServ slotServ;

    // get all slots - not needed
    @GetMapping("")
    public ResponseEntity<GenericDto<List<SlotDao>>> getAllSlots(
            @RequestHeader(value = "Authorization") String requestKey)
    {
        if (Objects.equals(requestKey, MANAGER_KEY) || Objects.equals(requestKey, ADMIN_KEY) || Objects.equals(requestKey, STUDENT_KEY) || Objects.equals(requestKey, LEADER_KEY))
        {
            List<SlotDao> slots = slotServ.getAllSlots();

            if (slots != null && !slots.isEmpty())
            {
                return new ResponseEntity<>(new GenericDto<>(null, slots, null), HttpStatus.OK);
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


    // get leader slots
    @GetMapping("/leaders")
    public ResponseEntity<GenericDto<Map<UserDao, List<SlotDao>>>> getLeaderSlots(
            @RequestHeader(value = "Authorization") String requestKey,
            @RequestBody List<String> leaderIDs)
    {
        if (Objects.equals(requestKey, MANAGER_KEY) || Objects.equals(requestKey, ADMIN_KEY) || Objects.equals(requestKey, STUDENT_KEY) || Objects.equals(requestKey, LEADER_KEY))
        {
            Map<UserDao, List<SlotDao>> slots = slotServ.getAllLeaderSlots(leaderIDs);

            if (slots != null && !slots.isEmpty())
            {
                return new ResponseEntity<>(new GenericDto<>(null, slots, null), HttpStatus.OK);
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

    // get slot details - not needed
    @GetMapping("/{slotID}")
    public ResponseEntity<GenericDto<SlotDao>> getSlotDetails(
            @RequestHeader(value = "Authorization") String requestKey,
            @PathVariable("slotID") int slotID)
    {
        if (Objects.equals(requestKey, MANAGER_KEY) || Objects.equals(requestKey, ADMIN_KEY) || Objects.equals(requestKey, STUDENT_KEY) || Objects.equals(requestKey, LEADER_KEY))
        {
            SlotDao slot = slotServ.getSlotDetails(slotID);

            if (slot != null)
            {
                return new ResponseEntity<>(new GenericDto<>(null, slot, null), HttpStatus.OK);
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

    // create slot
    @PostMapping("")
    public ResponseEntity<GenericDto<SlotDao>> createSlot(
            @RequestHeader(value = "Authorization") String requestKey,
            @RequestBody SlotDao slotDao)
    {
        if (Objects.equals(requestKey, LEADER_KEY))
        {
            SlotDao createdSlot = slotServ.createSlot(
                    slotDao.getStarttime(),
                    slotDao.getEndtime(),
                    slotDao.getNote(),
                    slotDao.getSlotType().getTypeid(),
                    slotDao.getDay().getDayid(),
                    slotDao.getLeader().getUserid()
            );

            return new ResponseEntity<>(new GenericDto<>(null, createdSlot, null), HttpStatus.CREATED);
        }
        else
        {
            return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
        }
    }

    // edit slot
    @PutMapping("")
    public ResponseEntity<GenericDto<SlotDao>> editSlot(
            @RequestHeader(value = "Authorization") String requestKey,
            @RequestHeader(value = "Requester") String requisterID,
            @RequestBody SlotDao slotDao)
    {
        if (Objects.equals(requestKey, LEADER_KEY))
        {
            SlotDao editedSlots = slotServ.getSlotDetails(slotDao.getSlotid());

            if (editedSlots != null)
            {
                if (Objects.equals(editedSlots.getLeader().getUserid(), requisterID))
                {
                    return new ResponseEntity<>(new GenericDto<>(null, slotServ.editSlot(slotDao), null), HttpStatus.OK);
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

    // delete slot
    @DeleteMapping("/{slotID}")
    public ResponseEntity<GenericDto<Boolean>> deleteSlot(
            @RequestHeader(value = "Authorization") String requestKey,
            @RequestHeader(value = "Requester") String requisterID,
            @PathVariable("slotID") int slotID)
    {
        if (Objects.equals(requestKey, LEADER_KEY))
        {
            SlotDao toDeleteSlots = slotServ.getSlotDetails(slotID);

            if (toDeleteSlots != null)
            {
                if (Objects.equals(toDeleteSlots.getLeader().getUserid(), requisterID))
                {
                    return new ResponseEntity<>(new GenericDto<>(null, slotServ.deleteSlot(slotID), null), HttpStatus.OK);
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

