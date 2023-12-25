package polytechnic.bh.PassPlatforms_Backend.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import polytechnic.bh.PassPlatforms_Backend.Dao.MetadataDao;
import polytechnic.bh.PassPlatforms_Backend.Dao.UserDao;
import polytechnic.bh.PassPlatforms_Backend.Service.MetadataServ;
import polytechnic.bh.PassPlatforms_Backend.Service.UserServ;

import static polytechnic.bh.PassPlatforms_Backend.Constant.RoleConstant.ROLE_ADMIN;
import static polytechnic.bh.PassPlatforms_Backend.Constant.RoleConstant.ROLE_MANAGER;
import static polytechnic.bh.PassPlatforms_Backend.Util.TokenValidation.isValidToken;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/metadata")
public class MetadataController
{

    @Autowired
    private MetadataServ metadataServ;

    @Autowired
    private UserServ userServ;

    // Get metadata info
    @GetMapping("")
    public ResponseEntity<MetadataDao> getMetadata(
            @RequestHeader(value = "Authorization") String requestKey
    )
    {
        String userID = isValidToken(requestKey);

        if (userID != null)
        {
            MetadataDao metadata = metadataServ.getMetadata();
            return (metadata != null) ?
                    new ResponseEntity<>(metadata, HttpStatus.OK) :
                    new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        else
        {
            return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
        }


    }

    @GetMapping("/disabled")
    public ResponseEntity<Boolean> getSysDisable()
    {
        Boolean metadata = metadataServ.getDisable();

        return new ResponseEntity<>(metadata, HttpStatus.OK);
    }

    // Update metadata
    @PutMapping("")
    public ResponseEntity<MetadataDao> updateMetadata(
            @RequestHeader(value = "Authorization") String requestKey,
            @RequestBody MetadataDao metadataDao)
    {
        String userID = isValidToken(requestKey);

        if (userID != null)
        {
            //token is valid, get user and role
            UserDao user = userServ.getUser(userID);

            if (user.getRole().getRoleid() == ROLE_MANAGER)
            {
                MetadataDao updatedMetadata = metadataServ.updateMetadata(metadataDao);
                return (updatedMetadata != null) ?
                        new ResponseEntity<>(updatedMetadata, HttpStatus.OK) :
                        new ResponseEntity<>(HttpStatus.BAD_REQUEST);
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

    // reset database
    @DeleteMapping("")
    public ResponseEntity<MetadataDao> resetSystem(
            @RequestHeader(value = "Authorization") String requestKey)
    {
        String userID = isValidToken(requestKey);

        if (userID != null)
        {
            //token is valid, get user and role
            UserDao user = userServ.getUser(userID);

            if (user.getRole().getRoleid() == ROLE_MANAGER || user.getRole().getRoleid() == ROLE_ADMIN)
            {
                if (metadataServ.resetSystem())
                {
                    return new ResponseEntity<>(null, HttpStatus.OK);
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
}

