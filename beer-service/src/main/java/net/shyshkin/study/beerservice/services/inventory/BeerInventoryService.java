package net.shyshkin.study.beerservice.services.inventory;

import java.util.UUID;

public interface BeerInventoryService {

    String INVENTORY_PATH = "/api/v1/beer/{beerId}/inventory";

    Integer getOnHandInventory(UUID beerId);
}
