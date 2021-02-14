package net.shyshkin.study.beerorderservice.sm.events;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEvent;

import java.util.UUID;

@Slf4j
public class AllocateOrderSpringEvent extends ApplicationEvent {
    /**
     * Create a new {@code ApplicationEvent}.
     *
     * @param source the object on which the event initially occurred or with
     *               which the event is associated (never {@code null})
     */
    public AllocateOrderSpringEvent(UUID source) {
        super(source);
        log.debug("Create AllocateOrderSpringEvent with source {}", source);
    }
}
