package net.shyshkin.study.beerorderservice.sm.actions;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.shyshkin.study.beerorderservice.domain.BeerOrderEventEnum;
import net.shyshkin.study.beerorderservice.domain.BeerOrderStatusEnum;
import net.shyshkin.study.beerorderservice.sm.events.AllocateOrderSpringEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.action.Action;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

import static net.shyshkin.study.beerorderservice.services.BeerOrderManager.ORDER_ID_HEADER;

@Slf4j
@Component
@RequiredArgsConstructor
public class AllocateTransitionAction implements Action<BeerOrderStatusEnum, BeerOrderEventEnum> {

    private final ApplicationEventPublisher eventPublisher;

    @Override
    public void execute(StateContext<BeerOrderStatusEnum, BeerOrderEventEnum> context) {
        Optional
                .ofNullable(context.getMessage())
                .flatMap(msg -> Optional.ofNullable(msg.getHeaders().get(ORDER_ID_HEADER, UUID.class)))
                .map(AllocateOrderSpringEvent::new)
                .ifPresentOrElse(
                        eventPublisher::publishEvent,
                        () -> log.error("Order Id not present in headers")
                );
    }
}
