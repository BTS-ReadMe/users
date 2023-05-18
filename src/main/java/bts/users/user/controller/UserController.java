package bts.users.user.controller;

import bts.users.user.requestObject.RequestSignup;
import bts.users.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/user")
public class UserController {

    private final UserService userService;

    @PostMapping("/signup")
    public String signup(@RequestBody RequestSignup requestSignup) {
        return userService.signup(requestSignup);
    }
}
