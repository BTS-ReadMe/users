package bts.oauth2.dto;

import bts.oauth2.entity.type.AuthProvider;
import bts.oauth2.exception.OAuth2AuthenticationProcessingException;
import java.util.Map;

public class OAuth2UserInfoFactory {

    public static OAuth2UserInfo getOAuth2UserInfo(String registrationId, Map<String, Object> attributes) {
        if (registrationId.equalsIgnoreCase(AuthProvider.naver.toString())) {
            return new NaverOAuth2UserInfo(attributes);
        } else {
            throw new OAuth2AuthenticationProcessingException("Unsupported Login Type : " + registrationId);
        }
    }
}
