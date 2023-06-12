package bts.users.user.messageQueue.producer;

import bts.users.dto.ChargePointResultDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChargePointResultProducer {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void sendChargePointResult(ChargePointResultDto chargePointResultDto) {

        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());

        try {
            String data = mapper.writeValueAsString(chargePointResultDto);
            kafkaTemplate.send("chargePointResult", data);
        } catch (Exception e) {
            log.info(e.toString());
        }
    }
}
