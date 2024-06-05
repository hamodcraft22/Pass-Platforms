package polytechnic.bh.PassPlatforms_Backend.Constant;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RestController;

@RestController
public final class ManagerConst
{
    public static String MANGER_ID;

    @Value("${PP_MANAGER_ID}")
    public void setMangerId(String managerID)
    {
        ManagerConst.MANGER_ID = managerID;
    }
}
