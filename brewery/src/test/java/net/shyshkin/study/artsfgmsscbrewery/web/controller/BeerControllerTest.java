package net.shyshkin.study.artsfgmsscbrewery.web.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.shyshkin.study.artsfgmsscbrewery.services.BeerService;
import net.shyshkin.study.artsfgmsscbrewery.web.model.BeerDto;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static net.shyshkin.study.artsfgmsscbrewery.web.controller.BeerController.BASE_URL;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BeerController.class)
class BeerControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    BeerService beerService;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    void getBeer() throws Exception {
        //given
        UUID beerId = UUID.randomUUID();
        BeerDto stubBeerDto = BeerDto.builder().id(beerId).beerName("BName").beerStyle("BStyle").build();
        given(beerService.getBeerById(ArgumentMatchers.any(UUID.class))).willReturn(stubBeerDto);

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
        BeerDto stubBeerDto = BeerDto.builder().id(beerId).beerName("BName").beerStyle("BStyle").upc(123L).build();
        BeerDto beerToSave = BeerDto.builder().beerName("BName").beerStyle("BStyle").upc(123L).build();
        given(beerService.saveNewBeer(ArgumentMatchers.any(BeerDto.class))).willReturn(stubBeerDto);
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
        BeerDto stubBeerDto = BeerDto.builder().beerName("BName").beerStyle("BStyle").upc(123L).build();
        String beerJsonString = objectMapper.writeValueAsString(stubBeerDto);

        //when
        mockMvc
                .perform(
                        put(BASE_URL + "/{beerId}", beerId)
                                .contentType(APPLICATION_JSON)
                                .content(beerJsonString)
                                .accept(APPLICATION_JSON))
                .andExpect(status().isNoContent());

        //then
        then(beerService).should().updateBeer(eq(beerId), eq(stubBeerDto));
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