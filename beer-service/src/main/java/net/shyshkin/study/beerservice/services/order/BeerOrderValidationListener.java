package net.shyshkin.study.beerservice.services.order;

import lombok.RequiredArgsConstructor;
import net.shyshkin.study.beerdata.dto.BeerOrderDto;
import net.shyshkin.study.beerdata.events.ValidateOrderRequest;
import net.shyshkin.study.beerdata.events.ValidateOrderResult;
import net.shyshkin.study.beerdata.queue.Queues;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class BeerOrderValidationListener {

    private final BeerOrderValidator validator;

    @JmsListener(destination = Queues.VALIDATE_ORDER_QUEUE)
    @SendTo(Queues.VALIDATE_ORDER_RESULT_QUEUE)
    public ValidateOrderResult validateOrder(ValidateOrderRequest validateOrderRequest) {
        BeerOrderDto beerOrder = validateOrderRequest.getBeerOrder();

        UUID orderId = beerOrder.getId();

        Boolean orderValid = validator.validateOrder(beerOrder);

        return ValidateOrderResult.builder()
                .isValid(orderValid)
                .orderId(orderId)
                .build();
    }
}
