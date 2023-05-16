package bts.oauth2.controller;

import bts.oauth2.service.OAuth2ServiceImpl;
import bts.oauth2.service.UserService;
import java.io.IOException;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("auth")
public class AuthController {

    private final OAuth2ServiceImpl oAuth2ServiceImpl;
    private final UserService userService;

    @GetMapping("test")
    public String msg(){
        return "test";
    }

    @GetMapping("code")
    public String code(@RequestParam("code") String code) {
        return code;
    }

    @GetMapping("login")
    public String signUp(@RequestParam("code") String code) throws IOException {

        Map<String, String> userInfo = oAuth2ServiceImpl.signUp(code);

        if (userService.confirmUser(userInfo.get("email")) == null) {
            userService.registerUser(userInfo);
            return "register";
        } else {
            return "hi";
        }
    }
}
