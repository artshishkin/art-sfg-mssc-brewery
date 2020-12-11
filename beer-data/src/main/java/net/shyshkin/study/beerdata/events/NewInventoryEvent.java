package net.shyshkin.study.beerdata.events;

import lombok.NoArgsConstructor;
import net.shyshkin.study.beerdata.dto.BeerDto;

@NoArgsConstructor
public class NewInventoryEvent extends BeerEvent{
    public NewInventoryEvent(BeerDto beerDto) {
        super(beerDto);
    }
}
