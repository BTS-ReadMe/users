package bts.users.user.service.consumer;

import bts.users.dto.PurchaseEpisodeDto;
import bts.users.dto.PurchaseEpisodeResultDto;
import bts.users.user.model.User;
import bts.users.user.repository.UserRepository;
import bts.users.user.service.producer.SendPurchaseEpisodeResultService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PurchaseEpisodeService {

    private final UserRepository userRepository;
    private final SendPurchaseEpisodeResultService sendPurchaseEpisodeResultService;

    @KafkaListener(id = "purchaseEpisode", topics = "purchaseEpisode")
    public void purchaseEpisode(String kafkaMessage) {
        ObjectMapper mapper = new ObjectMapper();

        try {
            PurchaseEpisodeDto purchaseEpisodeDto = mapper.readValue(kafkaMessage, PurchaseEpisodeDto.class);
            String uuid = purchaseEpisodeDto.getMessage();
            User user = userRepository.findByUuid(uuid);
            Boolean result = true;

            if (user.getPoint() >= 100) {
                user.purchaseEpisode();
                userRepository.save(user);
            }
            else{
                result = false;
            }

            PurchaseEpisodeResultDto purchaseEpisodeResultDto=new PurchaseEpisodeResultDto();
            purchaseEpisodeResultDto.setId(purchaseEpisodeDto.getId());
            purchaseEpisodeResultDto.setResult(result);
            sendPurchaseEpisodeResultService.sendPurchaseResultEpisode(purchaseEpisodeResultDto);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
