package bts.oauth2.service;

//import bts.oauth2.repository.UserRepository;
//import bts.oauth2.util.TokenProvider;

import bts.oauth2.repository.UserRepository;
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

    private final UserRepository userRepository;

    @Value("${spring.security.oauth2.client.registration.naver.client-id}")
    private String CLIENT_ID;

    @Value("${spring.security.oauth2.client.registration.naver.client-secret}")
    private String CLIENT_SECRET;

    @Value("${spring.security.oauth2.client.registration.naver.redirect-uri}")
    private String REDIRECT_URI;

    @Value("${spring.security.oauth2.client.provider.naver.token-uri}")
    private String TOKEN_URI;

    @Value("${spring.security.oauth2.client.provider.naver.user-info-uri}")
    private String USER_INFO_URI;

    @Override
    public Map<String, String> signUp(String code) throws JsonProcessingException {
        String accessToken = getAccessToken(code);
        return getUserInfo(accessToken);
    }

    @Override
    public String getAccessToken(String code) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "authorization_code");
        body.add("client_id", CLIENT_ID);
        body.add("client_secret", CLIENT_SECRET);
        body.add("redirect_uri", REDIRECT_URI);
        body.add("code", code);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(body, headers);
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.exchange(
            TOKEN_URI,
            HttpMethod.POST,
            request,
            String.class
        );

        String responseBody = response.getBody();
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(responseBody);
            return jsonNode.get("access_token").asText();
        } catch (Exception e) {
            return e.toString();
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
        JsonNode jsonNode = objectMapper.readTree(responseBody);
        Map<String, String> userInfo = new HashMap<>();

//        userInfo.put("id", jsonNode.get("response").get("id").asText());
        userInfo.put("gender", jsonNode.get("response").get("gender").asText());
        userInfo.put("email", jsonNode.get("response").get("email").asText());
        userInfo.put("mobile", jsonNode.get("response").get("mobile").asText().replaceAll("-", ""));
        userInfo.put("name", jsonNode.get("response").get("name").asText());

        Integer age = (Integer) Year.now().getValue() - Integer.valueOf(
            jsonNode.get("response").get("birthyear").toString().replaceAll("[^0-9]", "")) + 1;

        userInfo.put("age", age.toString());

        return userInfo;
    }
}