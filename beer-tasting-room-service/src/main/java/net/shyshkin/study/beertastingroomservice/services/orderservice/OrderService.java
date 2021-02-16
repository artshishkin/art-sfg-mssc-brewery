package net.shyshkin.study.beertastingroomservice.services.orderservice;


import net.shyshkin.study.beerdata.dto.BeerOrderDto;
import net.shyshkin.study.beerdata.dto.CustomerDto;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface OrderService {

    String CUSTOMERS_PATH = "/api/v1/customers";
    String NEW_ORDER_PATH = CUSTOMERS_PATH + "/{customerId}/orders";
    String GET_ORDER_PATH = CUSTOMERS_PATH + "/{customerId}/orders/{orderId}";

    List<CustomerDto> getAllCustomers();

    Optional<BeerOrderDto> placeOrder(UUID customerId, BeerOrderDto beerOrderDto);

    Optional<BeerOrderDto> getOrder(UUID customerId, UUID orderId);
}
