package net.shyshkin.study.beerservice.services;

import lombok.RequiredArgsConstructor;
import net.shyshkin.study.beerservice.domain.Beer;
import net.shyshkin.study.beerservice.repositories.BeerRepository;
import net.shyshkin.study.beerservice.web.mappers.BeerMapper;
import net.shyshkin.study.beerservice.web.model.BeerDto;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BeerServiceImpl implements BeerService {

    private final BeerRepository beerRepository;
    private final BeerMapper beerMapper;

    @Override
    public BeerDto getBeerById(UUID beerId) {
        return beerRepository.findById(beerId)
                .map(beerMapper::asBeerDto)
                .orElseThrow(() -> new EntityNotFoundException("Beer with id: " + beerId + " not found"));
    }

    @Override
    public BeerDto saveNewBeer(BeerDto beerDto) {
        beerDto.setId(null);
        Beer beer = beerRepository.save(beerMapper.asBeer(beerDto));
        return beerMapper.asBeerDto(beer);
    }

    @Override
    public BeerDto updateBeer(UUID beerId, BeerDto beerDto) {
        if (!beerRepository.existsById(beerId))
            throw new EntityNotFoundException("Beer with id: " + beerId + " not found");
        beerDto.setId(beerId);
        Beer saved = beerRepository.save(beerMapper.asBeer(beerDto));
        return beerMapper.asBeerDto(saved);
    }
}
