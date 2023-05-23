package bts.users.user.service;

import bts.users.oauth2.service.OAuth2Service;
import bts.users.user.model.Role;
import bts.users.user.model.User;
import bts.users.user.repository.UserRepository;
import bts.users.user.responseObject.Message;
import bts.users.user.responseObject.ResponseLogin;
import bts.users.user.responseObject.Status;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.common.base.Charsets;
import java.util.Map;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final OAuth2Service oAuth2Service;

    @Override
    public ResponseEntity<Message<ResponseLogin>> login(String code) throws JsonProcessingException {

        String accessToken = oAuth2Service.getAccessToken(code); // todo:예외처리api
        Map<String, String> userInfo = oAuth2Service.getUserInfo(accessToken);

        String uuid = UUID.randomUUID().toString();

        if (userRepository.findByEmail(userInfo.get("email")) == null) {
            signup(userInfo, uuid);
        }

        Message message = new Message();

        ResponseLogin responseLogin = new ResponseLogin();
        responseLogin.setName(userInfo.get("name"));
        responseLogin.setAge(Integer.valueOf(userInfo.get("age")));

        message.setData(responseLogin);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(new MediaType("application", "json", Charsets.UTF_8));
        headers.add("accessToken", accessToken);
        headers.add("uuid", uuid);

        return new ResponseEntity<>(message, headers, HttpStatus.OK);
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