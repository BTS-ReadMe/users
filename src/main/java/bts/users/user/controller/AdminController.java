package bts.users.user.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/v1/admin")
public class AdminController { // 관리자는 oauth로그인 없이 id, pw입력으로 가입하면 된다


    @GetMapping("/test")
    public String test(Authentication authentication){
        final UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        return "hello";
    }
}
