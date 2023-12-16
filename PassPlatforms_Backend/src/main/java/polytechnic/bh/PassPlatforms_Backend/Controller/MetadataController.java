package polytechnic.bh.PassPlatforms_Backend.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import polytechnic.bh.PassPlatforms_Backend.Dao.MetadataDao;
import polytechnic.bh.PassPlatforms_Backend.Service.MetadataServ;

@RestController
@RequestMapping("/api/metadata")
public class MetadataController
{

    @Autowired
    private MetadataServ metadataServ;

    // Get metadata info
    @GetMapping("")
    public ResponseEntity<MetadataDao> getMetadata()
    {
        MetadataDao metadata = metadataServ.getMetadata();
        return (metadata != null) ?
                new ResponseEntity<>(metadata, HttpStatus.OK) :
                new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    // Update metadata
    @PutMapping("/")
    public ResponseEntity<MetadataDao> updateMetadata(@RequestBody MetadataDao metadataDao)
    {
        MetadataDao updatedMetadata = metadataServ.updateMetadata(metadataDao);
        return (updatedMetadata != null) ?
                new ResponseEntity<>(updatedMetadata, HttpStatus.OK) :
                new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}

