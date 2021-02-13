package net.shyshkin.study.beerorderservice.web.mappers;

import net.shyshkin.study.beerdata.dto.BeerDto;
import net.shyshkin.study.beerdata.dto.BeerOrderLineDto;
import net.shyshkin.study.beerorderservice.domain.BeerOrderLine;
import net.shyshkin.study.beerorderservice.services.beerservice.BeerService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;
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

        BeerOrderLineDto orderLineDto = delegate.beerOrderLineToDto(line);

        UUID beerId = line.getBeerId();
        Optional<BeerDto> beerDtoOptional = beerService.getBeerById(beerId);

        beerDtoOptional.ifPresent(beerDto -> {
            orderLineDto.setBeerName(beerDto.getBeerName());
            orderLineDto.setBeerStyle(beerDto.getBeerStyle().name());
            orderLineDto.setPrice(beerDto.getPrice());
            orderLineDto.setUpc(beerDto.getUpc());
        });

        return orderLineDto;
    }
}
