package net.shyshkin.study.beerservice.services.inventory;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.shyshkin.study.beerdata.dto.BeerInventoryDto;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.annotation.Profile;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
@Slf4j
@Profile("local-discovery")
@RequiredArgsConstructor
public class BeerInventoryServiceFiegnClientImpl implements BeerInventoryService {

    private final InventoryServiceFeignClient feignClient;

    @Override
    @Cacheable(cacheNames = "beerInventoryCache")
    public Integer getOnHandInventory(UUID beerId) {
        log.debug("Calling Inventory service");

        ResponseEntity<List<BeerInventoryDto>> responseEntity = feignClient.getOnHandInventory(beerId);

        int onHand = Objects.requireNonNull(responseEntity.getBody())
                .stream()
                .mapToInt(BeerInventoryDto::getQuantityOnHand)
                .sum();
        log.debug("BeerId: `{}`. On hand is: {}", beerId, onHand);
        return onHand;
    }
}
