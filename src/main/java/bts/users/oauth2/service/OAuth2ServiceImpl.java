package bts.users.oauth2.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.Year;
import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class OAuth2ServiceImpl implements OAuth2Service {

    @Value("${spring.security.oauth2.client.registration.kakao.client-id}")
    private String CLIENT_ID;

    @Value("${spring.security.oauth2.client.registration.kakao.client-secret}")
    private String CLIENT_SECRET;

    @Value("${spring.security.oauth2.client.registration.kakao.redirect-uri}")
    private String REDIRECT_URI;

    @Value("${spring.security.oauth2.client.provider.kakao.token-uri}")
    private String TOKEN_URI;

    @Value("${spring.security.oauth2.client.provider.kakao.user-info-uri}")
    private String USER_INFO_URI;

    @Override
    public String getAccessToken(String code) throws JsonProcessingException {

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "authorization_code");
        body.add("client_id", CLIENT_ID);
        body.add("redirect_uri", REDIRECT_URI);
        body.add("code", code);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(body, headers);
        RestTemplate restTemplate = new RestTemplate();
        try {
            ResponseEntity<String> response = restTemplate.exchange(
                TOKEN_URI,
                HttpMethod.POST,
                request,
                String.class
            );

            String responseBody = response.getBody();
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(responseBody);
            return jsonNode.get("access_token").asText();

        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public Map<String, String> getUserInfo(String accessToken) throws JsonProcessingException {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + accessToken);
        headers.add("Content-type", "application/x-form-urlencoded;charset=utf-8");

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(headers);
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response;

        response = restTemplate.exchange(
            USER_INFO_URI,
            HttpMethod.POST,
            request,
            String.class
        );

        String responseBody = response.getBody();
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(responseBody).get("kakao_account");

        Map<String, String> userInfo = new HashMap<>();

//        userInfo.put("email", jsonNode.get("email").asText());
//        userInfo.put("name", jsonNode.get("name").asText());
//        userInfo.put("phone", jsonNode.get("phone_number").asText());
//
//        Integer age = (Integer) Year.now().getValue()
//            - Integer.valueOf(jsonNode.get("birthyear").asText()) + 1;
//        userInfo.put("age", age.toString());
//
//        userInfo.put("gender", jsonNode.get("gender").asText());
//        userInfo.put("nickname", jsonNode.get("profile").get("nickname").asText());
//        userInfo.put("profileImg", jsonNode.get("profile").get("profile_image_url").asText());

        userInfo.put("email", jsonNode.get("email").asText());
        userInfo.put("nickname", jsonNode.get("profile").get("nickname").asText());
        userInfo.put("profileImg", jsonNode.get("profile").get("profile_image_url").asText());

        userInfo.put("gender", (jsonNode.get("gender_needs_agreement").asText().equals("false") ?
            jsonNode.get("gender").asText() : null));
        userInfo.put("age_range", (jsonNode.get("age_range_needs_agreement").asText().equals("false")?
            jsonNode.get("age_range").asText() : "20-29")); // temporary
        userInfo.put("birthday", (jsonNode.get("birthday_needs_agreement").asText().equals("false")?
            jsonNode.get("birthday").asText() : null));

        return userInfo;
    }
}