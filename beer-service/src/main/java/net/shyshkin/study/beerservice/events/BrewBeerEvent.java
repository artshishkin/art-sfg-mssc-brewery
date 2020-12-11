package net.shyshkin.study.beerservice.events;

import lombok.NoArgsConstructor;
import net.shyshkin.study.beerservice.web.model.BeerDto;

@NoArgsConstructor
public class BrewBeerEvent extends BeerEvent{
    public BrewBeerEvent(BeerDto beerDto) {
        super(beerDto);
    }
}
