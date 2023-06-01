package bts.users.user.service.consumer;

import bts.users.dto.ChargePointDto;
import bts.users.dto.ChargePointResultDto;
import bts.users.user.model.User;
import bts.users.user.repository.UserRepository;
import bts.users.user.service.producer.SendChargePointResult;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChargePointService {

    private final UserRepository userRepository;
    private final SendChargePointResult sendChargePointResult;


    @KafkaListener(id = "chargePoint", topics = "chargePoint")
    public void chargePoint(String kafkaMessage) {
        ObjectMapper mapper = new ObjectMapper();

        try {
            ChargePointDto chargePointDto = mapper.readValue(kafkaMessage, ChargePointDto.class);
            String id = chargePointDto.getId();
            Integer point = chargePointDto.getPoint();
            User user = userRepository.findByUuid(id.split("_")[0]);
            user.chargePoint(point);
            userRepository.save(user);

            User result = userRepository.findByUuid(id.split("_")[0]);
            ChargePointResultDto chargePointResultDto = new ChargePointResultDto();
            chargePointResultDto.setId(id);
            chargePointResultDto.setTotal(result.getPoint());
            chargePointResultDto.setPoint(point);
            chargePointResultDto.setChargeDate(result.getUpdatedDate());
            sendChargePointResult.sendResult(chargePointResultDto);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
