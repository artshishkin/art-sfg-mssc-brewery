package net.shyshkin.study.beerorderservice.services.beerservice;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import net.shyshkin.study.beerdata.dto.BeerDto;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;
import java.util.UUID;

@Component
@Slf4j
@ConfigurationProperties(prefix = "net.shyshkin.client", ignoreUnknownFields = false)
public class BeerServiceRestTemplateImpl implements BeerService {

    static final String BEER_UPC_PATH = "/api/v1/beerUpc/{upc}";
    static final String BEER_PATH = "/api/v1/beer/{beerId}";

    @Setter
    private String beerServiceHost;

    private final RestTemplate restTemplate;

    public BeerServiceRestTemplateImpl(RestTemplateBuilder restTemplateBuilder) {
        this.restTemplate = restTemplateBuilder.build();
    }

    @Override
    public Optional<BeerDto> getBeerById(UUID beerId) {

        log.debug("Calling Beer service (getBeerById)");

        BeerDto beerDto = restTemplate.getForObject(beerServiceHost + BEER_PATH, BeerDto.class, beerId);

        return beerDto == null ? Optional.empty() : Optional.of(beerDto);
    }

    @Override
    public Optional<BeerDto> getBeerByUpc(String upc) {

        log.debug("Calling Beer service (getBeerByUpc)");

        BeerDto beerDto = restTemplate.getForObject(beerServiceHost + BEER_UPC_PATH, BeerDto.class, upc);

        return beerDto == null ? Optional.empty() : Optional.of(beerDto);
    }
}
