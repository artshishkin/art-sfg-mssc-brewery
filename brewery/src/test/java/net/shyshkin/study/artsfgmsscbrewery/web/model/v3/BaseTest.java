package net.shyshkin.study.artsfgmsscbrewery.web.model.v3;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

public class BaseTest {

    @Autowired
    ObjectMapper objectMapper;

    BeerDtoV3 getBeerDto() {
        return BeerDtoV3.builder()
                .beerName("Beer Name")
                .beerStyle("Ale")
                .id(UUID.randomUUID())
                .price(new BigDecimal("12.99"))
                .createdDate(OffsetDateTime.now())
                .lastModifiedDate(OffsetDateTime.now())
                .upc(123123123L)
                .build();
    }
}
