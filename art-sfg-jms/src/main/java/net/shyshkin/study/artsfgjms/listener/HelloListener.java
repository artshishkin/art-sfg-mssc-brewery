package net.shyshkin.study.artsfgjms.listener;

import lombok.extern.slf4j.Slf4j;
import net.shyshkin.study.artsfgjms.config.JmsConfig;
import net.shyshkin.study.artsfgjms.model.HelloWorldMessage;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import javax.jms.Message;

@Slf4j
@Component
public class HelloListener {

    @JmsListener(destination = JmsConfig.MY_QUEUE)
    public void listen(@Payload HelloWorldMessage helloWorldMessage,
                       @Headers MessageHeaders headers,
                       Message message) {
        log.debug("I got a message {}", helloWorldMessage);
        log.debug("with headers {}", headers);

//        throw new RuntimeException("Testing jms_redelivered and JMSXDeliveryCount -> 6");
    }
}
