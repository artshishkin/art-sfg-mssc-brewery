package net.shyshkin.study.beerorderservice.sm.actions;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.shyshkin.study.beerdata.events.AllocationFailureEvent;
import net.shyshkin.study.beerdata.queue.Queues;
import net.shyshkin.study.beerorderservice.domain.BeerOrderEventEnum;
import net.shyshkin.study.beerorderservice.domain.BeerOrderStatusEnum;
import net.shyshkin.study.beerorderservice.services.BeerOrderManager;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.action.Action;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class AllocationFailureAction implements Action<BeerOrderStatusEnum, BeerOrderEventEnum> {

    private final JmsTemplate jmsTemplate;

    @Override
    public void execute(StateContext<BeerOrderStatusEnum, BeerOrderEventEnum> context) {
        UUID orderId = (UUID) context.getMessageHeader(BeerOrderManager.ORDER_ID_HEADER);
        log.error("Compensating Transaction... Allocation Failed: " + orderId);
        jmsTemplate.convertAndSend(Queues.ALLOCATION_FAILURE_QUEUE,
                AllocationFailureEvent.builder().orderId(orderId).build());
    }
}
