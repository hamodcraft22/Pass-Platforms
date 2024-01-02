package polytechnic.bh.PassPlatforms_Backend.Util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import polytechnic.bh.PassPlatforms_Backend.Dto.UserInfoDto;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UsersService
{
    public static Map<String, String> allAzureAdUsers = new HashMap<>();

    public static List<UserInfoDto> allAzureStudents = new ArrayList<>();

    public static String getAzureAdName(String userID)
    {
        try
        {
            if (allAzureAdUsers.isEmpty())
            {
                refreshUsers();
            }

            String userName = allAzureAdUsers.get(userID);

            if (userName != null && userName.length() > 0)
            {
                return userName;
            }
            else
            {
                return " ";
            }

        }
        catch (Exception ex)
        {
            return " ";
        }
    }

    public static void refreshUsers() throws JSONException
    {
        System.out.println("Fetching Users");
        String url = "https://graph.microsoft.com/v1.0/users";
        String token = getToken();

        if (token != null)
        {
            HttpHeaders headers = new HttpHeaders();

            headers.setBearerAuth(token);

            HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(headers);

            RestTemplate template = new RestTemplate();

            ResponseEntity<String> response = template.exchange(url, HttpMethod.GET, entity, String.class);

            JSONObject myjson = new JSONObject(response.getBody());

            try
            {
                // Create an ObjectMapper instance
                ObjectMapper objectMapper = new ObjectMapper();

                // Convert the JSON string to a List of Map objects
                List<Map<String, Object>> userList = objectMapper.readValue(myjson.get("value").toString(), new TypeReference<>()
                {
                });

                // Create a Map to store the key-value pairs
                Map<String, String> resultMap = new HashMap<>();

                allAzureStudents.clear();

                // Iterate over the user list and extract the desired fields
                for (Map<String, Object> user : userList)
                {
                    String userID = ((String) user.get("userPrincipalName")).substring(0, ((String) user.get("userPrincipalName")).indexOf("@"));
                    String displayName = (String) user.get("displayName");

                    resultMap.put(userID, displayName);


                    if (userID.charAt(0) == '2')
                    {
                        allAzureStudents.add(new UserInfoDto(userID, displayName));
                    }
                }

                allAzureAdUsers = resultMap;
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }

        }
    }

    private static String getToken()
    {
        try
        {
            String url = "https://login.microsoftonline.com/TANENT_ID/oauth2/v2.0/token";
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
            MultiValueMap<String, String> inputMap = new LinkedMultiValueMap<>();
            inputMap.add("grant_type", "client_credentials");
            inputMap.add("client_id", "CLIENT_ID");
            inputMap.add("client_secret", "CLIENT_SECRET");
            inputMap.add("scope", "https://graph.microsoft.com/.default");
            HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(inputMap, headers);
            RestTemplate template = new RestTemplate();
            ResponseEntity<String> response = template.postForEntity(url, entity, String.class);
            JSONObject myjson = new JSONObject(response.getBody());
            return (String) myjson.get("access_token");
        }
        catch (Exception ex)
        {
            return null;
        }
    }
}
