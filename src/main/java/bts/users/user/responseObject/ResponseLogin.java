package bts.users.user.responseObject;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResponseLogin {

//    private String name;
//    private Integer age;
    private String email;
    private String nickname;
    private String profileImg;
    private String gender;
    private String age_range;
    private String birthday;
    private Integer point;

}
