package net.shyshkin.study.beerinventoryfailover.web.controllers;

import net.shyshkin.study.beerdata.dto.BeerInventoryDto;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.time.OffsetDateTime;
import java.util.UUID;

@RestController
public class FailoverController {

    @GetMapping("inventory-failover")
    public Flux<BeerInventoryDto> failoverInventory() {
        return Flux.just(
                BeerInventoryDto.builder()
                        .id(UUID.randomUUID())
                        .beerId(UUID.fromString("00000000-0000-0000-0000-000000000000"))
                        .quantityOnHand(999)
                        .createdDate(OffsetDateTime.now())
                        .lastModifiedDate(OffsetDateTime.now())
                        .build());
    }

}
