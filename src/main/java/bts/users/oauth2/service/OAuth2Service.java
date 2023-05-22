package bts.users.oauth2.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import java.util.Map;

public interface OAuth2Service {

//    public Map<String, String> signUp(String code) throws JsonProcessingException;

    public String getAccessToken(String code) throws JsonProcessingException;

    public Map<String, String> getUserInfo(String accessToken) throws JsonProcessingException;
}
