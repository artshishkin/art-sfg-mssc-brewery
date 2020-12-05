package net.shyshkin.study.beerorderservice.web.mappers;

import net.shyshkin.study.beerorderservice.domain.BeerOrderLine;
import net.shyshkin.study.beerorderservice.services.beerservice.BeerService;
import net.shyshkin.study.beerorderservice.services.beerservice.model.BeerDto;
import net.shyshkin.study.beerorderservice.web.model.BeerOrderLineDto;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.UUID;

public abstract class BeerOrderLineMapperDecorator implements BeerOrderLineMapper {

    private BeerService beerService;

    private BeerOrderLineMapper delegate;

    @Autowired
    @org.springframework.beans.factory.annotation.Qualifier("delegate")
    public void setDelegate(BeerOrderLineMapper delegate) {
        this.delegate = delegate;
    }

    @Autowired
    public void setBeerService(BeerService beerService) {
        this.beerService = beerService;
    }

    @Override
    public BeerOrderLineDto beerOrderLineToDto(BeerOrderLine line) {
        String upc = line.getUpc();
        BeerDto beerDto = beerService.getBeerByUpc(upc);
        String beerName = beerDto.getBeerName();
        UUID beerId = beerDto.getId();
        BeerOrderLineDto dto = delegate.beerOrderLineToDto(line);
        dto.setBeerName(beerName);
        dto.setBeerId(beerId);
        return dto;
    }
}
