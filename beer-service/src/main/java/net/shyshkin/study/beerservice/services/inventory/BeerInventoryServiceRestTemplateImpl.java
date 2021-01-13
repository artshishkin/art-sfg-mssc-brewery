package net.shyshkin.study.beerservice.services.inventory;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import net.shyshkin.study.beerdata.dto.BeerInventoryDto;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.annotation.Profile;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Component
@Slf4j
@Profile("!local-discovery")
@ConfigurationProperties(prefix = "net.shyshkin.client", ignoreUnknownFields = false)
public class BeerInventoryServiceRestTemplateImpl implements BeerInventoryService {

    @Setter
    private String beerInventoryServiceHost;

    private final RestTemplate restTemplate;

    public BeerInventoryServiceRestTemplateImpl(RestTemplateBuilder restTemplateBuilder) {
        this.restTemplate = restTemplateBuilder.build();
    }

    @Override
    @Cacheable(cacheNames = "beerInventoryCache")
    public Integer getOnHandInventory(UUID beerId) {
        log.debug("Calling Inventory service");

        ResponseEntity<List<BeerInventoryDto>> responseEntity = restTemplate.exchange(beerInventoryServiceHost + INVENTORY_PATH,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<BeerInventoryDto>>() {
                },
                beerId
        );
        int onHand = Objects.requireNonNull(responseEntity.getBody())
                .stream()
                .mapToInt(BeerInventoryDto::getQuantityOnHand)
                .sum();
        log.debug("BeerId: `{}`. On hand is: {}", beerId, onHand);
        return onHand;
    }
}
