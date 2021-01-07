package net.shyshkin.study.beerinventoryservice.services.listeners;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.shyshkin.study.beerdata.dto.BeerOrderDto;
import net.shyshkin.study.beerdata.events.AllocateOrderRequest;
import net.shyshkin.study.beerdata.events.AllocateOrderResult;
import net.shyshkin.study.beerinventoryservice.services.AllocateOrder;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Component;

import static net.shyshkin.study.beerdata.queue.Queues.ALLOCATE_ORDER_QUEUE;
import static net.shyshkin.study.beerdata.queue.Queues.ALLOCATE_ORDER_RESULT_QUEUE;

@Slf4j
@Component
@RequiredArgsConstructor
public class AllocateOrderListener {

    private final AllocateOrder allocateOrder;

    @JmsListener(destination = ALLOCATE_ORDER_QUEUE)
    @SendTo(ALLOCATE_ORDER_RESULT_QUEUE)
    public AllocateOrderResult listen(AllocateOrderRequest allocateOrderRequest) {
        BeerOrderDto beerOrder = allocateOrderRequest.getBeerOrder();

        Boolean allocationSuccess = allocateOrder.allocateOrder(beerOrder);

        AllocateOrderResult result = AllocateOrderResult.builder()
                .beerOrder(beerOrder)
                .allocationError(!allocationSuccess)
                .pendingInventory(false)
                .build();
        log.debug("Allocation with Result {}", result);
        return result;
    }
}
