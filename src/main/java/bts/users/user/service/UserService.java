package bts.users.user.service;

import bts.users.user.responseObject.Message;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.http.ResponseEntity;

public interface UserService {

    public ResponseEntity<Message> login(String code) throws JsonProcessingException;
}
