package bts.users.user.service;

import bts.users.user.responseObject.Message;
import bts.users.user.responseObject.ResponseLogin;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.http.ResponseEntity;

public interface UserService {

    public ResponseEntity<Message<ResponseLogin>> login(String code) throws JsonProcessingException;
}
