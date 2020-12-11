package net.shyshkin.study.beerservice.events;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.shyshkin.study.beerservice.web.model.BeerDto;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BeerEvent implements Serializable {

    static final long serialVersionUID = 3683864662983793612L;

    private BeerDto beerDto;
}
