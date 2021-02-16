package net.shyshkin.study.beertastingroomservice.services.beerservice;

import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import net.shyshkin.study.beerdata.dto.BeerDto;
import net.shyshkin.study.beerdata.dto.BeerPagedList;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;
import java.util.UUID;

@Component
@Slf4j
@RequiredArgsConstructor
public class BeerServiceRestTemplateImpl implements BeerService {

    public static final String BEER_UPC_PATH = "/api/v1/beerUpc/{upc}";
    public static final String BEER_PATH_ALL = "/api/v1/beer";
    public static final String BEER_PATH = BEER_PATH_ALL + "/{beerId}";

    @Setter
    @Value("${net.shyshkin.client.beer-service-host}")
    private String beerServiceHost;

    private final RestTemplate restTemplate;

    @Override
    public Optional<BeerDto> getBeerById(UUID beerId) {

        log.debug("Calling Beer service getBeerById({})", beerId);

        BeerDto beerDto = restTemplate.getForObject(beerServiceHost + BEER_PATH, BeerDto.class, beerId);

        return Optional.ofNullable(beerDto);
    }

    @Override
    public Optional<BeerDto> getBeerByUpc(String upc) {

        log.debug("Calling Beer service getBeerByUpc({})", upc);

        BeerDto beerDto = restTemplate.getForObject(beerServiceHost + BEER_UPC_PATH, BeerDto.class, upc);

        return Optional.ofNullable(beerDto);
    }

    @Override
    public Optional<BeerPagedList> getListOfBeers() {
        return Optional.ofNullable(restTemplate.getForObject(beerServiceHost + BEER_PATH_ALL, BeerPagedList.class));
    }
}
