package net.shyshkin.study.beerdata.events;

import lombok.NoArgsConstructor;
import net.shyshkin.study.beerdata.dto.BeerDto;

@NoArgsConstructor
public class BrewBeerEvent extends BeerEvent{
    public BrewBeerEvent(BeerDto beerDto) {
        super(beerDto);
    }
}
