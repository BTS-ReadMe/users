package bts.users.user.controller;

import bts.users.user.requestObject.RequestAdminLogin;
import bts.users.user.responseObject.Message;
import bts.users.user.responseObject.ResponseAdminLogin;
import bts.users.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/admin")
public class AdminController {

    private final UserService userService;

    @Operation(summary = "관리자 로그인", description = "관리자 로그인 하기", tags = {"로그인"})
    @PostMapping("/login")
    public ResponseEntity<Message<ResponseAdminLogin>> login(@RequestBody RequestAdminLogin requestAdminLogin) {
        return userService.adminLogin(requestAdminLogin);
    }
}
