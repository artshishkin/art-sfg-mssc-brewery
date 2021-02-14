package net.shyshkin.study.beerorderservice.sm;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.shyshkin.study.beerorderservice.domain.BeerOrder;
import net.shyshkin.study.beerorderservice.domain.BeerOrderEventEnum;
import net.shyshkin.study.beerorderservice.domain.BeerOrderStatusEnum;
import net.shyshkin.study.beerorderservice.repositories.BeerOrderRepository;
import net.shyshkin.study.beerorderservice.services.BeerOrderManager;
import net.shyshkin.study.beerorderservice.sm.events.AllocateOrderSpringEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.messaging.Message;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.state.State;
import org.springframework.statemachine.support.StateMachineInterceptorAdapter;
import org.springframework.statemachine.transition.Transition;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class BeerOrderStateChangeInterceptor extends StateMachineInterceptorAdapter<BeerOrderStatusEnum, BeerOrderEventEnum> {

    private final BeerOrderRepository beerOrderRepository;
    private final ApplicationEventPublisher eventPublisher;

    @Transactional
    @Override
    public void preStateChange(State<BeerOrderStatusEnum, BeerOrderEventEnum> state,
                               Message<BeerOrderEventEnum> message,
                               Transition<BeerOrderStatusEnum, BeerOrderEventEnum> transition,
                               StateMachine<BeerOrderStatusEnum, BeerOrderEventEnum> stateMachine) {

        Optional
                .ofNullable(message)
                .flatMap(msg -> Optional.ofNullable(msg.getHeaders().get(BeerOrderManager.ORDER_ID_HEADER, UUID.class)))
                .ifPresent(orderId -> {
                    log.debug("Saving state for order id: `{}`. Status: `{}`", orderId, state.getId());

                    BeerOrder beerOrder = beerOrderRepository.getOne(orderId);
                    beerOrder.setOrderStatus(state.getId());
                    beerOrderRepository.save(beerOrder);
                });
    }

    @Transactional
    @Override
    public void postStateChange(
            State<BeerOrderStatusEnum, BeerOrderEventEnum> state,
            Message<BeerOrderEventEnum> message,
            Transition<BeerOrderStatusEnum, BeerOrderEventEnum> transition,
            StateMachine<BeerOrderStatusEnum, BeerOrderEventEnum> stateMachine) {

        BeerOrderStatusEnum beerOrderStatus = state.getId();
        if (beerOrderStatus == BeerOrderStatusEnum.VALIDATED) {
            Optional
                    .ofNullable(message)
                    .flatMap(msg -> Optional.ofNullable(msg.getHeaders().get(BeerOrderManager.ORDER_ID_HEADER, UUID.class)))
                    .map(AllocateOrderSpringEvent::new)
                    .ifPresentOrElse(
                            eventPublisher::publishEvent,
                            () -> log.error("Order Id not present in headers")
                    );
        }
    }

}
