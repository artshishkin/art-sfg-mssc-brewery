package net.shyshkin.study.beerinventoryfailover.web;

import net.shyshkin.study.beerdata.dto.BeerInventoryDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@WebFluxTest
class FailoverEndpointTest {

    @Autowired
    WebTestClient webClient;

    @Test
    @DisplayName("Getting `/inventory-failover` endpoint should respond with stub BeerInventoryDto List")
    void inventoryFailoverTest() {
        //when
        webClient.get().uri("/inventory-failover")

                //then
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(BeerInventoryDto.class)
                .hasSize(1)
                .value(list -> assertThat(list)
                        .allSatisfy(
                                order -> assertThat(order)
                                        .hasNoNullFieldsOrProperties()
                                        .hasFieldOrPropertyWithValue("quantityOnHand", 999)
                                        .hasFieldOrPropertyWithValue("beerId", UUID.fromString("00000000-0000-0000-0000-000000000000"))
                        ));
    }
}