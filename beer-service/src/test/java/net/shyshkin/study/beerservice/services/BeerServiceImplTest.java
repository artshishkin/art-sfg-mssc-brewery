package net.shyshkin.study.beerservice.services;

import net.shyshkin.study.beerservice.domain.Beer;
import net.shyshkin.study.beerservice.repositories.BeerRepository;
import net.shyshkin.study.beerservice.web.mappers.BeerMapper;
import net.shyshkin.study.beerservice.web.model.BeerPagedList;
import net.shyshkin.study.beerservice.web.model.BeerStyleEnum;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageRequest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.never;

@DataJpaTest
class BeerServiceImplTest {

    @Autowired
    BeerRepository beerRepository;

    BeerService beerService;

    @MockBean
    BeerMapper beerMapper;

    @BeforeEach
    void setUp() {
        beerService = new BeerServiceImpl(beerRepository, beerMapper);
    }

    @ParameterizedTest
    @ValueSource(booleans = {true, false})
    void listBeer(Boolean showInventoryOnHand) {
        //when
        BeerPagedList beerPagedList = beerService.listBeer(null, null, PageRequest.of(0, 5), showInventoryOnHand);

        //then
        assertNotNull(beerPagedList);
        assertThat(beerPagedList.getContent()).hasSize(3);
        if (showInventoryOnHand) {
            then(beerMapper).should(never()).asBeerDto(any());
            then(beerMapper).should(atLeastOnce()).asBeerDtoWithInventory(any());
        } else {
            then(beerMapper).should(never()).asBeerDtoWithInventory(any());
            then(beerMapper).should(atLeastOnce()).asBeerDto(any());
        }
    }

    @ParameterizedTest
    @CsvSource({
            "PORTER,1",
            "IPA,1",
            "PALE_ALE,1",
            "ALE,0"
    })
    void listBeer_byStyle(BeerStyleEnum beerStyle, Integer beerCount) {
        //given
        Boolean showInventoryOnHand = true;

        //when
        BeerPagedList beerPagedList = beerService.listBeer(null, beerStyle, PageRequest.of(0, 5), showInventoryOnHand);

        //then
        assertNotNull(beerPagedList);
        assertThat(beerPagedList.getContent()).hasSize(beerCount);
        then(beerMapper).should(never()).asBeerDto(any());
        if (beerCount > 0)
            then(beerMapper).should(atLeastOnce()).asBeerDtoWithInventory(any(Beer.class));
    }

    @ParameterizedTest
    @CsvSource({
            "PORTER,1",
            "IPA,1",
            "PALE_ALE,1",
            "ALE,0"
    })
    void listBeer_byStyle_doNotShowInventoryOnHand(BeerStyleEnum beerStyle, Integer beerCount) {
        //given
        Boolean showInventoryOnHand = false;

        //when
        BeerPagedList beerPagedList = beerService.listBeer(null, beerStyle, PageRequest.of(0, 5), showInventoryOnHand);

        //then
        assertNotNull(beerPagedList);
        assertThat(beerPagedList.getContent()).hasSize(beerCount);
        then(beerMapper).should(never()).asBeerDtoWithInventory(any(Beer.class));
        if (beerCount > 0)
            then(beerMapper).should(atLeastOnce()).asBeerDto(any());
    }

}