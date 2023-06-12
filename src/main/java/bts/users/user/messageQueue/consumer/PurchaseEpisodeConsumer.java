package bts.users.user.messageQueue.consumer;

import bts.users.dto.PurchaseEpisodeDto;
import bts.users.dto.PurchaseEpisodeResultDto;
import bts.users.user.model.User;
import bts.users.user.repository.UserRepository;
import bts.users.user.messageQueue.producer.PurchaseEpisodeResultProducer;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.io.IOException;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PurchaseEpisodeConsumer {

    private final UserRepository userRepository;
    private final PurchaseEpisodeResultProducer purchaseEpisodeResultProducer;

    @KafkaListener(id = "purchaseEpisode", topics = "purchaseEpisode")
    public void purchaseEpisode(String kafkaMessage) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());

        PurchaseEpisodeDto purchaseEpisodeDto = mapper.readValue(kafkaMessage,
            PurchaseEpisodeDto.class);

        String id = purchaseEpisodeDto.getId();
        String uuid = purchaseEpisodeDto.getUuid();
        Long episodeId = purchaseEpisodeDto.getEpisodeId();

        User user = userRepository.findByUuid(uuid);
        LocalDateTime purchasedDate = null;
        Boolean result = true;

        if (user.getPoint() >= 100) {
            user.purchaseEpisode();
            purchasedDate = userRepository.save(user).getUpdatedDate();

        } else {
            result = false;
        }

        PurchaseEpisodeResultDto purchaseEpisodeResultDto =
            PurchaseEpisodeResultDto.builder()
                .id(id)
                .uuid(uuid)
                .episodeId(episodeId)
                .result(result)
                .purchasedDate(purchasedDate)
                .build();
        purchaseEpisodeResultProducer.sendPurchaseResultEpisode(purchaseEpisodeResultDto);


    }
}
