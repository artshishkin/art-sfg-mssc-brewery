package net.shyshkin.study.artsfgmsscbrewery.web.controller;

import lombok.RequiredArgsConstructor;
import net.shyshkin.study.artsfgmsscbrewery.services.CustomerService;
import net.shyshkin.study.artsfgmsscbrewery.web.model.CustomerDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.net.URI;
import java.util.UUID;

import static net.shyshkin.study.artsfgmsscbrewery.web.controller.CustomerController.BASE_URL;
import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequiredArgsConstructor
@RequestMapping(BASE_URL)
public class CustomerController {

    public static final String BASE_URL = "/api/v1/customer";

    private final CustomerService customerService;

    @GetMapping("{customerId}")
    @ResponseStatus(OK)
    public CustomerDto getCustomer(@PathVariable("customerId") UUID id) {
        return customerService.getCustomerById(id);
    }

    @PostMapping
    public ResponseEntity<CustomerDto> newCustomer(@RequestBody CustomerDto customerDto, HttpServletRequest request) {
        CustomerDto savedCustomer = customerService.saveNewCustomer(customerDto);
        String resourceUrl = request.getRequestURL().append("/").append(savedCustomer.getId()).toString();
        return ResponseEntity.created(URI.create(resourceUrl)).body(savedCustomer);
    }

    @PutMapping("{customerId}")
    @ResponseStatus(NO_CONTENT)
    public void updateCustomer(@PathVariable("customerId") UUID customerId, @RequestBody CustomerDto customerDto) {
        customerService.updateCustomer(customerId, customerDto);
    }

    @DeleteMapping("{customerId}")
    @ResponseStatus(NO_CONTENT)
    public void deleteCustomer(@PathVariable("customerId") UUID customerId) {
        customerService.deleteById(customerId);
    }


}
