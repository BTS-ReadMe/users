package bts.users.dto;

import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PurchaseEpisodeResultDto {

    private String id;
    private String uuid;
    private Long episodeId;
    private Boolean result;
    private LocalDateTime purchasedDate;
}
