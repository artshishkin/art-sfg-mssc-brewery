package net.shyshkin.study.beerservice.services;

import net.shyshkin.study.beerservice.web.model.BeerDto;
import net.shyshkin.study.beerservice.web.model.BeerPagedList;
import net.shyshkin.study.beerservice.web.model.BeerStyleEnum;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface BeerService {

    BeerDto getBeerById(UUID beerId, Boolean showInventoryOnHand);

    BeerDto saveNewBeer(BeerDto beerDto);

    BeerDto updateBeer(UUID beerId, BeerDto beerDto);

    BeerPagedList listBeer(String beerName, BeerStyleEnum beerStyle, Pageable pageable, Boolean showInventoryOnHand);

    BeerDto getBeerByUpc(String beerUpc, Boolean showInventoryOnHand);
}
