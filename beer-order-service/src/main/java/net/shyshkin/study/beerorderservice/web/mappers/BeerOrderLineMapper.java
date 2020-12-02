package net.shyshkin.study.beerorderservice.web.mappers;

import net.shyshkin.study.beerorderservice.domain.BeerOrderLine;
import net.shyshkin.study.beerorderservice.web.model.BeerOrderLineDto;
import org.mapstruct.Mapper;

@Mapper(uses = {DateMapper.class})
public interface BeerOrderLineMapper {
    BeerOrderLineDto beerOrderLineToDto(BeerOrderLine line);

    BeerOrderLine dtoToBeerOrderLine(BeerOrderLineDto dto);
}
