package polytechnic.bh.PassPlatforms_Backend.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import polytechnic.bh.PassPlatforms_Backend.Dao.TranscriptDao;
import polytechnic.bh.PassPlatforms_Backend.Entity.Transcript;
import polytechnic.bh.PassPlatforms_Backend.Entity.User;
import polytechnic.bh.PassPlatforms_Backend.Repository.CourseRepo;
import polytechnic.bh.PassPlatforms_Backend.Repository.TranscriptRepo;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class TranscriptServ
{

    @Autowired
    private TranscriptRepo transcriptRepo;

    @Autowired
    private UserServ userServ;

    @Autowired
    private CourseRepo courseRepo;

    // get all transcripts - not needed
    public List<TranscriptDao> getAllTranscripts()
    {
        List<TranscriptDao> transcripts = new ArrayList<>();

        for (Transcript retrievedTranscript : transcriptRepo.findAll())
        {
            transcripts.add(new TranscriptDao(retrievedTranscript));
        }

        return transcripts;
    }

    // get student transcripts
    public List<TranscriptDao> getLeaderTranscripts(String leaderID)
    {
        List<TranscriptDao> transcripts = new ArrayList<>();

        for (Transcript retrievedTranscript : transcriptRepo.findTranscriptsByStudent_Userid(leaderID))
        {
            transcripts.add(new TranscriptDao(retrievedTranscript));
        }

        return transcripts;
    }

    // a single transcript details - not needed
    public TranscriptDao getTranscriptDetails(int transID)
    {
        Optional<Transcript> retrievedTranscript = transcriptRepo.findById(transID);

        return retrievedTranscript.map(TranscriptDao::new).orElse(null);
    }

    // create transcript entry
    public TranscriptDao createTranscript(String grade, String leaderID, String courseID)
    {
        Transcript newTranscript = new Transcript();

        newTranscript.setGrade(grade);
        newTranscript.setStudent(new User(userServ.getUser(leaderID)));
        newTranscript.setCourseid(courseID);

        return new TranscriptDao(transcriptRepo.save(newTranscript));
    }

    // edit grade - should not be allowed
    public TranscriptDao editTranscript(TranscriptDao updatedTranscript)
    {
        Optional<Transcript> retrievedTranscript = transcriptRepo.findById(updatedTranscript.getTransid());

        if (retrievedTranscript.isPresent())
        {
            retrievedTranscript.get().setGrade(updatedTranscript.getGrade());

            return new TranscriptDao(transcriptRepo.save(retrievedTranscript.get()));
        }
        else
        {
            return null;
        }
    }

    // delete transcript
    public boolean deleteTranscript(int transID)
    {
        transcriptRepo.deleteById(transID);
        return true;
    }
}

