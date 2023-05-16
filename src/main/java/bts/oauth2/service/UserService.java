package bts.oauth2.service;

import bts.oauth2.entity.User;
import java.util.Map;

public interface UserService {

    public User confirmUser(String email);

    public User registerUser(Map<String, String> userInfo);
}
