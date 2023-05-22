package bts.users.user.service;

import bts.users.config.JwtService;
import bts.users.oauth2.service.OAuth2Service;
import bts.users.user.model.Role;
import bts.users.user.model.User;
import bts.users.user.repository.UserRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserDetailsService userDetailsService;
    private final UserRepository userRepository;
    private final OAuth2Service oAuth2Service;
    private final JwtService jwtService;

    @Override
    public String login(String code) throws JsonProcessingException {

        String accessToken = oAuth2Service.getAccessToken(code); // 해야될거:예외처리api
        Map<String, String> userInfo = oAuth2Service.getUserInfo(accessToken);

        if (userRepository.findByEmail(userInfo.get("email")) == null) signup(userInfo);

        return jwtService.generateToken(userDetailsService.loadUserByUsername(userInfo.get("email")));
    }

    public void signup(Map<String,String> userInfo){
        userRepository.save(User.builder()
            .email(userInfo.get("email"))
            .name(userInfo.get("name"))
            .phone(userInfo.get("phone"))
            .age(Integer.valueOf(userInfo.get("age")))
            .gender(userInfo.get("gender"))
            .point(0)
            .profileImg(userInfo.get("profileImg"))
            .role(Role.USER)
            .build());
    }
}