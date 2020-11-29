package net.shyshkin.study.beerservice.web.mappers;

import net.shyshkin.study.beerservice.domain.Beer;
import net.shyshkin.study.beerservice.web.model.BeerDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(uses = DateMapper.class)
public interface BeerMapper {

    @Mapping(source = "minOnHand", target = "quantityOnHand")
    BeerDto asBeerDto(Beer beer);

    @Mapping(source = "quantityOnHand", target = "minOnHand")
    Beer asBeer(BeerDto beerDto);
}
