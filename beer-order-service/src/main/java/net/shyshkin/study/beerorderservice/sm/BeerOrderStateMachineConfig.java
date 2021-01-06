package net.shyshkin.study.beerorderservice.sm;

import lombok.RequiredArgsConstructor;
import net.shyshkin.study.beerorderservice.domain.BeerOrderEventEnum;
import net.shyshkin.study.beerorderservice.domain.BeerOrderStatusEnum;
import org.springframework.context.annotation.Configuration;
import org.springframework.statemachine.action.Action;
import org.springframework.statemachine.config.EnableStateMachineFactory;
import org.springframework.statemachine.config.StateMachineConfigurerAdapter;
import org.springframework.statemachine.config.builders.StateMachineStateConfigurer;
import org.springframework.statemachine.config.builders.StateMachineTransitionConfigurer;

import static java.util.EnumSet.allOf;
import static net.shyshkin.study.beerorderservice.domain.BeerOrderEventEnum.*;
import static net.shyshkin.study.beerorderservice.domain.BeerOrderStatusEnum.*;

@Configuration
@EnableStateMachineFactory
@RequiredArgsConstructor
public class BeerOrderStateMachineConfig extends StateMachineConfigurerAdapter<BeerOrderStatusEnum, BeerOrderEventEnum> {

    private final Action<BeerOrderStatusEnum, BeerOrderEventEnum> validateOrderAction;

    @Override
    public void configure(StateMachineStateConfigurer<BeerOrderStatusEnum, BeerOrderEventEnum> states) throws Exception {
        states.withStates()
                .initial(NEW)
                .states(allOf(BeerOrderStatusEnum.class))
                .end(DELIVERED)
                .end(PICKED_UP)
                .end(VALIDATION_EXCEPTION)
                .end(ALLOCATION_EXCEPTION)
                .end(DELIVERY_EXCEPTION);
    }

    @Override
    public void configure(StateMachineTransitionConfigurer<BeerOrderStatusEnum, BeerOrderEventEnum> transitions) throws Exception {

        transitions
                .withExternal().source(NEW).target(VALIDATION_PENDING).event(VALIDATE_ORDER)
                .action(validateOrderAction)
                .and()

                .withExternal().source(NEW).target(VALIDATED).event(VALIDATION_PASSED)
                .and()

                .withExternal().source(NEW).target(VALIDATION_EXCEPTION).event(VALIDATION_FAILED);

    }
}
