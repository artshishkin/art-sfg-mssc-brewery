package net.shyshkin.study.artsfgmsscbrewery.web.model.v3;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import javax.validation.constraints.Positive;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BeerDtoV3 {
    @Null
    private UUID id;
    @NotBlank
    private String beerName;
    @NotBlank
    private String beerStyle;
    @NotNull
    @Positive
    private Long upc;

    private BigDecimal price;

    @Null
    private OffsetDateTime createdDate;
    @Null
    private OffsetDateTime lastModifiedDate;
}
