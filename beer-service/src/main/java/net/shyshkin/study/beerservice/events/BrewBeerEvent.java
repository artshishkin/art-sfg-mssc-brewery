package net.shyshkin.study.beerservice.events;

import net.shyshkin.study.beerservice.web.model.BeerDto;

public class BrewBeerEvent extends BeerEvent{
    public BrewBeerEvent(BeerDto beerDto) {
        super(beerDto);
    }
}
