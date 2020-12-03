package net.shyshkin.study.beerservice.bootstrap;

import lombok.RequiredArgsConstructor;
import net.shyshkin.study.beerservice.domain.Beer;
import net.shyshkin.study.beerservice.repositories.BeerRepository;
import org.springframework.boot.CommandLineRunner;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

//@Component
@RequiredArgsConstructor
public class BeerLoader implements CommandLineRunner {

    private final BeerRepository beerRepository;

    private static final String BEER_UPC_PATTERN = "06312342003%02d";

    @Override
    public void run(String... args) throws Exception {
        if (beerRepository.count() == 0)
            bootstrapBeers();
    }

    private void bootstrapBeers() {
        List<Beer> stubBeers = stubBeerList(9);
        beerRepository.saveAll(stubBeers);
    }

    public List<Beer> stubBeerList(int size) {
        return IntStream.rangeClosed(1, size)
                .mapToObj(this::stubBeer)
                .collect(Collectors.toList());
    }

    private Beer stubBeer(int index) {
        return Beer.builder()
                .beerName("BeerName" + index)
                .beerStyle("BeerStyle" + index)
                .price(BigDecimal.valueOf(111 * (index)))
                .upc(String.format(BEER_UPC_PATTERN, index))
                .minOnHand(111 * index)
                .quantityToBrew(123 * index)
                .build();
    }
}
