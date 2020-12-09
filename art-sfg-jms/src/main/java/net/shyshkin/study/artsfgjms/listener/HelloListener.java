package net.shyshkin.study.artsfgjms.listener;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.shyshkin.study.artsfgjms.config.JmsConfig;
import net.shyshkin.study.artsfgjms.model.HelloWorldMessage;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import javax.jms.JMSException;
import javax.jms.Message;

@Slf4j
@Component
@RequiredArgsConstructor
public class HelloListener {

    private final JmsTemplate jmsTemplate;

    @JmsListener(destination = JmsConfig.MY_QUEUE)
    public void listen(@Payload HelloWorldMessage helloWorldMessage,
                       @Headers MessageHeaders headers,
                       Message message) {
        log.debug("I got a message {}", helloWorldMessage);
        log.debug("with headers {}", headers);

//        throw new RuntimeException("Testing jms_redelivered and JMSXDeliveryCount -> 6");
    }

    @JmsListener(destination = JmsConfig.SEND_RCV_QUEUE)
    public void listenAndRespond(@Payload HelloWorldMessage helloWorldMessage,
                                 @Headers MessageHeaders headers,
                                 Message message) throws JMSException {

        String payloadString = helloWorldMessage.getMessage() + "_back";

        HelloWorldMessage payloadMessage = HelloWorldMessage.builder()
                .message(payloadString)
                .id(helloWorldMessage.getId())
                .build();

        jmsTemplate.convertAndSend(message.getJMSReplyTo(), payloadMessage);
    }
}
