package net.shyshkin.study.artsfgmsscbrewery.services.v2;

import net.shyshkin.study.artsfgmsscbrewery.web.model.v2.BeerDtoV2;
import net.shyshkin.study.artsfgmsscbrewery.web.model.v2.BeerStyle;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class BeerServiceV2Impl implements BeerServiceV2 {
    @Override
    public BeerDtoV2 getBeerById(UUID beerId) {
        return BeerDtoV2.builder()
                .id(UUID.randomUUID())
                .beerName("Galaxy Cat" + beerId)
                .beerStyle(BeerStyle.ALE)
                .build();
    }

    @Override
    public BeerDtoV2 saveNewBeer(BeerDtoV2 beerDto) {
        beerDto.setId(UUID.randomUUID());
        return beerDto;
    }

    @Override
    public void updateBeer(UUID beerId, BeerDtoV2 beerDto) {
        // TODO: 24.11.2020 Should implement real update method
    }

    @Override
    public void deleteById(UUID beerId) {
        // TODO: 24.11.2020 Should implement real update method
    }
}
