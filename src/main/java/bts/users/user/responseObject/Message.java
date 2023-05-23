package bts.users.user.responseObject;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Message<T>{

    private Status status;
    private String message;
    private T data;
}
