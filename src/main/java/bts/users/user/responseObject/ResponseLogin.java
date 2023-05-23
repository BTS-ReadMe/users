package bts.users.user.responseObject;

import lombok.Getter;

@Getter
public class ResponseLogin {

    private String name;
    private Integer age;

    public void setName(String name) {
        this.name = name;
    }

    public void setAge(Integer age) {
        this.age = age;
    }
}
