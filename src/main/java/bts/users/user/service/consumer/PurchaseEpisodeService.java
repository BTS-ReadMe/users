package bts.users.user.service.consumer;

import bts.users.dto.PurchaseEpisodeDto;
import bts.users.user.model.User;
import bts.users.user.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PurchaseEpisodeService {

    private final UserRepository userRepository;

    @KafkaListener(id = "purchaseEpisode",topics = "purchaseEpisode")
    public void purchaseEpisode(String kafkaMessage){
        ObjectMapper mapper=new ObjectMapper();

        try {
            PurchaseEpisodeDto purchaseEpisodeDto=mapper.readValue(kafkaMessage, PurchaseEpisodeDto.class);
            String uuid= purchaseEpisodeDto.getUuid();
            User user = userRepository.findByUuid(uuid);

            if(user.getPoint()>=100){
                user.purchaseEpisode();
                userRepository.save(user);
            }
            else{
                //todo: point 부족할 때 예외 처리
                // 1) 여기서 바로 에러 발생
                // 2) payment로 오류 전송
                // 어떤 방법을 써야 될지??
                return;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
