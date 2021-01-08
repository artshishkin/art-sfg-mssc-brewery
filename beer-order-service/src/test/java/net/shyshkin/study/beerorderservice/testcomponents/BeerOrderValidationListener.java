package net.shyshkin.study.beerorderservice.testcomponents;

import lombok.RequiredArgsConstructor;
import net.shyshkin.study.beerdata.events.ValidateOrderRequest;
import net.shyshkin.study.beerdata.events.ValidateOrderResult;
import net.shyshkin.study.beerdata.queue.Queues;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BeerOrderValidationListener {

    @JmsListener(destination = Queues.VALIDATE_ORDER_QUEUE)
    @SendTo(Queues.VALIDATE_ORDER_RESULT_QUEUE)
    public ValidateOrderResult validateOrder(ValidateOrderRequest validateOrderRequest) {

        boolean isValid = true;

        if ("fail-validation".equals(validateOrderRequest.getBeerOrder().getCustomerRef()))
            isValid = false;

        return ValidateOrderResult.builder()
                .valid(isValid)
                .orderId(validateOrderRequest.getBeerOrder().getId())
                .build();
    }
}
