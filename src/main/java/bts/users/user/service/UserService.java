package bts.users.user.service;

import bts.users.user.requestObject.RequestAdminLogin;
import bts.users.user.responseObject.Message;
import bts.users.user.responseObject.ResponseAdminLogin;
import bts.users.user.responseObject.ResponseGetPoint;
import bts.users.user.responseObject.ResponseLogin;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.http.ResponseEntity;

public interface UserService {

    public ResponseEntity<Message<ResponseLogin>> login(String code);

    public ResponseEntity<Message<ResponseAdminLogin>> adminLogin(RequestAdminLogin requestAdminLogin);

    public ResponseEntity<Message<ResponseGetPoint>> getPoint(String uuid);
}
