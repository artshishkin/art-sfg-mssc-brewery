package net.shyshkin.study.beerorderservice.services.listeners;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.shyshkin.study.beerdata.events.ValidateOrderResult;
import net.shyshkin.study.beerdata.queue.Queues;
import net.shyshkin.study.beerorderservice.services.BeerOrderManager;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class BeerOrderValidationResultListener {

    private final BeerOrderManager beerOrderManager;

    @JmsListener(destination = Queues.VALIDATE_ORDER_RESULT_QUEUE)
    public void listenResult(ValidateOrderResult validateOrderResult) {

        UUID orderId = validateOrderResult.getOrderId();
        boolean orderIsValid = validateOrderResult.isValid();
        log.debug("Validation: Order with id `{}` is {}", orderId, orderIsValid ? "Valid" : "Invalid");
        beerOrderManager.processValidationResult(orderId, orderIsValid);

    }
}
