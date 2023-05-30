package bts.users.user.service.producer;

import bts.users.dto.PurchaseEpisodeResultDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SendPurchaseEpisodeResultService {

    private final KafkaTemplate<String,Object> kafkaTemplate;

    public void sendPurchaseResultEpisode(PurchaseEpisodeResultDto purchaseEpisodeResultDto) {
        ObjectMapper mapper = new ObjectMapper();
        String data = "";

        try {
            data = mapper.writeValueAsString(purchaseEpisodeResultDto);
        } catch (Exception e) {
            return;
        }

        kafkaTemplate.send("purchaseEpisodeResult", data);
    }
}