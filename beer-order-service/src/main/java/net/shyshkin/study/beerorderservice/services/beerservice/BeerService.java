package net.shyshkin.study.beerorderservice.services.beerservice;

import net.shyshkin.study.beerorderservice.services.beerservice.model.BeerDto;

public interface BeerService {
    BeerDto getBeerByUpc(String upc);
}
