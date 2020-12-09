package net.shyshkin.study.artsfgjms.sender;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.shyshkin.study.artsfgjms.config.JmsConfig;
import net.shyshkin.study.artsfgjms.model.HelloWorldMessage;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class HelloSender {

    private final JmsTemplate jmsTemplate;

    @Scheduled(fixedRate = 2000)
    public void sendMessage() {
        log.debug("I am sending a message");

        HelloWorldMessage message = HelloWorldMessage.builder()
                .id(UUID.randomUUID())
                .message("Hello world " + LocalDateTime.now())
                .build();

        jmsTemplate.convertAndSend(JmsConfig.MY_QUEUE, message);

        log.debug("Message {} sent", message);
    }
}
