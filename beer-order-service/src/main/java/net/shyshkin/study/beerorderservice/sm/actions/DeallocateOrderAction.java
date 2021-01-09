package net.shyshkin.study.beerorderservice.sm.actions;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.shyshkin.study.beerdata.events.DeallocateOrderRequest;
import net.shyshkin.study.beerorderservice.domain.BeerOrderEventEnum;
import net.shyshkin.study.beerorderservice.domain.BeerOrderStatusEnum;
import net.shyshkin.study.beerorderservice.repositories.BeerOrderRepository;
import net.shyshkin.study.beerorderservice.web.mappers.BeerOrderMapper;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.action.Action;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

import static net.shyshkin.study.beerdata.queue.Queues.DEALLOCATE_ORDER_QUEUE;
import static net.shyshkin.study.beerorderservice.services.BeerOrderManager.ORDER_ID_HEADER;

@Slf4j
@Component
@RequiredArgsConstructor
public class DeallocateOrderAction implements Action<BeerOrderStatusEnum, BeerOrderEventEnum> {

    private final JmsTemplate jmsTemplate;
    private final BeerOrderRepository beerOrderRepository;
    private final BeerOrderMapper mapper;

    @Override
    public void execute(StateContext<BeerOrderStatusEnum, BeerOrderEventEnum> context) {
        Optional
                .ofNullable(context.getMessage())
                .flatMap(msg -> Optional.ofNullable(msg.getHeaders().get(ORDER_ID_HEADER, UUID.class)))
                .map(beerOrderRepository::getOne)
                .map(mapper::beerOrderToDto)
                .map(DeallocateOrderRequest::new)
                .ifPresent(orderRequest -> {
                    jmsTemplate.convertAndSend(DEALLOCATE_ORDER_QUEUE, orderRequest);
                    log.debug("Sent Deallocate Request to queue for order id `{}`", orderRequest.getBeerOrder().getId());
                });
    }
}
