package net.shyshkin.study.artsfgmsscbrewery.web.model.v3;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

public class BaseTest {

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
