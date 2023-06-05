package bts.users.dto;

import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ChargePointResultDto {

    private String id;
    private String uuid;
    private Integer total;
    private Integer point;
    private LocalDateTime chargedDate;
}
