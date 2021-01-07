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
public class AllocateOrderResult implements Serializable {

    static final long serialVersionUID = -8140255571091468689L;

    private BeerOrderDto beerOrderDto;
    private boolean allocationError;
    private boolean pendingInventory;

}
