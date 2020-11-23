package net.shyshkin.study.artsfgmsscbrewery.services;

import net.shyshkin.study.artsfgmsscbrewery.web.model.BeerDto;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class BeerServiceImpl implements BeerService {
    @Override
    public BeerDto getBeerById(UUID beerId) {
        return BeerDto.builder()
                .id(UUID.randomUUID())
                .beerName("Galaxy Cat" + beerId)
                .beerStyle("Pale Ale")
                .build();
    }
}