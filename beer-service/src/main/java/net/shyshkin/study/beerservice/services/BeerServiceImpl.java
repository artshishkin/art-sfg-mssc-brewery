package net.shyshkin.study.beerservice.services;

import lombok.RequiredArgsConstructor;
import net.shyshkin.study.beerservice.domain.Beer;
import net.shyshkin.study.beerservice.repositories.BeerRepository;
import net.shyshkin.study.beerservice.web.mappers.BeerMapper;
import net.shyshkin.study.beerservice.web.model.BeerDto;
import net.shyshkin.study.beerservice.web.model.BeerPagedList;
import net.shyshkin.study.beerservice.web.model.BeerStyleEnum;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BeerServiceImpl implements BeerService {

    private final BeerRepository beerRepository;
    private final BeerMapper beerMapper;

    @Override
    public BeerDto getBeerById(UUID beerId, Boolean showInventoryOnHand) {

        Function<Beer, BeerDto> asBeerDto = showInventoryOnHand ?
                beerMapper::asBeerDtoWithInventory :
                beerMapper::asBeerDto;

        return beerRepository.findById(beerId)
                .map(asBeerDto)
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

    @Override
    public BeerPagedList listBeer(String beerName, BeerStyleEnum beerStyle, Pageable pageable, Boolean showInventoryOnHand) {

        Example<Beer> beerExample = Example.of(Beer.builder()
                .beerName(beerName)
                .beerStyle(beerStyle == null ? null : beerStyle.name()).build());

        Page<Beer> beerPage = beerRepository.findAll(beerExample, pageable);

        Function<Beer, BeerDto> asBeerDto = showInventoryOnHand ?
                beerMapper::asBeerDtoWithInventory :
                beerMapper::asBeerDto;

        List<BeerDto> beerDtos = beerPage.get()
                .map(asBeerDto)
                .collect(Collectors.toList());

        long totalElements = beerPage.getTotalElements();

        Pageable resultPageable = beerPage.getPageable();

        return new BeerPagedList(beerDtos, resultPageable, totalElements);
    }
}
