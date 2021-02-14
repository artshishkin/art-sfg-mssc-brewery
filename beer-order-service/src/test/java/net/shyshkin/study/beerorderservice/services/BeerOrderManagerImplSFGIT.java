package net.shyshkin.study.beerorderservice.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.jenspiegsa.wiremockextension.WireMockExtension;
import com.github.tomakehurst.wiremock.WireMockServer;
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
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static com.github.jenspiegsa.wiremockextension.ManagedWireMockServer.with;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.okJson;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;
import static net.shyshkin.study.beerorderservice.services.beerservice.BeerServiceRestTemplateImpl.BEER_PATH;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;

@SpringBootTest
@ExtendWith(WireMockExtension.class)
@TestPropertySource(properties = {
        "net.shyshkin.client.beer-service-host=http://localhost:8083"
})
@ActiveProfiles({"test","base_log"})
//@ComponentScan(excludeFilters = @ComponentScan.Filter(classes = {TastingRoomService.class},type = FilterType.ASSIGNABLE_TYPE))
class BeerOrderManagerImplSFGIT {

    @Autowired
    BeerOrderManager beerOrderManager;

    @Autowired
    CustomerRepository customerRepository;

    @Autowired
    BeerOrderRepository beerOrderRepository;

    @Autowired
    WireMockServer wireMockServer;

    @Autowired
    ObjectMapper objectMapper;

    UUID beerId = UUID.randomUUID();
    String beerUpc = "987654";

    Customer testCustomer;

    @MockBean
    TastingRoomService tastingRoomService;

    @TestConfiguration
    static class RestTemplateBuilderProvider {

        @Bean(destroyMethod = "stop")
        public WireMockServer wireMockServer() {
            WireMockServer server = with(wireMockConfig().port(8083));
            server.start();
            return server;
        }
    }

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

        wireMockServer.stubFor(get(BEER_PATH.replace("{beerId}",  beerId.toString()))
                .willReturn(okJson(json)));

        //when
        BeerOrder newBeerOrder = beerOrderManager.newBeerOrder(beerOrder);

        //then
        await()
                .timeout(2, TimeUnit.SECONDS)
                .untilAsserted(() -> {
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

        wireMockServer.stubFor(get(BEER_PATH.replace("{beerId}",  beerId.toString()))
                .willReturn(okJson(json)));

        //when
        BeerOrder newBeerOrder = beerOrderManager.newBeerOrder(beerOrder);

        //then
        await()
                .timeout(2, TimeUnit.SECONDS)
                .untilAsserted(() -> {
                    BeerOrder foundOrder = beerOrderRepository.findById(newBeerOrder.getId()).get();
                    assertThat(foundOrder.getOrderStatus()).isEqualTo(BeerOrderStatusEnum.ALLOCATED);
                });
        UUID beerOrderId = newBeerOrder.getId();

        beerOrderManager.beerOrderPickedUp(beerOrderId);

        await()
                .timeout(2, TimeUnit.SECONDS)
                .untilAsserted(() -> {
                    BeerOrder foundOrder = beerOrderRepository.findById(newBeerOrder.getId()).get();
                    assertThat(foundOrder.getOrderStatus()).isEqualTo(BeerOrderStatusEnum.PICKED_UP);
                });

        BeerOrder retrievedBeerOrder = beerOrderRepository.findById(newBeerOrder.getId()).get();

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