package net.shyshkin.study.beerservice.services.inventory;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@Disabled("Start Inventory Service before, then enable test and run it")
@SpringBootTest
class BeerInventoryServiceRestTemplateImplIT {

    @Autowired
    BeerInventoryService beerInventoryService;

    @Test
    void getOnHandInventory() {
        //given
        UUID beerId = UUID.fromString("0a818933-087d-47f2-ad83-2f986ed087eb");

        //when
        Integer countOnHand = beerInventoryService.getOnHandInventory(beerId);

        //then
        assertNotNull(countOnHand);
        assertEquals(50, countOnHand);
    }
}