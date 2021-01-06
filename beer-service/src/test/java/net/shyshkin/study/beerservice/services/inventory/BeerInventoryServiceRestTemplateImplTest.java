package net.shyshkin.study.beerservice.services.inventory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.shyshkin.study.beerdata.dto.BeerInventoryDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.test.web.client.MockRestServiceServer;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

import static net.shyshkin.study.beerservice.services.inventory.BeerInventoryServiceRestTemplateImpl.INVENTORY_PATH;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

@RestClientTest(BeerInventoryServiceRestTemplateImpl.class)
class BeerInventoryServiceRestTemplateImplTest {

    @Autowired
    MockRestServiceServer server;

    @Autowired
    BeerInventoryService beerInventoryService;

    @Autowired
    ObjectMapper objectMapper;

    @Value("${net.shyshkin.client.beer-inventory-service-host}")
    String apihost;

    @Test
    void getOnHandInventory() throws JsonProcessingException {
        //given
        UUID beerId = UUID.randomUUID();
        Integer desiredQuantityOnHand = 5;
        BeerInventoryDto beerInventoryDto = BeerInventoryDto
                .builder()
                .beerId(beerId)
                .id(UUID.randomUUID())
                .createdDate(OffsetDateTime.now())
                .lastModifiedDate(OffsetDateTime.now())
                .quantityOnHand(desiredQuantityOnHand)
                .build();
        String jsonInventory = objectMapper.writeValueAsString(List.of(beerInventoryDto));

        server
                .expect(requestTo((apihost + INVENTORY_PATH).replace("{beerId}", beerId.toString())))
                .andRespond(withSuccess().body(jsonInventory).contentType(APPLICATION_JSON));

        //when
        Integer countOnHand = beerInventoryService.getOnHandInventory(beerId);

        //then
        assertNotNull(countOnHand);
        assertEquals(5, countOnHand);
        server.verify();
    }
}