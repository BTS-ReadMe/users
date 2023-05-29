package bts.users.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class PurchaseEpisodeDto {

    private String uuid;

    @JsonCreator
    public PurchaseEpisodeDto(@JsonProperty("uuid") String uuid) {
        this.uuid = uuid;
    }
}
