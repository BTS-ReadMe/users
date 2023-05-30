package bts.users.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class ChargePointDto {

    private String uuid;
    private Integer point;

    @JsonCreator
    public ChargePointDto(@JsonProperty("uuid") String uuid, @JsonProperty("point") Integer point) {
        this.uuid = uuid;
        this.point = point;
    }
}