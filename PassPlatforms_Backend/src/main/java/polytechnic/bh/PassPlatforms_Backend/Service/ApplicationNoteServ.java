package polytechnic.bh.PassPlatforms_Backend.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import polytechnic.bh.PassPlatforms_Backend.Dao.ApplicationNoteDao;
import polytechnic.bh.PassPlatforms_Backend.Dao.UserDao;
import polytechnic.bh.PassPlatforms_Backend.Entity.Application;
import polytechnic.bh.PassPlatforms_Backend.Entity.ApplicationNote;
import polytechnic.bh.PassPlatforms_Backend.Repository.ApplicationNoteRepo;
import polytechnic.bh.PassPlatforms_Backend.Repository.ApplicationRepo;
import polytechnic.bh.PassPlatforms_Backend.Repository.UserRepo;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ApplicationNoteServ
{

    @Autowired
    private ApplicationRepo applicationRepo;

    @Autowired
    private ApplicationNoteRepo applicationNoteRepo;

    @Autowired
    private UserRepo userRepo;


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
                        new UserDao(retrivedNote.getUser())
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

        newApplicationNote.setDatetime(Timestamp.from(Instant.now()));
        newApplicationNote.setNotebody(noteBody);
        newApplicationNote.setApplication(applicationRepo.getReferenceById(applicationID));
        newApplicationNote.setUser(userRepo.getReferenceById(userID));

        return new ApplicationNoteDao(applicationNoteRepo.save(newApplicationNote));
    }

    // delete application note - full delete by user or manager / admin
    public boolean deleteNote(int noteID)
    {
        applicationNoteRepo.deleteById(noteID);
        return true;
    }
}
