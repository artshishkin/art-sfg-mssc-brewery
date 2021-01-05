package net.shyshkin.study.beerservice.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.shyshkin.study.beerdata.dto.BeerDto;
import net.shyshkin.study.beerdata.dto.BeerPagedList;
import net.shyshkin.study.beerdata.dto.BeerStyleEnum;
import net.shyshkin.study.beerservice.domain.Beer;
import net.shyshkin.study.beerservice.repositories.BeerRepository;
import net.shyshkin.study.beerservice.web.mappers.BeerMapper;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class BeerServiceImpl implements BeerService {

    private final BeerRepository beerRepository;
    private final BeerMapper beerMapper;

    @Cacheable(cacheNames = "beerCache", key = "#beerId", condition = "#showInventoryOnHand == false")
    @Override
    public BeerDto getBeerById(UUID beerId, Boolean showInventoryOnHand) {

        log.debug("getBeerById() was called");

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

    @Cacheable(cacheNames = "beerListCache", condition = "#showInventoryOnHand == false")
    @Override
    public BeerPagedList listBeer(String beerName, BeerStyleEnum beerStyle, Pageable pageable, Boolean showInventoryOnHand) {

        log.debug("listBeer() was called");

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

    @Cacheable(cacheNames = "beerUpcCache")
    @Override
    public BeerDto getBeerByUpc(String upc) {

        log.debug("getBeerByUpc() was called");

        return beerRepository.findByUpc(upc)
                .map(beerMapper::asBeerDto)
                .orElseThrow(() -> new EntityNotFoundException("Beer with UPC: " + upc + " not found"));
    }
}
