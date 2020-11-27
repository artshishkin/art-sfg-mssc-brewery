package net.shyshkin.study.beerservice.bootstrap;

import lombok.RequiredArgsConstructor;
import net.shyshkin.study.beerservice.domain.Beer;
import net.shyshkin.study.beerservice.repositories.BeerRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Component
@RequiredArgsConstructor
public class BeerLoader implements CommandLineRunner {

    private final BeerRepository beerRepository;

    @Override
    public void run(String... args) throws Exception {
        if (beerRepository.count() == 0)
            bootstrapBeers();
    }

    private void bootstrapBeers() {
        List<Beer> stubBeers = IntStream.rangeClosed(1, 9)
                .mapToObj(this::stubBeer)
                .collect(Collectors.toList());
        beerRepository.saveAll(stubBeers);
    }

    private Beer stubBeer(int index) {
        return Beer.builder()
                .beerName("BeerName" + index)
                .beerStyle("BeerStyle" + index)
                .price(BigDecimal.valueOf(111 * (index)))
                .upc(1111L * index)
                .minOnHand(111 * index)
                .quantityToBrew(123 * index)
                .build();
    }
}