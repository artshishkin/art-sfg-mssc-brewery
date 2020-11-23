package net.shyshkin.study.artsfgmsscbrewery.web.controller;

import lombok.RequiredArgsConstructor;
import net.shyshkin.study.artsfgmsscbrewery.services.CustomerService;
import net.shyshkin.study.artsfgmsscbrewery.web.model.CustomerDto;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

import static org.springframework.http.HttpStatus.OK;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/customer")
public class CustomerController {

    private final CustomerService customerService;

    @GetMapping("{customerId}")
    @ResponseStatus(OK)
    public CustomerDto getCustomer(@PathVariable("customerId")UUID id){
        return customerService.getCustomerById(id);
    }
}
