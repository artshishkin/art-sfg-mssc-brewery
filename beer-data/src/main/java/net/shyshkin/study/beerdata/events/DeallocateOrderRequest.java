package net.shyshkin.study.beerdata.events;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.shyshkin.study.beerdata.dto.BeerOrderDto;

import java.io.Serializable;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DeallocateOrderRequest implements Serializable {

    static final long serialVersionUID = 5147299244421007945L;

    private BeerOrderDto beerOrder;

}
