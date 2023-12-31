package polytechnic.bh.PassPlatforms_Backend.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import polytechnic.bh.PassPlatforms_Backend.Dao.DayDao;
import polytechnic.bh.PassPlatforms_Backend.Dao.SlotDao;
import polytechnic.bh.PassPlatforms_Backend.Dao.SlotTypeDao;
import polytechnic.bh.PassPlatforms_Backend.Dao.UserDao;
import polytechnic.bh.PassPlatforms_Backend.Dto.LeadersSlotsDto;
import polytechnic.bh.PassPlatforms_Backend.Entity.Slot;
import polytechnic.bh.PassPlatforms_Backend.Entity.User;
import polytechnic.bh.PassPlatforms_Backend.Repository.BookingRepo;
import polytechnic.bh.PassPlatforms_Backend.Repository.DayRepo;
import polytechnic.bh.PassPlatforms_Backend.Repository.SlotRepo;
import polytechnic.bh.PassPlatforms_Backend.Repository.SlotTypeRepo;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static polytechnic.bh.PassPlatforms_Backend.Util.UsersService.getAzureAdName;

@Service
public class SlotServ
{

    @Autowired
    private SlotRepo slotRepo;

    @Autowired
    private DayRepo dayRepo;

    @Autowired
    private BookingRepo bookingRepo;

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
    public List<SlotDao> getAllLeaderSlots(String LeaderID)
    {
        List<SlotDao> slots = new ArrayList<>();

        for (Slot retrievedSlot : slotRepo.findSlotsByLeader_Userid(LeaderID))
        {
            slots.add(new SlotDao(retrievedSlot.getSlotid(), retrievedSlot.getStarttime().toInstant(), retrievedSlot.getEndtime().toInstant(), retrievedSlot.getNote(), new SlotTypeDao(retrievedSlot.getSlotType()), new DayDao(retrievedSlot.getDay()), new UserDao(retrievedSlot.getLeader().getUserid(), null, getAzureAdName(retrievedSlot.getLeader().getUserid()), null)));
        }

        return slots;
    }


    // get slots based on a list of leaders -- tested
    public List<LeadersSlotsDto> getAllLeadersSlots(List<String> userIDs)
    {
        List<LeadersSlotsDto> leaderSlots = new ArrayList<>();

        for (String userID : userIDs)
        {
            UserDao retrievedUser = userServ.getUser(userID);

            if (retrievedUser.getRole().getRoleid() == 2)
            {
                List<SlotDao> slots = new ArrayList<>();

                for (Slot retrievedSlot : slotRepo.findSlotsByLeader_Userid(userID))
                {
                    slots.add(new SlotDao(retrievedSlot.getSlotid(), retrievedSlot.getStarttime().toInstant(), retrievedSlot.getEndtime().toInstant(), retrievedSlot.getNote(), new SlotTypeDao(retrievedSlot.getSlotType()), new DayDao(retrievedSlot.getDay()), null));
                }

                LeadersSlotsDto leadersSlotsDto = new LeadersSlotsDto(userID, getAzureAdName(userID), slots);

                leaderSlots.add(leadersSlotsDto);
            }

        }

        return leaderSlots;
    }

    // get available slots by date range
    public List<LeadersSlotsDto> getAvailableLeaderSlots(List<LeadersSlotsDto> leaderSlots, Date weekStartDate)
    {
        List<LeadersSlotsDto> correctedLeaderSlots = new ArrayList<>();

        for (LeadersSlotsDto leadersSlotsDto : leaderSlots)
        {
            // slots to include
            List<SlotDao> slotsToInclude = new ArrayList<>();

            // inner lop for slots
            for (SlotDao slotDao : leadersSlotsDto.getSlots())
            {
                Date slotDateToWeek = switch (slotDao.getDay().getDayid())
                {
                    // get the date (respective to the start / end date)

                    case 'U' -> weekStartDate;
                    case 'M' -> Date.from(weekStartDate.toInstant().plusSeconds(86400));
                    case 'T' -> Date.from(weekStartDate.toInstant().plusSeconds(172800));
                    case 'W' -> Date.from(weekStartDate.toInstant().plusSeconds(259200));
                    case 'R' -> Date.from(weekStartDate.toInstant().plusSeconds(345600));
                    case 'F' -> Date.from(weekStartDate.toInstant().plusSeconds(432000));
                    case 'S' -> Date.from(weekStartDate.toInstant().plusSeconds(518400));
                    default -> null;
                };

                // check db against date, if there is any active booking where date = gotten date and slot = gotten slot and status active
                if (slotDateToWeek != null)
                {
                    if (bookingRepo.activeUnderSlot(slotDao.getSlotid(), slotDateToWeek) == 0)
                    {
                        slotsToInclude.add(slotDao);
                    }
                }

            }

            if (!slotsToInclude.isEmpty())
            {
                LeadersSlotsDto correctedleadersSlotsDto = new LeadersSlotsDto(leadersSlotsDto.getLeaderID(), leadersSlotsDto.getLeaderName(), slotsToInclude);
                correctedLeaderSlots.add(correctedleadersSlotsDto);
            }
        }

        return correctedLeaderSlots;
    }

    // get course slots
    public List<LeadersSlotsDto> getCourseSlots(String courseID, Date weekStartDate)
    {
        // find all users that teach the course
        List<UserDao> courseLeaders = userServ.courseLeaders(courseID);

        List<String> leaderIDs = new ArrayList<>();
        for (UserDao leader : courseLeaders)
        {
            leaderIDs.add(leader.getUserid());
        }

        // get all of their slots
        List<LeadersSlotsDto> allLeadersSlots = getAllLeadersSlots(leaderIDs);

        // filter slots and return
        return getAvailableLeaderSlots(allLeadersSlots, weekStartDate);
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

        if (slotRepo.sameSlotTimeFind(leaderID, dayID, Timestamp.from(startTime), Timestamp.from(endTime)) != 0)
        {
            return null;
        }
        else
        {
            newSlot.setStarttime(Timestamp.from(startTime));
            newSlot.setEndtime(Timestamp.from(endTime));
            newSlot.setNote(note);
            newSlot.setSlotType(slotTypeRepo.getReferenceById(typeID));
            newSlot.setDay(dayRepo.getReferenceById(dayID));
            newSlot.setLeader(new User(userServ.getUser(leaderID)));

            return new SlotDao(slotRepo.save(newSlot));
        }
    }

    // edit slot - available before starting services
    public SlotDao editSlot(SlotDao gottenSlot)
    {
        Optional<Slot> retrivedSlot = slotRepo.findById(gottenSlot.getSlotid());

        if (retrivedSlot.isPresent())
        {
            // retrivedSlot.get().setStarttime(Timestamp.from(gottenSlot.getStarttime()));
            // retrivedSlot.get().setEndtime(Timestamp.from(gottenSlot.getEndtime()));
            retrivedSlot.get().setNote(gottenSlot.getNote());
            retrivedSlot.get().setSlotType(slotTypeRepo.getReferenceById(gottenSlot.getSlotType().getTypeid()));
            // retrivedSlot.get().setDay(dayRepo.getReferenceById(gottenSlot.getDay().getDayid()));

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


