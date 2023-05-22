package bts.users.user.service;

import com.fasterxml.jackson.core.JsonProcessingException;

public interface UserService {

    public String login(String code) throws JsonProcessingException;
}
