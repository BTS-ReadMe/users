package bts.users.user.service.producer;

import bts.users.dto.ChargePointResultDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SendChargePointResult {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void sendResult(ChargePointResultDto chargePointResultDto) {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        String data = "";

        try {
            data = mapper.writeValueAsString(chargePointResultDto);
        } catch (Exception e) {
            return;
        }

        kafkaTemplate.send("chargePointResult", data);
    }
}
