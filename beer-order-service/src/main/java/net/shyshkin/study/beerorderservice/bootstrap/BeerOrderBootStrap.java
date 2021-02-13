package net.shyshkin.study.beerorderservice.bootstrap;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.shyshkin.study.beerorderservice.domain.Customer;
import net.shyshkin.study.beerorderservice.repositories.CustomerRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

/**
 * Created by jt on 2019-06-06.
 */
@Slf4j
@RequiredArgsConstructor
@Component
public class BeerOrderBootStrap implements CommandLineRunner {
    public static final String TASTING_ROOM = "Tasting Room";

    private final CustomerRepository customerRepository;

    @Override
    public void run(String... args) throws Exception {
        loadCustomerData();
    }

    private void loadCustomerData() {

        Optional<Customer> customerOptional = customerRepository.findByCustomerName(TASTING_ROOM);

        if (customerOptional.isEmpty()) {
            Customer savedCustomer = customerRepository.save(Customer.builder()
                    .customerName(TASTING_ROOM)
                    .apiKey(UUID.randomUUID())
                    .build());
            log.info("##################################################################");
            log.info("# Saved Customer Id: " + savedCustomer.getId()  + "#");
            log.info("##################################################################");
        } else {
            log.info("##################################################################");
            log.info("# Found Customer Id: " + customerOptional.get().getId() + "#");
            log.info("##################################################################");
        }
    }
}
