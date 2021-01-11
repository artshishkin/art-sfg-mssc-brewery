package net.shyshkin.study.beerorderservice.services;

import net.shyshkin.study.beerdata.dto.CustomerDto;

import java.util.List;

public interface CustomerService {
    List<CustomerDto> listAllCustomers();
}
