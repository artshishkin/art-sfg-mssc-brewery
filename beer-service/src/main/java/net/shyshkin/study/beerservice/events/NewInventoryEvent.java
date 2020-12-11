package net.shyshkin.study.beerservice.events;

import lombok.NoArgsConstructor;
import net.shyshkin.study.beerservice.web.model.BeerDto;

@NoArgsConstructor
public class NewInventoryEvent extends BeerEvent{
    public NewInventoryEvent(BeerDto beerDto) {
        super(beerDto);
    }
}
