package net.shyshkin.study.beerservice;

import net.shyshkin.study.beerservice.services.inventory.BeerInventoryService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest
class BeerServiceApplicationTests {

    @MockBean
    BeerInventoryService inventoryService;

    @Test
    void contextLoads() {
    }
}