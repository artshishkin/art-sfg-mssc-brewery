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

    private final String[][] BEER_VALUES = {
            {"Mango Bobs", "IPA", "0631234200036"},
            {"Galaxy Cat", "PALE_ALE", "0631234300019"},
            {"Pinball Porter", "PORTER", "0083783375213"}};

    @Override
    public void run(String... args) throws Exception {
        if (beerRepository.count() == 0)
            bootstrapBeers();
    }

    private void bootstrapBeers() {
        List<Beer> stubBeers = stubBeerList(3);
        beerRepository.saveAll(stubBeers);
    }

    public List<Beer> stubBeerList(int size) {
        return IntStream.range(0, size)
                .mapToObj(this::stubBeer)
                .collect(Collectors.toList());
    }

    private Beer stubBeer(int index) {
        String beerName = BEER_VALUES[index][0];
        String beerStyle = BEER_VALUES[index][1];
        String beerUPC = BEER_VALUES[index][2];

        return Beer.builder()
                .beerName(beerName)
                .beerStyle(beerStyle)
                .price(new BigDecimal("12.95"))
                .upc(beerUPC)
                .minOnHand(12)
                .quantityToBrew(200)
                .build();
    }
}
