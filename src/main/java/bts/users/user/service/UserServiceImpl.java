package bts.users.user.service;

import bts.users.config.JwtService;
import bts.users.oauth2.service.OAuth2Service;
import bts.users.user.model.Role;
import bts.users.user.model.User;
import bts.users.user.repository.UserRepository;
import bts.users.user.responseObject.Message;
import bts.users.user.responseObject.ResponseLogin;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.common.base.Charsets;
import java.util.Map;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final OAuth2Service oAuth2Service;
    private final JwtService jwtService;

    @Override
    public ResponseEntity<Message<ResponseLogin>> login(String code)
        throws JsonProcessingException {

        String accessToken = oAuth2Service.getAccessToken(code); // todo:예외처리api
        Map<String, String> userInfo = oAuth2Service.getUserInfo(accessToken);

        User user = userRepository.findByEmail(userInfo.get("email"));
        String uuid;
        if (user == null) {
            uuid = UUID.randomUUID().toString();
            signup(userInfo, uuid);
        } else {
            uuid = user.getUuid();
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(new MediaType("application", "json", Charsets.UTF_8));
        headers.add("accessToken", jwtService.generateToken(
            User.builder()
                .uuid(uuid)
                .role(Role.USER)
                .build()
        ));
        headers.add("uuid", uuid);

        Message message = new Message();
        ResponseLogin responseLogin = new ResponseLogin();
        responseLogin.setName(userInfo.get("name"));
        responseLogin.setAge(Integer.valueOf(userInfo.get("age")));
        responseLogin.setPoint(userRepository.findByUuid(uuid).getPoint());
        message.setData(responseLogin);

        return ResponseEntity.status(HttpStatus.OK).headers(headers).body(message);
    }

    public void signup(Map<String, String> userInfo, String uuid) {
        userRepository.save(User.builder()
            .email(userInfo.get("email"))
            .name(userInfo.get("name"))
            .phone(userInfo.get("phone"))
            .age(Integer.valueOf(userInfo.get("age")))
            .gender(userInfo.get("gender"))
            .point(0)
            .profileImg(userInfo.get("profileImg"))
            .uuid(uuid)
            .role(Role.USER)
            .build());
    }
}