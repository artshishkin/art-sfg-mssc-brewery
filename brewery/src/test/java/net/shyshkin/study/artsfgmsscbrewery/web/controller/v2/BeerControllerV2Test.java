package net.shyshkin.study.artsfgmsscbrewery.web.controller.v2;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.shyshkin.study.artsfgmsscbrewery.services.v2.BeerServiceV2;
import net.shyshkin.study.artsfgmsscbrewery.web.model.v2.BeerDtoV2;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static net.shyshkin.study.artsfgmsscbrewery.web.controller.v2.BeerControllerV2.BASE_URL;
import static net.shyshkin.study.artsfgmsscbrewery.web.model.v2.BeerStyle.*;
import static org.hamcrest.CoreMatchers.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BeerControllerV2.class)
class BeerControllerV2Test {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    BeerServiceV2 beerService;

    @Autowired
    ObjectMapper objectMapper;


    @Test
    void getBeer() throws Exception {
        //given
        UUID beerId = UUID.randomUUID();
        BeerDtoV2 stubBeerDtoV2 = BeerDtoV2.builder().id(beerId).beerName("BName").beerStyle(LAGER).build();
        given(beerService.getBeerById(ArgumentMatchers.any(UUID.class))).willReturn(stubBeerDtoV2);

        //when
        mockMvc
                .perform(
                        get(BASE_URL + "/{beerId}", beerId)
                                .accept(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(jsonPath("$.id", notNullValue()))
                .andExpect(jsonPath("$.beerName", notNullValue()))
                .andExpect(jsonPath("$.beerStyle", notNullValue()));

        //then
        then(beerService).should().getBeerById(eq(beerId));
    }

    @Test
    void handlePost() throws Exception {
        //given
        UUID beerId = UUID.randomUUID();
        BeerDtoV2 stubBeerDtoV2 = BeerDtoV2.builder().id(beerId).beerName("BName").beerStyle(ALE).upc(123L).build();
        BeerDtoV2 beerToSave = BeerDtoV2.builder().beerName("BName").beerStyle(ALE).upc(123L).build();
        given(beerService.saveNewBeer(ArgumentMatchers.any(BeerDtoV2.class))).willReturn(stubBeerDtoV2);
        String beerJsonString = objectMapper.writeValueAsString(beerToSave);

        //when
        mockMvc
                .perform(
                        post(BASE_URL)
                                .contentType(APPLICATION_JSON)
                                .content(beerJsonString)
                                .accept(APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(jsonPath("$.id", notNullValue()))
                .andExpect(jsonPath("$.beerName", notNullValue()))
                .andExpect(jsonPath("$.beerStyle", notNullValue()))
                .andExpect(header().string("Location", containsString(BASE_URL)));

        //then
        then(beerService).should().saveNewBeer(eq(beerToSave));
    }

    @Test
    void updateBeer() throws Exception {
        //given
        UUID beerId = UUID.randomUUID();
        BeerDtoV2 stubBeerDtoV2 = BeerDtoV2.builder().beerName("BName").upc(123L).beerStyle(PILSNER).build();
        String beerJsonString = objectMapper.writeValueAsString(stubBeerDtoV2);

        //when
        mockMvc
                .perform(
                        put(BASE_URL + "/{beerId}", beerId)
                                .contentType(APPLICATION_JSON)
                                .content(beerJsonString)
                                .accept(APPLICATION_JSON))
                .andExpect(status().isNoContent());

        //then
        then(beerService).should().updateBeer(eq(beerId), eq(stubBeerDtoV2));
    }

    @Test
    void updateBeer_withValidationErrors() throws Exception {
        //given
        UUID beerId = UUID.randomUUID();
        BeerDtoV2 stubBeerDtoV2 = BeerDtoV2.builder().beerName(null).upc(null).beerStyle(PILSNER).build();
        String beerJsonString = objectMapper.writeValueAsString(stubBeerDtoV2);

        //when
        mockMvc
                .perform(
                        put(BASE_URL + "/{beerId}", beerId)
                                .contentType(APPLICATION_JSON)
                                .content(beerJsonString)
                                .accept(APPLICATION_JSON))
                .andExpect(status().is4xxClientError())
                .andExpect(content().string(allOf(containsString("upc must not be null"), containsString("beerName must not be blank"))));

        //then
        then(beerService).shouldHaveNoInteractions();
    }

    @Test
    void deleteBeer() throws Exception {
        //given
        UUID beerId = UUID.randomUUID();

        //when
        mockMvc
                .perform(
                        delete(BASE_URL + "/{beerId}", beerId)
                                .accept(APPLICATION_JSON))
                .andExpect(status().isNoContent());

        //then
        then(beerService).should().deleteById(eq(beerId));
    }

}