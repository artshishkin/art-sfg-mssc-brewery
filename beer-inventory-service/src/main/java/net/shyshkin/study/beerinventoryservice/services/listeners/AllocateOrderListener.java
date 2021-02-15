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
import org.springframework.transaction.annotation.Transactional;

import static net.shyshkin.study.beerdata.queue.Queues.ALLOCATE_ORDER_QUEUE;
import static net.shyshkin.study.beerdata.queue.Queues.ALLOCATE_ORDER_RESULT_QUEUE;

@Slf4j
@Component
@RequiredArgsConstructor
public class AllocateOrderListener {

    private final AllocateOrder allocateOrder;

    @JmsListener(destination = ALLOCATE_ORDER_QUEUE)
    @SendTo(ALLOCATE_ORDER_RESULT_QUEUE)
    @Transactional
    public AllocateOrderResult listen(AllocateOrderRequest request) {
        AllocateOrderResult.AllocateOrderResultBuilder builder = AllocateOrderResult.builder();
        builder.allocationError(false);

//        try {
            BeerOrderDto beerOrderDto = request.getBeerOrder();
            builder.beerOrderDto(beerOrderDto);

            Boolean allocationSuccess = allocateOrder.allocateOrder(beerOrderDto);

            builder.pendingInventory(!allocationSuccess);

//        } catch (Exception exception) {
//            log.error("Allocation failed for order {}", request.getBeerOrder().getId(), exception);
//            builder.allocationError(true);
//        }
        AllocateOrderResult result = builder.build();
        log.debug("Allocation with Result {}", result);
        return result;
    }
}
