package net.shyshkin.study.beertastingroomservice.services;

import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import net.shyshkin.study.beerdata.dto.*;
import net.shyshkin.study.beertastingroomservice.services.beerservice.BeerService;
import net.shyshkin.study.beertastingroomservice.services.orderservice.OrderService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Function;

@Service
@Slf4j
@RequiredArgsConstructor
public class TastingRoomService {

    public static final String TASTING_ROOM = "Tasting Room";

    private final BeerService beerService;
    private final OrderService orderService;

    @Setter
    @Value("${net.shyshkin.tasting-room.max-quantity}")
    private Integer maxQuantity;

    @Scheduled(fixedRateString = "${net.shyshkin.tasting-room.rate}")
    public void placeTastingRoomOrder() {

        log.debug("Start placing order");

        orderService.getAllCustomers()
                .stream()
                .filter(customerDto -> TASTING_ROOM.equals(customerDto.getName()))
                .forEach(this::doPlaceOrder);
    }

    private void doPlaceOrder(CustomerDto customer) {

        getRandomBeer()
                .map(beerId ->
                        BeerOrderLineDto.builder()
                                .beerId(beerId)
                                .orderQuantity(ThreadLocalRandom.current().nextInt(maxQuantity) + 1)
                                .build())
                .map(List::of)
                .map(beerOrderLineSet ->
                        BeerOrderDto.builder()
                                .customerId(customer.getId())
                                .customerRef(UUID.randomUUID().toString())
                                .beerOrderLines(beerOrderLineSet)
                                .build())
                .flatMap(beerOrderDto -> orderService.placeOrder(customer.getId(), beerOrderDto))
                .ifPresentOrElse(
                        beerOrderDto -> log.debug("Order successfully placed: {} ", beerOrderDto),
                        () -> log.info("Could not place order for Customer: {} with id: {} ", customer, customer != null ? customer.getId() : null));
    }

    private Optional<UUID> getRandomBeer() {
        Optional<BeerPagedList> listOfBeers = beerService.getListOfBeers();

        if (listOfBeers.isEmpty())
            log.debug("Failed to get list of beers");

        Function<List<BeerDto>, BeerDto> chooseRandomBeer = list -> list.get(ThreadLocalRandom.current().nextInt(list.size()));

        return listOfBeers
                .map(BeerPagedList::getContent)
                .filter(list -> !list.isEmpty())
                .map(chooseRandomBeer)
                .map(BeerDto::getId);
    }
}
