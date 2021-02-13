package net.shyshkin.study.beerorderservice.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.shyshkin.study.beerdata.dto.BeerDto;
import net.shyshkin.study.beerdata.dto.BeerOrderDto;
import net.shyshkin.study.beerdata.dto.BeerOrderLineDto;
import net.shyshkin.study.beerdata.dto.BeerPagedList;
import net.shyshkin.study.beerorderservice.bootstrap.BeerOrderBootStrap;
import net.shyshkin.study.beerorderservice.domain.Customer;
import net.shyshkin.study.beerorderservice.repositories.CustomerRepository;
import net.shyshkin.study.beerorderservice.services.beerservice.BeerService;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Function;

@Service
@Slf4j
@Profile("!test")
@RequiredArgsConstructor
public class TastingRoomService {

    private final CustomerRepository customerRepository;
    private final BeerOrderService beerOrderService;
    private final BeerService beerService;

    @Transactional
    @Scheduled(fixedRate = 2000) //run every 2 seconds
    public void placeTastingRoomOrder() {

        customerRepository
                .findByCustomerName(BeerOrderBootStrap.TASTING_ROOM)
                .ifPresentOrElse(
                        this::doPlaceOrder,
                        () -> log.error("Too many or too few tasting room customers found")
                );
    }

    private void doPlaceOrder(Customer customer) {

        getRandomBeer()
                .map(beerId ->
                        BeerOrderLineDto.builder()
                                .beerId(beerId)
//                    .upc(beerToOrder) //todo May be we need UPC
                                .orderQuantity(ThreadLocalRandom.current().nextInt(5) + 1) //todo externalize value to property
                                .build())
                .map(List::of)
                .map(beerOrderLineSet ->
                        BeerOrderDto.builder()
                                .customerId(customer.getId())
                                .customerRef(UUID.randomUUID().toString())
                                .beerOrderLines(beerOrderLineSet)
                                .build())
                .ifPresentOrElse(
                        beerOrderDto -> beerOrderService.placeOrder(customer.getId(), beerOrderDto),
                        () -> log.info("Could not place order for Customer: {}", customer));
    }

    private Optional<UUID> getRandomBeer() {
        Optional<BeerPagedList> listOfBeers = beerService.getListOfBeers();

        if (listOfBeers.isEmpty())
            log.debug("Failed to get list of beers");

        Function<List<BeerDto>, BeerDto> chooseRandomBeer = list -> list.get(ThreadLocalRandom.current().nextInt(list.size()));

        return listOfBeers
                .map(BeerPagedList::getContent)
                .map(chooseRandomBeer)
                .map(BeerDto::getId);
    }
}
