package net.shyshkin.study.breweryclient.web.client;

import net.shyshkin.study.breweryclient.web.model.BeerDto;
import net.shyshkin.study.breweryclient.web.model.CustomerDto;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.net.URI;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@Disabled("NOT TO RUN IN CI yet")
class BreweryClientIT {

    @Autowired
    BreweryClient breweryClient;

    @Nested
    @DisplayName("Beer Endpoints Client")
    class BeerEndpoints {

        @Test
        void getBeerById() {
            //given
            UUID beerId = UUID.randomUUID();

            //when
            BeerDto retrievedBeer = breweryClient.getBeerById(beerId);

            //then
            assertNotNull(retrievedBeer);
        }

        @Test
        void saveNewBeer() {
            //given
            BeerDto beerDto = BeerDto.builder().beerName("Name").beerStyle("Beer Style").upc(123L).build();

            //when
            URI location = breweryClient.saveNewBeer(beerDto);

            //then
            assertNotNull(location);
            assertThat(location.toString()).contains(BreweryClient.BEER_PATH_V1);
        }

        @Test
        void updateBeer() {
            //given
            UUID beerId = UUID.randomUUID();
            BeerDto beerDto = BeerDto.builder().beerName("Name").beerStyle("Beer Style").upc(123L).build();

            //when
            breweryClient.updateBeer(beerId, beerDto);

            //then
            //should not be exception
        }

        @Test
        void deleteBeer() {
            //given
            UUID beerId = UUID.randomUUID();

            //when
            breweryClient.deleteBeer(beerId);

            //then
            //should not be exception
        }
    }
    
    @Nested
    @DisplayName("Customer Endpoints Client")
    class CustomerEndpoints {

        @Test
        void getCustomerById() {
            //given
            UUID customerId = UUID.randomUUID();

            //when
            CustomerDto retrievedCustomer = breweryClient.getCustomerById(customerId);

            //then
            assertNotNull(retrievedCustomer);
        }

        @Test
        void saveNewCustomer() {
            //given
            CustomerDto customerDto = CustomerDto.builder().name("Name").build();

            //when
            URI location = breweryClient.saveNewCustomer(customerDto);

            //then
            assertNotNull(location);
            assertThat(location.toString()).contains(BreweryClient.CUSTOMER_PATH_V1);
        }

        @Test
        void updateCustomer() {
            //given
            UUID customerId = UUID.randomUUID();
            CustomerDto customerDto = CustomerDto.builder().name("Name").build();

            //when
            breweryClient.updateCustomer(customerId, customerDto);

            //then
            //should not be exception
        }

        @Test
        void deleteCustomer() {
            //given
            UUID customerId = UUID.randomUUID();

            //when
            breweryClient.deleteCustomer(customerId);

            //then
            //should not be exception
        }
    }
}