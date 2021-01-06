package net.shyshkin.study.beerorderservice.services;

import net.shyshkin.study.beerorderservice.domain.BeerOrder;

public interface BeerOrderManager {

    BeerOrder newBeerOrder(BeerOrder beerOrder);

}