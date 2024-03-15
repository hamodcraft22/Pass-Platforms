package polytechnic.bh.PassPlatforms_Backend.Util;

import com.auth0.jwk.Jwk;
import com.auth0.jwk.JwkException;
import com.auth0.jwk.JwkProvider;
import com.auth0.jwk.UrlJwkProvider;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.extern.slf4j.Slf4j;

import java.net.MalformedURLException;
import java.net.URL;
import java.security.interfaces.RSAPublicKey;
import java.util.Date;

import static polytechnic.bh.PassPlatforms_Backend.Constant.AuthConstant.TENANT_ID;

@Slf4j
public class TokenValidation
{
    private static final String JWK_PROVIDER_URL = "https://login.microsoftonline.com/" + TENANT_ID + "/discovery/v2.0/keys";

    public static String isValidToken(String token)
    {
        // first, check if it is as bearer token
        if (token != null && token.startsWith("Bearer "))
        {
            int index = token.indexOf("Bearer ") + 7;
            token = token.substring(index);
        }
        else
        {
            return null; // invalid if not bearer
        }
        // decode it
        DecodedJWT jwt = JWT.decode(token);

        Jwk jwk;
        JwkProvider provider;
        Algorithm algorithm;
        try
        {
            provider = new UrlJwkProvider(new URL(JWK_PROVIDER_URL));
            jwk = provider.get(jwt.getKeyId());
            algorithm = Algorithm.RSA256((RSAPublicKey) jwk.getPublicKey(), null);
            algorithm.verify(jwt);
            Date now = new Date();
            Date notBefore = jwt.getNotBefore();
            Date expiresAt = jwt.getExpiresAt();

            if (notBefore != null && expiresAt != null && now.toInstant().compareTo(notBefore.toInstant()) >= 0 && now.toInstant().isBefore(expiresAt.toInstant()))
            {
                return jwt.getClaims().get("unique_name").toString().substring(1, jwt.getClaims().get("unique_name").toString().indexOf("@"));
            }
            else
            {
                return null;
            }

        }
        catch (MalformedURLException | JwkException | SignatureVerificationException e)
        {
            log.warn(e.getMessage(), e);
            return null; // invalid
        }
    }
}
