package bts.users.user.controller;

import bts.users.config.JwtService;
import bts.users.user.responseObject.Message;
import bts.users.user.responseObject.ResponseLogin;
import bts.users.user.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import java.nio.charset.Charset;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/user")
public class UserController {

    private final UserService userService;
    private final JwtService jwtService;

    @GetMapping("/login")
    public ResponseEntity<Message<ResponseLogin>> login(@RequestParam("code") String code) {
        try {
            return userService.login(code);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping("/test")
    public String test(Authentication authentication) {

        try {
            final UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            log.info(userDetails.getAuthorities().toString());
            return userDetails.getUsername();

        } catch (Exception e) {
//            throw new RuntimeException(e);
            return null;
        }
    }

}
