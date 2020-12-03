package net.shyshkin.study.beerservice.services;

import net.shyshkin.study.beerservice.repositories.BeerRepository;
import net.shyshkin.study.beerservice.web.mappers.BeerMapper;
import net.shyshkin.study.beerservice.web.model.BeerPagedList;
import net.shyshkin.study.beerservice.web.model.BeerStyleEnum;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageRequest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;

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

    @Test
    void listBeer() {
        //when
        BeerPagedList beerPagedList = beerService.listBeer(null, null, PageRequest.of(0, 5));

        //then
        assertNotNull(beerPagedList);
        assertThat(beerPagedList.getContent()).hasSize(3);
    }

    @ParameterizedTest
    @CsvSource({
            "PORTER,1",
            "IPA,1",
            "PALE_ALE,1",
            "ALE,0"
    })
    void listBeer_byStyle(BeerStyleEnum beerStyle, Integer beerCount) {
        //when
        BeerPagedList beerPagedList = beerService.listBeer(null, beerStyle, PageRequest.of(0, 5));

        //then
        assertNotNull(beerPagedList);
        assertThat(beerPagedList.getContent()).hasSize(beerCount);
    }

}