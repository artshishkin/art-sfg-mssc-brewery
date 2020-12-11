package net.shyshkin.study.beerservice.events;

import net.shyshkin.study.beerservice.web.model.BeerDto;

public class NewInventoryEvent extends BeerEvent{
    public NewInventoryEvent(BeerDto beerDto) {
        super(beerDto);
    }
}
