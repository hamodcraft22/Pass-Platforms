package polytechnic.bh.PassPlatforms_Backend.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import polytechnic.bh.PassPlatforms_Backend.Dao.*;
import polytechnic.bh.PassPlatforms_Backend.Dto.LeadersSlotsDto;
import polytechnic.bh.PassPlatforms_Backend.Entity.Slot;
import polytechnic.bh.PassPlatforms_Backend.Entity.User;
import polytechnic.bh.PassPlatforms_Backend.Repository.DayRepo;
import polytechnic.bh.PassPlatforms_Backend.Repository.SlotRepo;
import polytechnic.bh.PassPlatforms_Backend.Repository.SlotTypeRepo;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.*;

@Service
public class SlotServ
{

    @Autowired
    private SlotRepo slotRepo;

    @Autowired
    private DayRepo dayRepo;

    @Autowired
    private UserServ userServ;

    @Autowired
    private SlotTypeRepo slotTypeRepo;

    // get all slots - not needed (to get all slots saved in the system)
    public List<SlotDao> getAllSlots()
    {
        List<SlotDao> slots = new ArrayList<>();

        for (Slot retrievedSlot : slotRepo.findAll())
        {
            slots.add(new SlotDao(retrievedSlot));
        }

        return slots;
    }

    // get slots based on a list of leaders (or a single leader)
    public List<LeadersSlotsDto> getAllLeaderSlots(List<String> userIDs)
    {
        List<LeadersSlotsDto>  leaderSlots = new ArrayList<>();

        for (String userID : userIDs)
        {
            User retrivedUser = userServ.getUser(userID);
            if (retrivedUser.getRole().getRoleid() == 2)
            {
                List<SlotDao> slots = new ArrayList<>();

                for (Slot retrievedSlot : slotRepo.findSlotsByLeader(retrivedUser))
                {
                    slots.add(new SlotDao(retrievedSlot.getSlotid(), retrievedSlot.getStarttime().toInstant(), retrievedSlot.getEndtime().toInstant(), retrievedSlot.getNote(), new SlotTypeDao(retrievedSlot.getSlotType()), new DayDao(retrievedSlot.getDay()), null));
                }

                LeadersSlotsDto leadersSlotsDto = new LeadersSlotsDto(userID, "actualname", slots);

                leaderSlots.add(leadersSlotsDto);
            }

        }

        return leaderSlots;
    }

    // get a single slot and its details - also not needed
    public SlotDao getSlotDetails(int slotID)
    {
        Optional<Slot> retrievedSlot = slotRepo.findById(slotID);

        return retrievedSlot.map(SlotDao::new).orElse(null);
    }

    // creating new slot
    public SlotDao createSlot(Instant startTime, Instant endTime, String note, char typeID, char dayID, String leaderID)
    {
        Slot newSlot = new Slot();

        newSlot.setStarttime(Timestamp.from(startTime));
        newSlot.setEndtime(Timestamp.from(endTime));
        newSlot.setNote(note);
        newSlot.setSlotType(slotTypeRepo.getReferenceById(typeID));
        newSlot.setDay(dayRepo.getReferenceById(dayID));
        newSlot.setLeader(userServ.getUser(leaderID));

        return new SlotDao(slotRepo.save(newSlot));
    }

    // edit slot - available before starting services
    public SlotDao editSlot(SlotDao gottenSlot)
    {
        Optional<Slot> retrivedSlot = slotRepo.findById(gottenSlot.getSlotid());

        if (retrivedSlot.isPresent())
        {
            retrivedSlot.get().setStarttime(Timestamp.from(gottenSlot.getStarttime()));
            retrivedSlot.get().setEndtime(Timestamp.from(gottenSlot.getEndtime()));
            retrivedSlot.get().setNote(gottenSlot.getNote());
            retrivedSlot.get().setSlotType(slotTypeRepo.getReferenceById(gottenSlot.getSlotType().getTypeid()));
            retrivedSlot.get().setDay(dayRepo.getReferenceById(gottenSlot.getDay().getDayid()));

            return new SlotDao(slotRepo.save(retrivedSlot.get()));
        }
        else
        {
            return null;
        }
    }

    // delete slot - available before staring services
    public boolean deleteSlot(int slotID)
    {
        slotRepo.deleteById(slotID);
        return true;
    }
}


