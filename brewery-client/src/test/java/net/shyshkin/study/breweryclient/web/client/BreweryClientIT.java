package net.shyshkin.study.breweryclient.web.client;

import net.shyshkin.study.breweryclient.web.model.BeerDto;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class BreweryClientIT {

    @Autowired
    BreweryClient breweryClient;

    @Disabled("NOT TO RUN IN CI yet")
    @Test
    void getBeerById() {
        //given
        UUID beerId = UUID.randomUUID();

        //when
        BeerDto retrievedBeer = breweryClient.getBeerById(beerId);

        //then
        assertNotNull(retrievedBeer);
    }
}