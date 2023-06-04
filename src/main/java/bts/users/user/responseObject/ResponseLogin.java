package bts.users.user.responseObject;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResponseLogin {

    private String name;
    private String nickname;
    private String profileImg;
    private Integer age;
    private Integer point;

}
