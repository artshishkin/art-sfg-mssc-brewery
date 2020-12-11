package net.shyshkin.study.beerservice.web.mappers;

import net.shyshkin.study.beerdata.dto.BeerDto;
import net.shyshkin.study.beerservice.domain.Beer;
import net.shyshkin.study.beerservice.services.inventory.BeerInventoryService;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class BeerMapperDecorator implements BeerMapper {

    private BeerInventoryService beerInventoryService;
    private BeerMapper delegate;

    @Autowired
    public void setBeerInventoryService(BeerInventoryService beerInventoryService) {
        this.beerInventoryService = beerInventoryService;
    }

    @Autowired
    public void setDelegate(BeerMapper delegate) {
        this.delegate = delegate;
    }

    @Override
    public BeerDto asBeerDtoWithInventory(Beer beer) {
        BeerDto dto = delegate.asBeerDto(beer);

        Integer onHand = beerInventoryService.getOnHandInventory(beer.getId());
        dto.setQuantityOnHand(onHand);
        return dto;
    }
}
