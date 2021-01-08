package net.shyshkin.study.beerorderservice.sm.actions;

import lombok.extern.slf4j.Slf4j;
import net.shyshkin.study.beerorderservice.domain.BeerOrderEventEnum;
import net.shyshkin.study.beerorderservice.domain.BeerOrderStatusEnum;
import net.shyshkin.study.beerorderservice.services.BeerOrderManager;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.action.Action;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Slf4j
@Component
public class ValidationFailureAction implements Action<BeerOrderStatusEnum, BeerOrderEventEnum> {

    @Override
    public void execute(StateContext<BeerOrderStatusEnum, BeerOrderEventEnum> context) {
        UUID orderId = (UUID) context.getMessageHeader(BeerOrderManager.ORDER_ID_HEADER);
        log.error("Compensating Transaction... Validation Failed: " + orderId);
    }
}
