package net.shyshkin.study.beerservice.events;

import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import net.shyshkin.study.beerservice.web.model.BeerDto;

import java.io.Serializable;

@Data
@RequiredArgsConstructor
@Builder
public class BeerEvent implements Serializable {

    static final long serialVersionUID = 3683864662983793612L;

    private final BeerDto beerDto;
}
