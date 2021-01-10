package net.shyshkin.study.beerorderservice.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.jenspiegsa.wiremockextension.WireMockExtension;
import net.shyshkin.study.beerdata.dto.BeerDto;
import net.shyshkin.study.beerdata.dto.BeerStyleEnum;
import net.shyshkin.study.beerdata.events.AllocationFailureEvent;
import net.shyshkin.study.beerdata.events.DeallocateOrderRequest;
import net.shyshkin.study.beerdata.queue.Queues;
import net.shyshkin.study.beerorderservice.domain.BeerOrder;
import net.shyshkin.study.beerorderservice.domain.BeerOrderLine;
import net.shyshkin.study.beerorderservice.domain.BeerOrderStatusEnum;
import net.shyshkin.study.beerorderservice.domain.Customer;
import net.shyshkin.study.beerorderservice.repositories.BeerOrderRepository;
import net.shyshkin.study.beerorderservice.repositories.CustomerRepository;
import net.shyshkin.study.beerorderservice.testcomponents.BeerOrderValidationListener;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.test.context.ActiveProfiles;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static java.util.concurrent.TimeUnit.SECONDS;
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

    @Autowired
    JmsTemplate jmsTemplate;

    @SpyBean
    BeerOrderValidationListener beerOrderValidationListener;

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

    @ParameterizedTest(name = "[{index}]{0}")
    @CsvSource({
            "VALIDATION_PENDING -> CANCELLED,pause-validation,VALIDATION_PENDING",
            "ALLOCATION_PENDING -> CANCELLED,pause-allocation,ALLOCATION_PENDING",
            "ALLOCATED -> CANCELLED,,ALLOCATED"
    })
    void testAnyStateToCancelled(final String testName, final String fakeListenerKey, final BeerOrderStatusEnum fromState) throws JsonProcessingException {
        //given
        BeerOrder beerOrder = createBeerOrder();
        BeerDto beerDto = BeerDto.builder()
                .upc(beerUpc)
                .id(beerId)
                .beerStyle(BeerStyleEnum.PILSNER)
                .build();

        String json = objectMapper.writeValueAsString(beerDto);

        beerOrder.setCustomerRef(fakeListenerKey);

        givenThat(
                get(BEER_UPC_PATH.replace("{upc}", beerUpc))
                        .willReturn(okJson(json)));

        BeerOrder newBeerOrder = beerOrderManager.newBeerOrder(beerOrder);
        UUID orderId = newBeerOrder.getId();
        await()
                .timeout(2L, SECONDS)
                .untilAsserted(() -> {
                    BeerOrder foundOrder = beerOrderRepository.findById(orderId).get();
                    assertThat(foundOrder.getOrderStatus()).isEqualTo(fromState);
                });

        //when
        beerOrderManager.cancelOrder(orderId);

        //then
        await()
                .timeout(2L, SECONDS)
                .untilAsserted(() -> {
                    BeerOrder foundOrder = beerOrderRepository.findById(orderId).get();
                    assertThat(foundOrder.getOrderStatus()).isEqualTo(BeerOrderStatusEnum.CANCELED);
                });

        if ("ALLOCATED -> CANCELLED".equals(testName)) {
            DeallocateOrderRequest request = (DeallocateOrderRequest) jmsTemplate.receiveAndConvert(Queues.DEALLOCATE_ORDER_QUEUE);
            assertThat(request.getBeerOrder().getId()).isEqualTo(orderId);
        }

    }

    @ParameterizedTest(name = "[{index}] {arguments}")
    @CsvSource({
            "fail-validation, VALIDATION_EXCEPTION",
            "fail-allocation, ALLOCATION_EXCEPTION",
            "partial-allocation, PENDING_INVENTORY"
    })
    void failTests(final String failName, final BeerOrderStatusEnum expectedFinalState) throws JsonProcessingException {
        //given
        BeerOrder beerOrder = createBeerOrder();
        BeerDto beerDto = BeerDto.builder()
                .upc(beerUpc)
                .id(beerId)
                .beerStyle(BeerStyleEnum.PILSNER)
                .build();

        //fake ref to mark process failing
        beerOrder.setCustomerRef(failName);

        String json = objectMapper.writeValueAsString(beerDto);

        givenThat(
                get(BEER_UPC_PATH.replace("{upc}", beerUpc))
                        .willReturn(okJson(json)));

        //when
        BeerOrder newBeerOrder = beerOrderManager.newBeerOrder(beerOrder);

        //then
        UUID beerOrderId = newBeerOrder.getId();
        await()
                .timeout(2L, SECONDS)
                .untilAsserted(() -> {
                    BeerOrder foundOrder = beerOrderRepository.findById(newBeerOrder.getId()).get();
                    assertThat(foundOrder.getOrderStatus()).isEqualTo(expectedFinalState);
                });

        if ("fail-allocation".equals(failName)) {
            AllocationFailureEvent message = (AllocationFailureEvent) jmsTemplate.receiveAndConvert(Queues.ALLOCATION_FAILURE_QUEUE);
            assertThat(message).hasFieldOrPropertyWithValue("orderId", beerOrderId);
        }
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

    @Test
    @Disabled("Can not make it working")
    void testValidationPendingToCancelled_usingSpyBean() throws JsonProcessingException, InterruptedException {
        //given
        BeerOrder beerOrder = createBeerOrder();
        BeerDto beerDto = BeerDto.builder()
                .upc(beerUpc)
                .id(beerId)
                .beerStyle(BeerStyleEnum.PILSNER)
                .build();

//        BDDMockito.given(beerOrderValidationListener.validateOrder(ArgumentMatchers.any(ValidateOrderRequest.class)))
//        BDDMockito.given(beerOrderValidationListener.validateOrder(ArgumentMatchers.any()))
//                .willAnswer(
//                        (Answer<ValidateOrderResult>) invocationOnMock -> {
//                            Thread.sleep(100);
//                            ValidateOrderRequest validateOrderRequest = invocationOnMock.getArgument(0, ValidateOrderRequest.class);
//                            return ValidateOrderResult.builder()
//                                    .valid(true)
//                                    .orderId(validateOrderRequest.getBeerOrder().getId())
//                                    .build();
////                            return (ValidateOrderResult) invocationOnMock.callRealMethod();
//                        });
        BDDMockito.given(beerOrderValidationListener.validateOrder(ArgumentMatchers.any())).willCallRealMethod();
        String json = objectMapper.writeValueAsString(beerDto);

        givenThat(
                get(BEER_UPC_PATH.replace("{upc}", beerUpc))
                        .willReturn(okJson(json)));

        //when
        BeerOrder newBeerOrder = beerOrderManager.newBeerOrder(beerOrder);
        UUID orderId = newBeerOrder.getId();
        beerOrderManager.cancelOrder(orderId);

        //then
        await()
                .timeout(2L, SECONDS)
                .untilAsserted(() -> {
                    BeerOrder foundOrder = beerOrderRepository.findById(orderId).get();
                    assertThat(foundOrder.getOrderStatus()).isEqualTo(BeerOrderStatusEnum.CANCELED);
                });
    }

    private BeerOrder createBeerOrder() {

        BeerOrder beerOrder = BeerOrder.builder()
                .customer(testCustomer)
                .build();

        Set<BeerOrderLine> lines = new HashSet<>();
        lines.add(BeerOrderLine.builder()
                .beerId(beerId)
                .upc(beerUpc)
                .orderQuantity(3)
                .beerOrder(beerOrder)
                .build());

        beerOrder.setBeerOrderLines(lines);

        return beerOrder;
    }
}