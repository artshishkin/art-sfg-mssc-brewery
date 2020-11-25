package net.shyshkin.study.breweryclient.web.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.shyshkin.study.breweryclient.web.model.BeerDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.test.web.client.MockRestServiceServer;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

@RestClientTest(BreweryClient.class)
class BreweryClientTest {

    @Autowired
    MockRestServiceServer server;

    @Autowired
    BreweryClient breweryClient;

    @Autowired
    ObjectMapper objectMapper;

    @Value("${net.shyshkin.apihost}")
    String apihost;

    @Test
    void getBeerById() throws JsonProcessingException {
        //given
        UUID beerId = UUID.randomUUID();
        BeerDto beerDto = BeerDto.builder().build();
        String beerJson = objectMapper.writeValueAsString(beerDto);
        server
                .expect(requestTo(apihost + "/api/v1/beer/" + beerId))
                .andRespond(withSuccess().body(beerJson).contentType(APPLICATION_JSON));

        //when
        BeerDto retrievedBeer = breweryClient.getBeerById(beerId);

        //then
        assertNotNull(retrievedBeer);
        server.verify();
    }
}