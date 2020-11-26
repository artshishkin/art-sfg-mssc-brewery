package net.shyshkin.study.breweryclient.web.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.shyshkin.study.breweryclient.web.model.BeerDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.test.web.client.MockRestServiceServer;

import java.net.URI;
import java.util.UUID;

import static net.shyshkin.study.breweryclient.web.client.BreweryClient.BEER_PATH_V1;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.http.HttpMethod.*;
import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.*;
import static org.springframework.test.web.client.response.MockRestResponseCreators.*;

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
                .expect(requestTo(apihost + BEER_PATH_V1 + beerId))
                .andRespond(withSuccess().body(beerJson).contentType(APPLICATION_JSON));

        //when
        BeerDto retrievedBeer = breweryClient.getBeerById(beerId);

        //then
        assertNotNull(retrievedBeer);
        server.verify();
    }

    @Test
    void saveNewBeer() throws JsonProcessingException {
        //given
        UUID beerId = UUID.randomUUID();
        BeerDto beerDto = BeerDto.builder().beerName("Name").beerStyle("Beer Style").upc(123L).build();
        String beerJson = objectMapper.writeValueAsString(beerDto);
        server
                .expect(requestTo(apihost + BEER_PATH_V1))
                .andExpect(method(POST))
                .andExpect(content().json(beerJson))
                .andRespond(withCreatedEntity(URI.create(apihost + BEER_PATH_V1 + beerId)));

        //when
        URI location = breweryClient.saveNewBeer(beerDto);

        //then
        assertNotNull(location);
        assertThat(location.toString()).contains(BEER_PATH_V1);
        server.verify();
    }

    @Test
    void updateBeer() throws JsonProcessingException {
        //given
        UUID beerId = UUID.randomUUID();
        BeerDto beerDto = BeerDto.builder().beerName("Name").beerStyle("Beer Style").upc(123L).build();
        String beerJson = objectMapper.writeValueAsString(beerDto);
        server
                .expect(requestTo(apihost + BEER_PATH_V1 + beerId))
                .andExpect(method(PUT))
                .andExpect(content().json(beerJson))
                .andRespond(withStatus(NO_CONTENT));

        //when
        breweryClient.updateBeer(beerId, beerDto);

        //then
        server.verify();
    }

    @Test
    void deleteBeer() {
        //given
        UUID beerId = UUID.randomUUID();
        server
                .expect(requestTo(apihost + BEER_PATH_V1 + beerId))
                .andExpect(method(DELETE))
                .andRespond(withStatus(NO_CONTENT));

        //when
        breweryClient.deleteBeer(beerId);

        //then
        server.verify();
    }
}