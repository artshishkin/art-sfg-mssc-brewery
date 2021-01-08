package net.shyshkin.study.beerorderservice.testcomponents;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.shyshkin.study.beerdata.dto.BeerOrderDto;
import net.shyshkin.study.beerdata.events.AllocateOrderRequest;
import net.shyshkin.study.beerdata.events.AllocateOrderResult;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Component;

import static net.shyshkin.study.beerdata.queue.Queues.ALLOCATE_ORDER_QUEUE;
import static net.shyshkin.study.beerdata.queue.Queues.ALLOCATE_ORDER_RESULT_QUEUE;

@Slf4j
@Component
@RequiredArgsConstructor
public class AllocateOrderListener {

    @JmsListener(destination = ALLOCATE_ORDER_QUEUE)
    @SendTo(ALLOCATE_ORDER_RESULT_QUEUE)
    public AllocateOrderResult listen(AllocateOrderRequest request) {
        BeerOrderDto beerOrderDto = request.getBeerOrder();

        if ("fail-allocation".equals(beerOrderDto.getCustomerRef())) {
            return AllocateOrderResult.builder()
                    .beerOrderDto(beerOrderDto)
                    .allocationError(true)
                    .build();
        }

        boolean partialAllocation = "partial-allocation".equals(beerOrderDto.getCustomerRef());

        beerOrderDto.getBeerOrderLines().forEach(line -> {
            Integer orderQuantity = line.getOrderQuantity();
            if (partialAllocation) orderQuantity--;
            line.setQuantityAllocated(orderQuantity);
        });

        AllocateOrderResult result = AllocateOrderResult.builder()
                .beerOrderDto(beerOrderDto)
                .pendingInventory(partialAllocation)
                .build();

        log.debug("Allocation with Result {}", result);
        return result;
    }
}
