package net.shyshkin.study.beerinventoryservice.services.listeners;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.shyshkin.study.beerdata.dto.BeerOrderDto;
import net.shyshkin.study.beerdata.events.AllocateOrderRequest;
import net.shyshkin.study.beerinventoryservice.services.AllocateOrder;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import static net.shyshkin.study.beerdata.queue.Queues.DEALLOCATE_ORDER_QUEUE;

@Slf4j
@Component
@RequiredArgsConstructor
public class DeallocateOrderListener {

    private final AllocateOrder allocateOrder;

    @JmsListener(destination = DEALLOCATE_ORDER_QUEUE)
    public void listen(AllocateOrderRequest request) {
        BeerOrderDto beerOrderDto = request.getBeerOrder();
        allocateOrder.deallocateOrder(beerOrderDto);
    }
}
