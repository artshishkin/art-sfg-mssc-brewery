package net.shyshkin.study.artsfgmsscbrewery.services;

import net.shyshkin.study.artsfgmsscbrewery.web.model.CustomerDto;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class CustomerServiceImpl implements CustomerService {
    @Override
    public CustomerDto getCustomerById(UUID id) {
        return CustomerDto.builder()
                .id(id)
                .name("Customer Name")
                .build();
    }
}
