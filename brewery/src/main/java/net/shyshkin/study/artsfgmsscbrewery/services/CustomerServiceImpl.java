package net.shyshkin.study.artsfgmsscbrewery.services;

import lombok.extern.slf4j.Slf4j;
import net.shyshkin.study.artsfgmsscbrewery.web.model.CustomerDto;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Slf4j
@Service
public class CustomerServiceImpl implements CustomerService {
    @Override
    public CustomerDto getCustomerById(UUID id) {
        return CustomerDto.builder()
                .id(id)
                .name("Customer Name")
                .build();
    }

    @Override
    public CustomerDto saveNewCustomer(CustomerDto customerDto) {
        customerDto.setId(UUID.randomUUID());
        return customerDto;
    }

    @Override
    public void updateCustomer(UUID customerId, CustomerDto customerDto) {
        // TODO: 24.11.2020 Implement real update customer
        log.debug("updating customer with Id {} body is {}", customerId, customerDto);
    }

    @Override
    public void deleteById(UUID customerId) {
        // TODO: 24.11.2020 Implement deletion
        log.debug("deleting customer with Id {}", customerId);
    }
}
