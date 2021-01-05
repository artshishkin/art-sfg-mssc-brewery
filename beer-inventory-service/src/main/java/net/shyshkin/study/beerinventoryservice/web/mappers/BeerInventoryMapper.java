package net.shyshkin.study.beerinventoryservice.web.mappers;

import net.shyshkin.study.beerdata.dto.BeerInventoryDto;
import net.shyshkin.study.beerinventoryservice.domain.BeerInventory;
import org.mapstruct.Mapper;

/**
 * Created by jt on 2019-05-31.
 */
@Mapper(uses = {DateMapper.class})
public interface BeerInventoryMapper {

    BeerInventory beerInventoryDtoToBeerInventory(BeerInventoryDto beerInventoryDTO);

    BeerInventoryDto beerInventoryToBeerInventoryDto(BeerInventory beerInventory);
}
