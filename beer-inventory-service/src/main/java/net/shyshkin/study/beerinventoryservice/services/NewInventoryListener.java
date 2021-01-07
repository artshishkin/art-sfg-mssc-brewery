package net.shyshkin.study.beerinventoryservice.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.shyshkin.study.beerdata.dto.BeerDto;
import net.shyshkin.study.beerdata.events.NewInventoryEvent;
import net.shyshkin.study.beerinventoryservice.domain.BeerInventory;
import net.shyshkin.study.beerinventoryservice.repositories.BeerInventoryRepository;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Service;

import static net.shyshkin.study.beerdata.queue.Queues.NEW_INVENTORY_QUEUE;

@Slf4j
@Service
@RequiredArgsConstructor
public class NewInventoryListener {

    private final BeerInventoryRepository repository;

    @JmsListener(destination = NEW_INVENTORY_QUEUE)
    public void listen(NewInventoryEvent inventoryEvent) {

        log.debug("NewInventoryEvent: {}", inventoryEvent);

        BeerDto beerDto = inventoryEvent.getBeerDto();

        BeerInventory beerInventory = BeerInventory.builder()
                .beerId(beerDto.getId())
                .upc(beerDto.getUpc())
                .quantityOnHand(beerDto.getQuantityOnHand())
                .build();

        BeerInventory savedInventory = repository.save(beerInventory);

        log.debug("Saved new Inventory with id: {}", savedInventory.getId());
    }
}
