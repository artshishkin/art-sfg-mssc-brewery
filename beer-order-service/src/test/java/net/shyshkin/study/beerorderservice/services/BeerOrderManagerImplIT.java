package net.shyshkin.study.beerorderservice.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.jenspiegsa.wiremockextension.WireMockExtension;
import net.shyshkin.study.beerdata.dto.BeerDto;
import net.shyshkin.study.beerdata.dto.BeerStyleEnum;
import net.shyshkin.study.beerorderservice.domain.BeerOrder;
import net.shyshkin.study.beerorderservice.domain.BeerOrderLine;
import net.shyshkin.study.beerorderservice.domain.BeerOrderStatusEnum;
import net.shyshkin.study.beerorderservice.domain.Customer;
import net.shyshkin.study.beerorderservice.repositories.BeerOrderRepository;
import net.shyshkin.study.beerorderservice.repositories.CustomerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static net.shyshkin.study.beerorderservice.services.beerservice.BeerServiceRestTemplateImpl.BEER_UPC_PATH;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;

@SpringBootTest
@ExtendWith(WireMockExtension.class)
@ActiveProfiles("test")
class BeerOrderManagerImplIT {

    @Autowired
    BeerOrderManager beerOrderManager;

    @Autowired
    CustomerRepository customerRepository;

    @Autowired
    BeerOrderRepository beerOrderRepository;

    @Autowired
    ObjectMapper objectMapper;

    UUID beerId = UUID.randomUUID();
    String beerUpc = "987654";
    Customer testCustomer;

    @BeforeEach
    void setUp() {
        testCustomer = customerRepository.save(Customer.builder()
                .customerName("Test Customer")
                .build());
    }

    @Test
    void testNewToAllocated() throws JsonProcessingException {
        //given
        BeerOrder beerOrder = createBeerOrder();
        BeerDto beerDto = BeerDto.builder()
                .upc(beerUpc)
                .id(beerId)
                .beerStyle(BeerStyleEnum.PILSNER)
                .build();

        String json = objectMapper.writeValueAsString(beerDto);

        givenThat(
                get(BEER_UPC_PATH.replace("{upc}", beerUpc))
                        .willReturn(okJson(json)));

        //when
        BeerOrder newBeerOrder = beerOrderManager.newBeerOrder(beerOrder);

        //then
        await().untilAsserted(() -> {
            BeerOrder foundOrder = beerOrderRepository.findById(newBeerOrder.getId()).get();
            assertThat(foundOrder.getOrderStatus()).isEqualTo(BeerOrderStatusEnum.ALLOCATED);
        });

        BeerOrder retrievedBeerOrder = beerOrderRepository.findById(newBeerOrder.getId()).get();

        assertThat(retrievedBeerOrder)
                .isNotNull()
                .hasFieldOrPropertyWithValue("orderStatus", BeerOrderStatusEnum.ALLOCATED);

    }

    @Test
    void testNewToPickedUp() throws JsonProcessingException {
        //given
        BeerOrder beerOrder = createBeerOrder();
        BeerDto beerDto = BeerDto.builder()
                .upc(beerUpc)
                .id(beerId)
                .beerStyle(BeerStyleEnum.PILSNER)
                .build();

        String json = objectMapper.writeValueAsString(beerDto);

        givenThat(
                get(BEER_UPC_PATH.replace("{upc}", beerUpc))
                        .willReturn(okJson(json)));

        //when
        BeerOrder newBeerOrder = beerOrderManager.newBeerOrder(beerOrder);

        //then
        UUID orderId = newBeerOrder.getId();

        await().untilAsserted(() -> {
            BeerOrder foundOrder = beerOrderRepository.findById(orderId).get();
            assertThat(foundOrder.getOrderStatus()).isEqualTo(BeerOrderStatusEnum.ALLOCATED);
        });

        beerOrderManager.beerOrderPickedUp(orderId);

        await().untilAsserted(() -> {
            BeerOrder foundOrder = beerOrderRepository.findById(orderId).get();
            assertThat(foundOrder.getOrderStatus()).isEqualTo(BeerOrderStatusEnum.PICKED_UP);
        });

        BeerOrder retrievedBeerOrder = beerOrderRepository.findById(orderId).get();

        assertThat(retrievedBeerOrder)
                .isNotNull()
                .hasFieldOrPropertyWithValue("orderStatus", BeerOrderStatusEnum.PICKED_UP);
    }

    private BeerOrder createBeerOrder() {

        BeerOrder beerOrder = BeerOrder.builder()
                .customer(testCustomer)
                .build();

        Set<BeerOrderLine> lines = new HashSet<>();
        lines.add(BeerOrderLine.builder()
                .beerId(beerId)
                .upc(beerUpc)
                .orderQuantity(1)
                .beerOrder(beerOrder)
                .build());

        beerOrder.setBeerOrderLines(lines);

        return beerOrder;
    }
}