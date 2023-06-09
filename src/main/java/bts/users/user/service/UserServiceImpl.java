package bts.users.user.service;

import bts.users.config.JwtService;
import bts.users.oauth2.service.OAuth2Service;
import bts.users.user.requestObject.RequestAdminLogin;
import bts.users.user.model.Role;
import bts.users.user.model.User;
import bts.users.user.repository.UserRepository;
import bts.users.user.responseObject.Message;
import bts.users.user.responseObject.ResponseAdminLogin;
import bts.users.user.responseObject.ResponseGetPoint;
import bts.users.user.responseObject.ResponseLogin;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.common.base.Charsets;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final OAuth2Service oAuth2Service;
    private final JwtService jwtService;

    @Override
    public ResponseEntity<Message<ResponseLogin>> login(String code) {

        String accessToken;
        Map<String, String> userInfo = new HashMap<>();

        try {
            accessToken = oAuth2Service.getAccessToken(code);
            userInfo = oAuth2Service.getUserInfo(accessToken);
        } catch (Exception e) {
            log.info("fail to use oauth2 service");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

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
        responseLogin.setEmail(userInfo.get("email"));
        responseLogin.setNickname(userInfo.get("nickname"));
        responseLogin.setProfileImg(userInfo.get("profileImg"));
        responseLogin.setGender(userInfo.get("gender"));
        responseLogin.setAge_range(userInfo.get("age_range"));
        responseLogin.setBirthday(userInfo.get("birthday"));
        responseLogin.setPoint(userRepository.findByUuid(uuid).getPoint());
        message.setData(responseLogin);

        return ResponseEntity.status(HttpStatus.OK).headers(headers).body(message);
    }

    @Override
    public ResponseEntity<Message<ResponseAdminLogin>> adminLogin(
        RequestAdminLogin requestAdminLogin) {

        String adminId = requestAdminLogin.getId();
        String adminPassword = requestAdminLogin.getPassword();

        HttpHeaders headers = new HttpHeaders();
        Message message = new Message();

        User admin = userRepository.findByEmail(adminId);
        if (admin != null && admin.getUuid().equals(adminPassword)) {

            headers.setContentType(new MediaType("application", "json", Charsets.UTF_8));
            headers.add("accessToken", jwtService.generateToken(
                User.builder()
                    .role(Role.ADMIN)
                    .uuid(null)
                    .build()
            ));

            ResponseAdminLogin responseAdminLogin = new ResponseAdminLogin();
            responseAdminLogin.setNickname(admin.getNickname());
            message.setData(responseAdminLogin);

            return ResponseEntity.status(HttpStatus.OK).headers(headers).body(message);
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).headers(headers).body(message);
        }
    }

    @Override
    public ResponseEntity<Message<ResponseGetPoint>> getPoint(String uuid) {

        Message message = new Message();
        ResponseGetPoint responseGetPoint =
            ResponseGetPoint.builder()
                .uuid(uuid)
                .point(userRepository.findByUuid(uuid).getPoint())
                .build();
        message.setData(responseGetPoint);

        return ResponseEntity.status(HttpStatus.OK).body(message);
    }


    public void signup(Map<String, String> userInfo, String uuid) {
        userRepository.save(User.builder()
            .email(userInfo.get("email"))
            .nickname(userInfo.get("nickname"))
            .age_range(userInfo.get("age_range"))
            .gender(userInfo.get("gender"))
            .point(0)
            .profileImg(userInfo.get("profileImg"))
            .birthday(userInfo.get("birthday"))
            .uuid(uuid)
            .role(Role.USER)
            .build());
    }
}