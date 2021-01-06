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
public class ValidateOrderRequest implements Serializable {

    static final long serialVersionUID = -7010464803302368356L;

    private BeerOrderDto beerOrder;

}
