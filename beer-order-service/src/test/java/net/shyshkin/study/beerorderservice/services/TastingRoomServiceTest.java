package net.shyshkin.study.beerorderservice.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.shyshkin.study.beerdata.dto.BeerDto;
import net.shyshkin.study.beerdata.dto.BeerOrderDto;
import net.shyshkin.study.beerdata.dto.BeerPagedList;
import net.shyshkin.study.beerorderservice.domain.Customer;
import net.shyshkin.study.beerorderservice.repositories.CustomerRepository;
import net.shyshkin.study.beerorderservice.services.beerservice.BeerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@ExtendWith(MockitoExtension.class)
class TastingRoomServiceTest {

    @InjectMocks
    TastingRoomService tastingRoomService;

    @Mock
    CustomerRepository customerRepository;
    @Mock
    BeerOrderService beerOrderService;
    @Mock
    BeerService beerService;

    @Captor
    ArgumentCaptor<BeerOrderDto> beerOrderDtoCaptor;

    @BeforeEach
    void setUp() {
        tastingRoomService.setMaxQuantity(6);
    }

    @Test
    void placeTastingRoomOrder() {
        //given
        UUID customerId = UUID.randomUUID();
        UUID beerId = UUID.randomUUID();

        Customer customer = Customer.builder().id(customerId).build();
        given(customerRepository.findByCustomerName(anyString())).willReturn(Optional.of(customer));
        BeerPagedList beerPagedList = new BeerPagedList(List.of(BeerDto.builder().id(beerId).build()));
        given(beerService.getListOfBeers()).willReturn(Optional.of(beerPagedList));

        //when
        tastingRoomService.placeTastingRoomOrder();

        //then
        then(beerOrderService).should().placeOrder(eq(customerId), beerOrderDtoCaptor.capture());
        BeerOrderDto placedOrder = beerOrderDtoCaptor.getValue();
        assertThat(placedOrder.getBeerOrderLines())
                .hasSize(1)
                .allSatisfy(beerOrderLineDto -> assertThat(beerOrderLineDto.getBeerId()).isEqualTo(beerId));
    }

    @Test
    void placeTastingRoomOrder_emptyList() {
        //given
        UUID customerId = UUID.randomUUID();

        Customer customer = Customer.builder().id(customerId).build();
        given(customerRepository.findByCustomerName(anyString())).willReturn(Optional.of(customer));
        BeerPagedList beerPagedList = new BeerPagedList(new ArrayList<>());
        given(beerService.getListOfBeers()).willReturn(Optional.of(beerPagedList));

        //when
        tastingRoomService.placeTastingRoomOrder();

        //then
        then(beerOrderService).shouldHaveNoInteractions();
    }

    @Test
    @Disabled
    void placeTastingRoomOrder_noBeers_JSON() throws JsonProcessingException {
        //given
        UUID customerId = UUID.randomUUID();
        UUID beerId = UUID.randomUUID();

        Customer customer = Customer.builder().id(customerId).build();
        given(customerRepository.findByCustomerName(anyString())).willReturn(Optional.of(customer));

        ObjectMapper objectMapper = new ObjectMapper();

        String emptyBeerListJson = "{\"content\":[],\"number\":0,\"size\":25,\"totalElements\":0,\"pageable\":{\"sort\":{\"sorted\":false,\"unsorted\":true,\"empty\":true},\"offset\":0,\"pageNumber\":0,\"pageSize\":25,\"paged\":true,\"unpaged\":false},\"totalPages\":0,\"first\":true,\"last\":true,\"sort\":{\"sorted\":false,\"unsorted\":true,\"empty\":true},\"numberOfElements\":0,\"empty\":true}";
        BeerPagedList beerPagedList = objectMapper.readValue(emptyBeerListJson, BeerPagedList.class);

        given(beerService.getListOfBeers()).willReturn(Optional.of(beerPagedList));

        //when
        tastingRoomService.placeTastingRoomOrder();

        //then
        then(beerOrderService).shouldHaveNoInteractions();
    }
}