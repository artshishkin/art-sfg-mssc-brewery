package net.shyshkin.study.beerorderservice.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.shyshkin.study.beerdata.dto.BeerOrderDto;
import net.shyshkin.study.beerorderservice.domain.BeerOrder;
import net.shyshkin.study.beerorderservice.domain.BeerOrderEventEnum;
import net.shyshkin.study.beerorderservice.domain.BeerOrderStatusEnum;
import net.shyshkin.study.beerorderservice.repositories.BeerOrderRepository;
import net.shyshkin.study.beerorderservice.sm.BeerOrderStateChangeInterceptor;
import net.shyshkin.study.beerorderservice.sm.events.AllocateOrderSpringEvent;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.config.StateMachineFactory;
import org.springframework.statemachine.support.DefaultStateMachineContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.Optional;
import java.util.UUID;

import static net.shyshkin.study.beerorderservice.domain.BeerOrderEventEnum.*;

@Slf4j
@Service
@RequiredArgsConstructor
//@Transactional
public class BeerOrderManagerImpl implements BeerOrderManager {

    private final StateMachineFactory<BeerOrderStatusEnum, BeerOrderEventEnum> stateMachineFactory;
    private final BeerOrderRepository beerOrderRepository;
    private final BeerOrderStateChangeInterceptor beerOrderStateChangeInterceptor;

    @Transactional
    @Override
    public BeerOrder newBeerOrder(BeerOrder beerOrder) {
        beerOrder.setId(null);
        beerOrder.setOrderStatus(BeerOrderStatusEnum.NEW);
        BeerOrder savedOrder = beerOrderRepository.saveAndFlush(beerOrder);

        sendBeerOrderEvent(beerOrder, VALIDATE_ORDER);
        return savedOrder;
    }

    //    @Transactional
    @Override
    public void processValidationResult(UUID orderId, boolean isValid) {

        log.debug("processValidationResult UUID is {}", orderId);
        Optional<BeerOrder> orderOptional = beerOrderRepository.findById(orderId);

        BeerOrder order = orderOptional
                .orElseThrow(() -> new EntityNotFoundException("Order with id `" + orderId + "` not found among total of " + beerOrderRepository.count() + " orders"));

        if (isValid) {
            sendBeerOrderEvent(order, VALIDATION_PASSED);
        } else
            sendBeerOrderEvent(order, VALIDATION_FAILED);
    }

    @EventListener(AllocateOrderSpringEvent.class)
    @Transactional
//    @Async
    public void sendEventToAllocateOrderEventListener(AllocateOrderSpringEvent event) {
        UUID orderId = (UUID) event.getSource();
        // TODO: 07.01.2021 Something weired from SFG - It needs to be refactored
        log.debug("Sending Event To ALLOCATE_ORDER {}", orderId);
        BeerOrder validatedOrder = beerOrderRepository.findOneById(orderId);
        sendBeerOrderEvent(validatedOrder, ALLOCATE_ORDER);
    }

    @Override
    public void beerOrderAllocationPassed(BeerOrderDto beerOrder) {
        processAllocationResult(beerOrder, ALLOCATION_SUCCESS);
        updateAllocatedQty(beerOrder);
    }

    @Override
    public void beerOrderAllocationPendingInventory(BeerOrderDto beerOrder) {
        processAllocationResult(beerOrder, ALLOCATION_NO_INVENTORY);
        updateAllocatedQty(beerOrder);
    }

    @Override
    public void beerOrderAllocationFailed(BeerOrderDto beerOrder) {
        processAllocationResult(beerOrder, ALLOCATION_FAILED);
    }

    @Override
    public void beerOrderPickedUp(UUID orderId) {
        beerOrderRepository
                .findById(orderId)
                .ifPresentOrElse(
                        beerOrderRetrieved -> sendBeerOrderEvent(beerOrderRetrieved, BEERORDER_PICKED_UP),
                        () -> log.error("Order with id `{}` not found", orderId));
    }

    @Override
    public void cancelOrder(UUID orderId) {
        beerOrderRepository
                .findById(orderId)
                .ifPresentOrElse(
                        beerOrderRetrieved -> sendBeerOrderEvent(beerOrderRetrieved, CANCEL_ORDER),
                        () -> log.error("Order with id `{}` not found", orderId));
    }

    private void processAllocationResult(BeerOrderDto beerOrderDto, BeerOrderEventEnum allocationEvent) {
        UUID orderId = beerOrderDto.getId();
        Optional<BeerOrder> orderOptional = beerOrderRepository.findById(orderId);

        orderOptional
                .ifPresentOrElse(order -> {
                            log.debug("After receiving Allocation result for Order `{}` sending Event {}", orderId, allocationEvent);
                            sendBeerOrderEvent(order, allocationEvent);
                        },
                        () -> log.error("Error while allocation order `{}` not found", orderId));
    }

    private void updateAllocatedQty(BeerOrderDto beerOrderDto) {
        UUID orderId = beerOrderDto.getId();

        beerOrderRepository.findById(orderId).ifPresentOrElse(beerOrder -> {
                    beerOrder.getBeerOrderLines().forEach(beerOrderLine -> {
                        beerOrderDto.getBeerOrderLines().forEach(beerOrderLineDto -> {
                            if (beerOrderLineDto.getId().equals(beerOrderLine.getId())) {
                                beerOrderLine.setQuantityAllocated(beerOrderLineDto.getQuantityAllocated());
                                beerOrderLine.setUpc(beerOrderLineDto.getUpc());
                            }
                        });
                    });
                    beerOrderRepository.save(beerOrder);
                },
                () -> log.error("Error while allocation order `{}` not found", orderId));
    }

    private void sendBeerOrderEvent(BeerOrder beerOrder, BeerOrderEventEnum eventEnum) {
        StateMachine<BeerOrderStatusEnum, BeerOrderEventEnum> sm = build(beerOrder);
        Message<BeerOrderEventEnum> msg = MessageBuilder
                .withPayload(eventEnum)
                .setHeader(BeerOrderManager.ORDER_ID_HEADER, beerOrder.getId())
                .build();
        sm.sendEvent(msg);
    }

    private StateMachine<BeerOrderStatusEnum, BeerOrderEventEnum> build(BeerOrder beerOrder) {
        StateMachine<BeerOrderStatusEnum, BeerOrderEventEnum> sm = stateMachineFactory.getStateMachine(beerOrder.getId());
        sm.stop();
        sm.getStateMachineAccessor()
                .doWithAllRegions(sma -> {
                    sma.addStateMachineInterceptor(beerOrderStateChangeInterceptor);
                    sma.resetStateMachine(new DefaultStateMachineContext<>(beerOrder.getOrderStatus(), null, null, null));
                });
        sm.start();
        return sm;
    }
}
