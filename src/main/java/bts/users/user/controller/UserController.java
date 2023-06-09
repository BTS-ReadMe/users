package bts.users.user.controller;

import bts.users.user.responseObject.Message;
import bts.users.user.responseObject.ResponseGetPoint;
import bts.users.user.responseObject.ResponseLogin;
import bts.users.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/user")
public class UserController {

    private final UserService userService;

    @Operation(summary = "로그인", description = "로그인 하기", tags = {"로그인"})
    @PostMapping("/login")
    public ResponseEntity<Message<ResponseLogin>> login(@RequestParam("code") String code) {
        return userService.login(code);
    }

    @Operation(summary = "포인트 조회", description = "포인트 조회하기", tags = {"포인트 조회"})
    @GetMapping("/getPoint")
    public ResponseEntity<Message<ResponseGetPoint>> getPoint(@RequestHeader("uuid") String uuid) {
        return userService.getPoint(uuid);
    }
}