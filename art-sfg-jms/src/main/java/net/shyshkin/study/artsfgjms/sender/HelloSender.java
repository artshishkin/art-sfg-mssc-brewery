package net.shyshkin.study.artsfgjms.sender;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.shyshkin.study.artsfgjms.config.JmsConfig;
import net.shyshkin.study.artsfgjms.model.HelloWorldMessage;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.TextMessage;
import java.time.LocalDateTime;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class HelloSender {

    private final JmsTemplate jmsTemplate;
    private final ObjectMapper objectMapper;

    @Scheduled(fixedRate = 2000)
    public void sendMessage() {

        HelloWorldMessage message = HelloWorldMessage.builder()
                .id(UUID.randomUUID())
                .message("Hello world " + LocalDateTime.now())
                .build();

        jmsTemplate.convertAndSend(JmsConfig.MY_QUEUE, message);
    }

    @Scheduled(fixedRate = 2000)
    public void sendAndReceiveMessage() throws JsonProcessingException, JMSException {

        HelloWorldMessage message = HelloWorldMessage.builder()
                .id(UUID.randomUUID())
                .message("Hello " + LocalDateTime.now())
                .build();

        log.debug("Sending {}", message.getMessage());

        String jsonMessage = objectMapper.writeValueAsString(message);

        Message receivedMessage = jmsTemplate.sendAndReceive(JmsConfig.SEND_RCV_QUEUE, session -> {
            TextMessage textMessage = session.createTextMessage(jsonMessage);
            textMessage.setStringProperty("_type", HelloWorldMessage.class.getName());
            return textMessage;
        });

        log.debug("Received message {}", receivedMessage.getBody(String.class));
    }
}
