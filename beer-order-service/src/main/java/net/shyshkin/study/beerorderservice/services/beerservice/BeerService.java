package net.shyshkin.study.beerorderservice.services.beerservice;


import net.shyshkin.study.beerdata.dto.BeerDto;
import net.shyshkin.study.beerdata.dto.BeerPagedList;

import java.util.Optional;
import java.util.UUID;

public interface BeerService {

    Optional<BeerDto> getBeerById(UUID beerId);

    Optional<BeerDto> getBeerByUpc(String upc);

    Optional<BeerPagedList> getListOfBeers();
}
