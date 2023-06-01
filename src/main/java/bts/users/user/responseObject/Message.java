package bts.users.user.responseObject;

import lombok.Getter;

@Getter
public class Message<T>{

    private T data;

    public void setData(T data) {
        this.data = data;
    }
}
