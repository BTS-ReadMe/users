package bts.users.user.messageQueue.producer;

import bts.users.dto.PurchaseEpisodeResultDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PurchaseEpisodeResultProducer {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void sendPurchaseResultEpisode(PurchaseEpisodeResultDto purchaseEpisodeResultDto) {

        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());

        try {
            String data = mapper.writeValueAsString(purchaseEpisodeResultDto);
            kafkaTemplate.send("purchaseEpisodeResult", data);
        } catch (Exception e) {
        }
    }
}