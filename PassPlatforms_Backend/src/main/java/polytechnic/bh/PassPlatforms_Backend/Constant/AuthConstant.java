package polytechnic.bh.PassPlatforms_Backend.Constant;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RestController;

@RestController
public final class AuthConstant
{
    public static String TENANT_ID;

    public static String CLIENT_ID;

    public static String CLIENT_SECRET;


    @Value("${PP_TENANT_ID}")
    public void setTenantId(String tenantId) {
        AuthConstant.TENANT_ID = tenantId;
    }

    @Value("${PP_CLIENT_ID}")
    public void setClientId(String clientId) {
        AuthConstant.CLIENT_ID = clientId;
    }

    @Value("${PP_CLIENT_SECRET}")
    public void setClientSecret(String clientSecret) {
        AuthConstant.CLIENT_SECRET = clientSecret;
    }
}
