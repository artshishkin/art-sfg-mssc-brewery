package net.shyshkin.study.beertastingroomservice.services.orderservice;

import lombok.RequiredArgsConstructor;
import lombok.Setter;
import net.shyshkin.study.beerdata.dto.BeerOrderDto;
import net.shyshkin.study.beerdata.dto.CustomerDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Service
@RequiredArgsConstructor
public class OrderServiceRestTemplateImpl implements OrderService {

    private final RestTemplate restTemplate;

    @Setter
    @Value("${net.shyshkin.client.order-service-host}")
    private String orderServiceHost;

    @Override
    public List<CustomerDto> getAllCustomers() {
        CustomerDto[] customerDtoArray = restTemplate.getForObject(orderServiceHost + OrderService.CUSTOMERS_PATH,
                CustomerDto[].class);
        return Arrays.asList(Objects.requireNonNull(customerDtoArray));
    }

    @Override
    public Optional<BeerOrderDto> placeOrder(UUID customerId, BeerOrderDto beerOrderDto) {

        BeerOrderDto responseBeerOrderDto = restTemplate.postForObject(
                orderServiceHost + OrderService.NEW_ORDER_PATH,
                beerOrderDto,
                BeerOrderDto.class,
                customerId);

        return Optional.ofNullable(responseBeerOrderDto);
    }

    @Override
    public Optional<BeerOrderDto> getOrder(UUID customerId, UUID orderId) {

        BeerOrderDto responseBeerOrderDto = restTemplate.getForObject(
                orderServiceHost + OrderService.GET_ORDER_PATH,
                BeerOrderDto.class,
                customerId,
                orderId);

        return Optional.ofNullable(responseBeerOrderDto);
    }
}
