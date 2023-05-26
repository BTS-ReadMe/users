package bts.users.user.service.consumer;

import bts.users.user.dto.ChargePointDto;
import bts.users.user.model.User;
import bts.users.user.repository.UserRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChargePointImpl implements ChargePoint {

    private final UserRepository userRepository;

    @Override
    @KafkaListener(id = "chargePoint",topics = "chargePoint")
    public void saveChargePoint(String kafkaMessage) {
        ObjectMapper mapper = new ObjectMapper();

        try {
            ChargePointDto chargePointDto = mapper.readValue(kafkaMessage, ChargePointDto.class);
            
            String uuid = chargePointDto.getUuid();
            User user = userRepository.findByUuid(uuid);
            user.chargePoint(chargePointDto.getPoint());
            userRepository.save(user);

        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }


    }
}
