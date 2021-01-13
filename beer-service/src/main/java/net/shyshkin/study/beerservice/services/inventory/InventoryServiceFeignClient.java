package net.shyshkin.study.beerservice.services.inventory;

import net.shyshkin.study.beerdata.dto.BeerInventoryDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;
import java.util.UUID;

@FeignClient(name = "inventory-service")
public interface InventoryServiceFeignClient {

    @RequestMapping(method = RequestMethod.GET, value = BeerInventoryService.INVENTORY_PATH)
    ResponseEntity<List<BeerInventoryDto>> getOnHandInventory(@PathVariable UUID beerId);

}
