package bts.users.user.responseObject;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ResponseGetPoint {

    private String uuid;
    private Integer point;
}
