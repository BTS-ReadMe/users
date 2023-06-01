package bts.users.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class ChargePointDto {

    private String id;
    private Integer point;

    @JsonCreator
    public ChargePointDto(@JsonProperty("id") String id, @JsonProperty("point") Integer point) {
        this.id = id;
        this.point = point;
    }
}