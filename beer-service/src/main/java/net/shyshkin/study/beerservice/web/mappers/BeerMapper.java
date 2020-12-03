package net.shyshkin.study.beerservice.web.mappers;

import net.shyshkin.study.beerservice.domain.Beer;
import net.shyshkin.study.beerservice.web.model.BeerDto;
import org.mapstruct.DecoratedWith;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(uses = DateMapper.class)
@DecoratedWith(BeerMapperDecorator.class)
public interface BeerMapper {

    BeerMapper INSTANCE = Mappers.getMapper( BeerMapper.class );

    @Mapping(source = "minOnHand", target = "quantityOnHand")
    BeerDto asBeerDto(Beer beer);

    @Mapping(source = "quantityOnHand", target = "minOnHand")
    Beer asBeer(BeerDto beerDto);
}
