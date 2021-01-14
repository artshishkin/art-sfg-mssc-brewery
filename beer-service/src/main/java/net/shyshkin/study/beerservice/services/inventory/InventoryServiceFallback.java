package net.shyshkin.study.beerservice.services.inventory;

import lombok.RequiredArgsConstructor;
import net.shyshkin.study.beerdata.dto.BeerInventoryDto;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class InventoryServiceFallback implements InventoryServiceFeignClient {

    private final InventoryFailoverFeignClient fallbackFeignClient;

    @Override
    public ResponseEntity<List<BeerInventoryDto>> getOnHandInventory(UUID beerId) {
        return fallbackFeignClient.getOnHandInventory();
    }
}
