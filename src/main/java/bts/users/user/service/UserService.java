package bts.users.user.service;

import bts.users.requestObject.RequestAdminLogin;
import bts.users.user.responseObject.Message;
import bts.users.user.responseObject.ResponseAdminLogin;
import bts.users.user.responseObject.ResponseLogin;
import org.springframework.http.ResponseEntity;

public interface UserService {

    public ResponseEntity<Message<ResponseLogin>> login(String code);

    public ResponseEntity<Message<ResponseAdminLogin>> adminLogin(RequestAdminLogin requestAdminLogin);
}
