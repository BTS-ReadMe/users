package bts.users.user.controller;

import bts.users.user.responseObject.Message;
import bts.users.user.responseObject.ResponseLogin;
import bts.users.user.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
public class UserController { //todo : try catch를 controller말고 service에서 처리하기

    private final UserService userService;

    @Operation(summary = "로그인", description = "로그인 하기", tags = {"로그인"})
    @GetMapping("/login")
    public ResponseEntity<Message<ResponseLogin>> login(@RequestParam("code") String code) {
        try {
            return userService.login(code);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

}
