package net.shyshkin.study.beerservice.services.brewing;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.shyshkin.study.beerdata.dto.BeerDto;
import net.shyshkin.study.beerdata.events.BrewBeerEvent;
import net.shyshkin.study.beerdata.events.NewInventoryEvent;
import net.shyshkin.study.beerservice.domain.Beer;
import net.shyshkin.study.beerservice.repositories.BeerRepository;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static net.shyshkin.study.beerservice.config.JmsConfig.BREWING_REQUEST_QUEUE;
import static net.shyshkin.study.beerservice.config.JmsConfig.NEW_INVENTORY_QUEUE;

@Slf4j
@Service
@RequiredArgsConstructor
public class BrewBeerListener {

    private final BeerRepository beerRepository;

    @Transactional
    @JmsListener(destination = BREWING_REQUEST_QUEUE)
    @SendTo(NEW_INVENTORY_QUEUE)
    public NewInventoryEvent listen(BrewBeerEvent brewBeerEvent) {

        BeerDto beerDto = brewBeerEvent.getBeerDto();

        Beer beer = beerRepository.getOne(beerDto.getId());

        beerDto.setQuantityOnHand(beer.getQuantityToBrew());

        log.debug("Brewed beer {} : QOH: {}", beer.getMinOnHand(), beerDto.getQuantityOnHand());

        return new NewInventoryEvent(beerDto);
    }
}
