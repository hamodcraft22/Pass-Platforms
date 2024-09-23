package polytechnic.bh.PassPlatforms_Backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.bind.annotation.CrossOrigin;

@EnableAsync
@EnableScheduling
@CrossOrigin(origins = "*")
@SpringBootApplication
public class PassPlatformsBackendApplication
{

    public static void main(String[] args)
    {
        SpringApplication.run(PassPlatformsBackendApplication.class, args);
    }

}
