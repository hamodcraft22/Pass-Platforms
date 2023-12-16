package polytechnic.bh.PassPlatforms_Backend.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import polytechnic.bh.PassPlatforms_Backend.Dao.ApplicationNoteDao;
import polytechnic.bh.PassPlatforms_Backend.Dao.RoleDao;
import polytechnic.bh.PassPlatforms_Backend.Dao.UserDao;
import polytechnic.bh.PassPlatforms_Backend.Entity.Application;
import polytechnic.bh.PassPlatforms_Backend.Entity.ApplicationNote;
import polytechnic.bh.PassPlatforms_Backend.Entity.Notification;
import polytechnic.bh.PassPlatforms_Backend.Repository.ApplicationNoteRepo;
import polytechnic.bh.PassPlatforms_Backend.Repository.ApplicationRepo;
import polytechnic.bh.PassPlatforms_Backend.Repository.NotificationRepo;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class ApplicationNoteServ
{

    @Autowired
    private ApplicationRepo applicationRepo;

    @Autowired
    private ApplicationNoteRepo applicationNoteRepo;

    @Autowired
    private UserServ userServ;

    @Autowired
    private NotificationRepo notificationRepo;


    // get all application Notes - not needed
    public List<ApplicationNoteDao> getAllAplicationNotes(Boolean details)
    {
        List<ApplicationNoteDao> applicationNotes = new ArrayList<>();

        if (!details)
        {
            for (ApplicationNote retrivedNote : applicationNoteRepo.findAll())
            {
                applicationNotes.add(new ApplicationNoteDao(
                        retrivedNote.getNoteid(),
                        retrivedNote.getDatetime().toInstant(),
                        retrivedNote.getNotebody(),
                        null,
                        new UserDao(retrivedNote.getUser().getUserid(), new RoleDao(retrivedNote.getUser().getRole()), null)
                ));
            }
        }
        else
        {
            for (ApplicationNote retrivedNote : applicationNoteRepo.findAll())
            {
                applicationNotes.add(new ApplicationNoteDao(retrivedNote));
            }
        }
        return applicationNotes;
    }

    // get all applications notes (by application) - not needed - returned in main application
    public List<ApplicationNoteDao> getAplicationNotes(int applicationID)
    {
        Optional<Application> applicationDao = applicationRepo.findById(applicationID);

        if (applicationDao.isPresent())
        {
            List<ApplicationNoteDao> applicationNotes = new ArrayList<>();

            for (ApplicationNote retirvedNote : applicationDao.get().getApplicationNotes())
            {
                applicationNotes.add(new ApplicationNoteDao(retirvedNote));
            }

            return applicationNotes;
        }
        else
        {
            return null;
        }
    }

    // get specific application note details
    public ApplicationNoteDao getNoteDetials(int noteID)
    {
        Optional<ApplicationNote> applicationNote = applicationNoteRepo.findById(noteID);

        return applicationNote.map(ApplicationNoteDao::new).orElse(null);
    }

    // add new application note
    public ApplicationNoteDao creatApplicationNote(int applicationID, String noteBody, String userID)
    {
        ApplicationNote newApplicationNote = new ApplicationNote();

        // create application
        newApplicationNote.setDatetime(Timestamp.from(Instant.now()));
        newApplicationNote.setNotebody(noteBody);
        newApplicationNote.setApplication(applicationRepo.getReferenceById(applicationID));
        newApplicationNote.setUser(userServ.getUser(userID));

        // send notification to manager or user
        Notification newNotification = new Notification();
        newNotification.setEntity("Application");
        newNotification.setItemid(String.valueOf(applicationID));
        newNotification.setNotficmsg("new note added to application");
        if (Objects.equals(userID, "MANAGERID"))
        {
            // send to student
            newNotification.setUser(userServ.getUser(applicationRepo.getReferenceById(applicationID).getUser().getUserid()));
        }
        else
        {
            // send to manager
            newNotification.setUser(userServ.getUser("MANAGERID"));
        }
        newNotification.setSeen(false);
        notificationRepo.save(newNotification);

        // return
        return new ApplicationNoteDao(applicationNoteRepo.save(newApplicationNote));
    }

    // delete application note - full delete by user or manager / admin
    public boolean deleteNote(int noteID)
    {
        applicationNoteRepo.deleteById(noteID);
        return true;
    }
}
