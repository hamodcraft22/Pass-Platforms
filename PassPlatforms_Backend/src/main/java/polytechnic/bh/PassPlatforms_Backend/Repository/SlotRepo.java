package polytechnic.bh.PassPlatforms_Backend.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import polytechnic.bh.PassPlatforms_Backend.Entity.Day;
import polytechnic.bh.PassPlatforms_Backend.Entity.Slot;
import polytechnic.bh.PassPlatforms_Backend.Entity.User;

import java.util.Date;
import java.util.List;

public interface SlotRepo extends JpaRepository<Slot, Integer>
{
    List<Slot> findSlotsByStarttime(Date date);

    List<Slot> findSlotsByEndtime(Date date);

    List<Slot> findSlotsByNoteContainsIgnoreCase(String note);

    List<Slot> findSlotsByIsrevision(Boolean isrevision);

    List<Slot> findSlotsByIsonline(Boolean isonline);

    List<Slot> findSlotsByDay(Day day);

    List<Slot> findSlotsByLeader(User leader);
}
