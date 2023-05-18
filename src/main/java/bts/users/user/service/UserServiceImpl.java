package bts.users.user.service;

import bts.users.user.model.Role;
import bts.users.user.model.User;
import bts.users.user.repository.UserRepository;
import bts.users.user.requestObject.RequestSignup;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public String signup(RequestSignup requestSignup) {

        Optional<User> user = Optional.ofNullable(
            userRepository.findByEmail(requestSignup.getEmail()));

        if (user.isPresent()) {
            return "already signup";
        } else {
            userRepository.save(
                User.builder()
                    .email(requestSignup.getEmail())
                    .name(requestSignup.getName())
                    .phone(requestSignup.getPhone())
                    .age(requestSignup.getAge())
                    .gender(requestSignup.getGender())
                    .point(0)
                    .profileImg(requestSignup.getProfileImg())
                    .role(Role.USER)
                    .build());

            return "signup";
        }
    }
}
