package bts.users.user.messageQueue.consumer;

import bts.users.dto.ChargePointDto;
import bts.users.dto.ChargePointResultDto;
import bts.users.user.model.User;
import bts.users.user.repository.UserRepository;
import bts.users.user.messageQueue.producer.ChargePointResultProducer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChargePointConsumer {

    private final UserRepository userRepository;
    private final ChargePointResultProducer chargePointResultProducer;


    @KafkaListener(id = "chargePoint", topics = "chargePoint")
    public void chargePoint(String kafkaMessage) {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());

        try {
            ChargePointDto chargePointDto = mapper.readValue(kafkaMessage, ChargePointDto.class);

            String id = chargePointDto.getId();
            String uuid = chargePointDto.getUuid();
            Integer point = chargePointDto.getPoint();

            User user = userRepository.findByUuid(uuid);
            user.chargePoint(point);
            userRepository.save(user);

            User result = userRepository.findByUuid(uuid);

            ChargePointResultDto chargePointResultDto =
                ChargePointResultDto.builder()
                    .id(id)
                    .uuid(uuid)
                    .total(result.getPoint())
                    .point(point)
                    .chargedDate(result.getUpdatedDate())
                    .build();

            chargePointResultProducer.sendChargePointResult(chargePointResultDto);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
