package net.shyshkin.study.beerorderservice.services;

import net.shyshkin.study.beerorderservice.domain.BeerOrder;

public interface BeerOrderManager {

    String ORDER_ID_HEADER = "order_id";

    BeerOrder newBeerOrder(BeerOrder beerOrder);

}