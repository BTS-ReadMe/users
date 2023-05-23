package bts.users.user.responseObject;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Message {

    private Status status;
    private String message;
    private Object data;
}
