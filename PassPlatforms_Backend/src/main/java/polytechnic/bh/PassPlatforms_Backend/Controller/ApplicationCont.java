package polytechnic.bh.PassPlatforms_Backend.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import polytechnic.bh.PassPlatforms_Backend.Dto.ApplicationDto;
import polytechnic.bh.PassPlatforms_Backend.Entity.Application;
import polytechnic.bh.PassPlatforms_Backend.Entity.ApplicationNote;
import polytechnic.bh.PassPlatforms_Backend.Repository.ApplicationRepo;

import java.util.*;

@RestController
@RequestMapping("/api")
public class ApplicationCont<T> {

    @Autowired
    ApplicationRepo applicationRepo;

    @GetMapping("/application")
    public ResponseEntity<ApplicationDto> getAllTutorials(@RequestHeader(value = "requestKey", required = false) String requestKey)
    {
        if (Objects.equals(requestKey, "student-3e1d-4e5f-a2b1-6c7d8e9f0a1b"))
        {
            List<Application> applications = applicationRepo.findAll();

            //ApplicationDto retrivedApplication = new ApplicationDto<T>();

            //retrivedApplication.setApplicationid(applications.get(0).getApplicationid());
            //retrivedApplication.setDatetime(applications.get(0).getDatetime());
            //retrivedApplication.setNote(applications.get(0).getNote());

            Map<String, T> statusMap = new TreeMap<>();
            statusMap.put("Status", (T) applications.get(0).getApplicationStatus().getStatusname());
            //retrivedApplication.setApplicationStatus(statusMap);

            Map<String, T> userMap = new TreeMap<>();
            userMap.put("userID",(T) applications.get(0).getUser().getUserid());
            userMap.put("Role",(T) applications.get(0).getUser().getRole());
            //retrivedApplication.setUser(userMap);

            List<Map<String, T>> notesMap = new LinkedList<>();

            for (ApplicationNote note : applications.get(0).getApplicationNotes())
            {
                Map<String, T> noteMap = new HashMap<>();

                noteMap.put("noteId", (T) String.valueOf(note.getNoteid()));
                noteMap.put("dateTime", (T) note.getDatetime());
                noteMap.put("Body", (T) note.getNotebody());
                noteMap.put("User", (T) note.getUser().getUserid());

                notesMap.add(noteMap);
            }
            //retrivedApplication.setApplicationNotes(notesMap);

            return new ResponseEntity<>(null, HttpStatus.OK);
        }
        else
        {
            return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
        }

    }
}
