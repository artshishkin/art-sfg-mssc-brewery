package net.shyshkin.study.beerinventoryservice.services;

import net.shyshkin.study.beerdata.dto.BeerOrderDto;

public interface AllocateOrder {

    Boolean allocateOrder(BeerOrderDto beerOrderDto);

}
