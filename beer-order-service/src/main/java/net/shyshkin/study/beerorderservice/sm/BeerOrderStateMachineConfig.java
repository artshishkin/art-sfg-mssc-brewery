package net.shyshkin.study.beerorderservice.sm;

import lombok.RequiredArgsConstructor;
import net.shyshkin.study.beerorderservice.domain.BeerOrderEventEnum;
import net.shyshkin.study.beerorderservice.domain.BeerOrderStatusEnum;
import net.shyshkin.study.beerorderservice.sm.actions.AllocationFailureAction;
import net.shyshkin.study.beerorderservice.sm.actions.DeallocateOrderAction;
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
    private final Action<BeerOrderStatusEnum, BeerOrderEventEnum> allocateTransitionAction;
    private final Action<BeerOrderStatusEnum, BeerOrderEventEnum> allocateOrderAction;
    private final Action<BeerOrderStatusEnum, BeerOrderEventEnum> validationFailureAction;
    private final AllocationFailureAction allocationFailureAction;
    private final DeallocateOrderAction deallocateOrderAction;

    @Override
    public void configure(StateMachineStateConfigurer<BeerOrderStatusEnum, BeerOrderEventEnum> states) throws Exception {
        states.withStates()
                .initial(NEW)
                .states(allOf(BeerOrderStatusEnum.class))
                .end(DELIVERED)
                .end(PICKED_UP)
                .end(VALIDATION_EXCEPTION)
                .end(ALLOCATION_EXCEPTION)
                .end(DELIVERY_EXCEPTION)
                .end(CANCELED);
    }

    @Override
    public void configure(StateMachineTransitionConfigurer<BeerOrderStatusEnum, BeerOrderEventEnum> transitions) throws Exception {

        transitions
                .withExternal().source(NEW).target(VALIDATION_PENDING).event(VALIDATE_ORDER)
                .action(validateOrderAction)
                .and()

                .withExternal().source(VALIDATION_PENDING).target(VALIDATED).event(VALIDATION_PASSED)
//                .action(allocateTransitionAction)
                .and()

                .withExternal().source(VALIDATION_PENDING).target(VALIDATION_EXCEPTION).event(VALIDATION_FAILED)
                .action(validationFailureAction)
                .and()

                .withExternal().source(VALIDATED).target(ALLOCATION_PENDING)
                .event(ALLOCATE_ORDER)
                .action(allocateOrderAction)
                .and()

                .withExternal().source(ALLOCATION_PENDING).target(ALLOCATED).event(ALLOCATION_SUCCESS)
                .and()

                .withExternal().source(ALLOCATION_PENDING).target(ALLOCATION_EXCEPTION).event(ALLOCATION_FAILED)
                .action(allocationFailureAction)
                .and()

                .withExternal().source(ALLOCATION_PENDING).target(PENDING_INVENTORY).event(ALLOCATION_NO_INVENTORY)
                .and()

                .withExternal().source(ALLOCATED).target(PICKED_UP).event(BEERORDER_PICKED_UP)
                .and()

                .withExternal().source(VALIDATION_PENDING).target(CANCELED).event(CANCEL_ORDER).and()
                .withExternal().source(VALIDATED).target(CANCELED).event(CANCEL_ORDER).and()
                .withExternal().source(ALLOCATION_PENDING).target(CANCELED).event(CANCEL_ORDER).and()

                .withExternal().source(ALLOCATED).target(CANCELED).event(CANCEL_ORDER)
                .action(deallocateOrderAction);

    }
}
