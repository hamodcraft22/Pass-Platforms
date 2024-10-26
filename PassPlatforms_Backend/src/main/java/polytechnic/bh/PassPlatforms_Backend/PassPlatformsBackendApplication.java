package polytechnic.bh.PassPlatforms_Backend;

import jakarta.annotation.PostConstruct;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.util.Date;
import java.util.TimeZone;
import java.util.logging.Logger;

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

    @PostConstruct
    public void init()
    {
        Logger logger = Logger.getLogger(PassPlatformsBackendApplication.class.getName());

        // Setting Spring Boot SetTimeZone
        TimeZone.setDefault(TimeZone.getTimeZone("GMT+3"));

        logger.info("Date in Project now: " + new Date());
    }
}
