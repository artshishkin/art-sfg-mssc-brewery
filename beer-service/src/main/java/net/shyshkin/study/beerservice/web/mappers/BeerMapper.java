package net.shyshkin.study.beerservice.web.mappers;

import net.shyshkin.study.beerdata.dto.BeerDto;
import net.shyshkin.study.beerservice.domain.Beer;
import org.mapstruct.DecoratedWith;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(uses = DateMapper.class)
@DecoratedWith(BeerMapperDecorator.class)
public interface BeerMapper {

    BeerMapper INSTANCE = Mappers.getMapper( BeerMapper.class );

    BeerDto asBeerDto(Beer beer);

    BeerDto asBeerDtoWithInventory(Beer beer);

    Beer asBeer(BeerDto beerDto);
}
