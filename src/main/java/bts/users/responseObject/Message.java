package bts.users.responseObject;

import lombok.Getter;

@Getter
public class Message<T>{

    private T data;

    public void setData(T data) {
        this.data = data;
    }
}
