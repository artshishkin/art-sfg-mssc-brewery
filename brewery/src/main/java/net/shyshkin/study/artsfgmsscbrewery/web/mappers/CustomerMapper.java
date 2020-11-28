package net.shyshkin.study.artsfgmsscbrewery.web.mappers;

import net.shyshkin.study.artsfgmsscbrewery.domain.Customer;
import net.shyshkin.study.artsfgmsscbrewery.web.model.CustomerDto;
import org.mapstruct.Mapper;

@Mapper
public interface CustomerMapper {
    Customer customerDtoToCustomer(CustomerDto customerDto);

    CustomerDto customerToCustomerDto(Customer customer);
}
