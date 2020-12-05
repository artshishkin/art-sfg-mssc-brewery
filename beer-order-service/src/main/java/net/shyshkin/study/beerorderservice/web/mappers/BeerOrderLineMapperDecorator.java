package net.shyshkin.study.beerorderservice.web.mappers;

import net.shyshkin.study.beerorderservice.domain.BeerOrderLine;
import net.shyshkin.study.beerorderservice.services.beerservice.BeerService;
import net.shyshkin.study.beerorderservice.services.beerservice.model.BeerDto;
import net.shyshkin.study.beerorderservice.web.model.BeerOrderLineDto;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

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

        BeerOrderLineDto orderLineDto = delegate.beerOrderLineToDto(line);

        String upc = line.getUpc();
        Optional<BeerDto> beerDtoOptional = beerService.getBeerByUpc(upc);

        beerDtoOptional.ifPresent(beerDto -> {
            orderLineDto.setBeerName(beerDto.getBeerName());
            orderLineDto.setBeerStyle(beerDto.getBeerStyle().name());
            orderLineDto.setPrice(beerDto.getPrice());
            orderLineDto.setBeerId(beerDto.getId());
        });

        return orderLineDto;
    }
}
