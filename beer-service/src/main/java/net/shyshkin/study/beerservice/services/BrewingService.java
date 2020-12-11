package net.shyshkin.study.beerservice.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.shyshkin.study.beerservice.events.BrewBeerEvent;
import net.shyshkin.study.beerservice.repositories.BeerRepository;
import net.shyshkin.study.beerservice.services.inventory.BeerInventoryService;
import net.shyshkin.study.beerservice.web.mappers.BeerMapper;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import static net.shyshkin.study.beerservice.config.JmsConfig.BREWING_REQUEST_QUEUE;

@Slf4j
@Service
@RequiredArgsConstructor
public class BrewingService {
    private final BeerRepository beerRepository;
    private final BeerInventoryService inventoryService;
    private final BeerMapper beerMapper;
    private final JmsTemplate jmsTemplate;

    @Scheduled(fixedRateString = "PT5S")
    public void checkForLowInventory() {
        beerRepository
                .findAll()
                .parallelStream()
                .peek(beer ->
                        log.debug("Min on hand is {}. Inventory is {}",
                                beer.getMinOnHand(),
                                inventoryService.getOnHandInventory(beer.getId())))
                .filter(beer -> beer.getMinOnHand() >= inventoryService.getOnHandInventory(beer.getId()))
                .map(beerMapper::asBeerDto)
                .map(BrewBeerEvent::new)
                .forEach(event -> jmsTemplate.convertAndSend(BREWING_REQUEST_QUEUE, event));
    }
}
