package net.shyshkin.study.beerorderservice.services.listeners;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.shyshkin.study.beerdata.dto.BeerOrderDto;
import net.shyshkin.study.beerdata.events.AllocateOrderResult;
import net.shyshkin.study.beerdata.queue.Queues;
import net.shyshkin.study.beerorderservice.services.BeerOrderManager;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class BeerOrderAllocationResultListener {

    private final BeerOrderManager beerOrderManager;

    @JmsListener(destination = Queues.ALLOCATE_ORDER_RESULT_QUEUE)
    public void listenResult(AllocateOrderResult allocateOrderResult) {

        BeerOrderDto beerOrderDto = allocateOrderResult.getBeerOrderDto();
        UUID orderId = beerOrderDto.getId();
        boolean orderHasAllocationOrder = allocateOrderResult.isAllocationError();
        boolean pendingInventory = allocateOrderResult.isPendingInventory();
        log.debug("Allocation: Order with id `{}` has {} Error{}",
                orderId,
                orderHasAllocationOrder ? "an" : "NO",
                pendingInventory ? ", inventory is PENDING" : "");

        if (orderHasAllocationOrder){
            beerOrderManager.beerOrderAllocationFailed(beerOrderDto);
            return;
        }
        if (pendingInventory){
            beerOrderManager.beerOrderAllocationPendingInventory(beerOrderDto);
            return;
        }
        beerOrderManager.beerOrderAllocationPassed(beerOrderDto);
    }
}
