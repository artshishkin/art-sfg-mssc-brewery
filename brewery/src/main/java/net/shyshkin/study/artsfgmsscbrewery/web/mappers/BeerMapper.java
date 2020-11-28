package net.shyshkin.study.artsfgmsscbrewery.web.mappers;

import net.shyshkin.study.artsfgmsscbrewery.domain.Beer;
import net.shyshkin.study.artsfgmsscbrewery.web.model.BeerDto;
import net.shyshkin.study.artsfgmsscbrewery.web.model.v2.BeerDtoV2;
import org.mapstruct.Mapper;

@Mapper
public interface BeerMapper {

    Beer beerDtoToBeer(BeerDto beerDto);
    BeerDto beerToBeerDto(Beer beer);

    Beer beerDtoV2ToBeer(BeerDtoV2 beerDtoV2);
    BeerDtoV2 beerToBeerDtoV2(Beer beer);
}
