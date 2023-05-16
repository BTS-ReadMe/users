package bts.oauth2.service;

import bts.oauth2.entity.User;
import bts.oauth2.repository.UserRepository;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public User confirmUser(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public User registerUser(Map<String, String> userInfo) {

        User user = new User();
        user.setGender(userInfo.get("gender"));
        user.setEmail(userInfo.get("email"));
        user.setPhone(userInfo.get("mobile"));
        user.setName(userInfo.get("name"));
        user.setAge(Integer.parseInt(userInfo.get("age")));
        user.setPoint(0);
        user.setProfileImg(userInfo.get("profileImg"));
        return userRepository.save(user);
    }
}
