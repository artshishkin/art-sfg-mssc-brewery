package net.shyshkin.study.breweryclient.web.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.shyshkin.study.breweryclient.web.model.BeerDto;
import net.shyshkin.study.breweryclient.web.model.CustomerDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.test.web.client.MockRestServiceServer;

import java.net.URI;
import java.util.UUID;

import static net.shyshkin.study.breweryclient.web.client.BreweryClient.BEER_PATH_V1;
import static net.shyshkin.study.breweryclient.web.client.BreweryClient.CUSTOMER_PATH_V1;
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

    @Nested
    @DisplayName("Beer Endpoints Client")
    class BeerEndpoints {
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

    @Nested
    @DisplayName("Customer Endpoints Client")
    class CustomerEndpoints {
        @Test
        void getCustomerById() throws JsonProcessingException {
            //given
            UUID customerId = UUID.randomUUID();
            CustomerDto customerDto = CustomerDto.builder().build();
            String customerJson = objectMapper.writeValueAsString(customerDto);
            server
                    .expect(requestTo(apihost + CUSTOMER_PATH_V1 + customerId))
                    .andExpect(method(GET))
                    .andRespond(withSuccess().body(customerJson).contentType(APPLICATION_JSON));

            //when
            CustomerDto retrievedCustomer = breweryClient.getCustomerById(customerId);

            //then
            assertNotNull(retrievedCustomer);
            server.verify();
        }

        @Test
        void saveNewCustomer() throws JsonProcessingException {
            //given
            UUID customerId = UUID.randomUUID();
            CustomerDto customerDto = CustomerDto.builder().name("Name").build();
            String customerJson = objectMapper.writeValueAsString(customerDto);
            server
                    .expect(requestTo(apihost + CUSTOMER_PATH_V1))
                    .andExpect(method(POST))
                    .andExpect(content().json(customerJson))
                    .andRespond(withCreatedEntity(URI.create(apihost + CUSTOMER_PATH_V1 + customerId)));

            //when
            URI location = breweryClient.saveNewCustomer(customerDto);

            //then
            assertNotNull(location);
            assertThat(location.toString()).contains(CUSTOMER_PATH_V1);
            server.verify();
        }

        @Test
        void updateCustomer() throws JsonProcessingException {
            //given
            UUID customerId = UUID.randomUUID();
            CustomerDto customerDto = CustomerDto.builder().name("Name").build();
            String customerJson = objectMapper.writeValueAsString(customerDto);
            server
                    .expect(requestTo(apihost + CUSTOMER_PATH_V1 + customerId))
                    .andExpect(method(PUT))
                    .andExpect(content().json(customerJson))
                    .andRespond(withStatus(NO_CONTENT));

            //when
            breweryClient.updateCustomer(customerId, customerDto);

            //then
            server.verify();
        }

        @Test
        void deleteCustomer() {
            //given
            UUID customerId = UUID.randomUUID();
            server
                    .expect(requestTo(apihost + CUSTOMER_PATH_V1 + customerId))
                    .andExpect(method(DELETE))
                    .andRespond(withStatus(NO_CONTENT));

            //when
            breweryClient.deleteCustomer(customerId);

            //then
            server.verify();
        }
    }
}