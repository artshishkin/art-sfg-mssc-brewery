package net.shyshkin.study.beerorderservice.sm.actions;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.shyshkin.study.beerdata.events.ValidateOrderRequest;
import net.shyshkin.study.beerorderservice.config.JmsConfig;
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

import static net.shyshkin.study.beerorderservice.services.BeerOrderManager.ORDER_ID_HEADER;

@Slf4j
@Component
@RequiredArgsConstructor
public class ValidateOrderAction implements Action<BeerOrderStatusEnum, BeerOrderEventEnum> {

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
                .map(ValidateOrderRequest::new)
                .ifPresent(orderRequest -> {
                    jmsTemplate.convertAndSend(JmsConfig.VALIDATE_ORDER_QUEUE, orderRequest);
                    log.debug("Sent Validation Request to queue for order id `{}`", orderRequest.getBeerOrder().getId());
                });
    }
}
