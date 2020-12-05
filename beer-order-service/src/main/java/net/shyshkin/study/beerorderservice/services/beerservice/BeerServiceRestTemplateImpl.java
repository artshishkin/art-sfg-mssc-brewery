package net.shyshkin.study.beerorderservice.services.beerservice;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import net.shyshkin.study.beerorderservice.services.beerservice.model.BeerDto;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Objects;

@Component
@Slf4j
@ConfigurationProperties(prefix = "net.shyshkin.client", ignoreUnknownFields = false)
public class BeerServiceRestTemplateImpl implements BeerService {

    static final String BEER_UPC_PATH = "/api/v1/beerUpc/{upc}";

    @Setter
    private String beerServiceHost;

    private final RestTemplate restTemplate;

    public BeerServiceRestTemplateImpl(RestTemplateBuilder restTemplateBuilder) {
        this.restTemplate = restTemplateBuilder.build();
    }

    @Override
    public BeerDto getBeerByUpc(String upc) {

        log.debug("Calling Beer service");

        BeerDto beerDto = restTemplate.getForObject(beerServiceHost + BEER_UPC_PATH, BeerDto.class, upc);

        return Objects.requireNonNull(beerDto);
    }
}
