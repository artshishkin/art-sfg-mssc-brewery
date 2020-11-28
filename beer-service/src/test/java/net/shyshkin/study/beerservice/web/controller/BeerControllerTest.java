package net.shyshkin.study.beerservice.web.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.shyshkin.study.beerservice.web.model.BeerDto;
import net.shyshkin.study.beerservice.web.model.BeerStyleEnum;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.UUID;

import static net.shyshkin.study.beerservice.web.controller.BeerController.BASE_URL;
import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.CoreMatchers.containsString;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BeerController.class)
class BeerControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    void getBeerById() throws Exception {
        //given
        UUID beerId = UUID.randomUUID();

        //when
        mockMvc.perform(get(BASE_URL + "/{beerId}", beerId))

                //then
                .andExpect(status().isOk());
    }

    @Test
    void createNewBeer() throws Exception {
        //given
        UUID beerId = UUID.randomUUID();
        BeerDto beerDto = BeerDto.builder()
                .beerName("Beer Name")
                .beerStyle(BeerStyleEnum.PILSNER)
                .upc(123L)
                .price(BigDecimal.valueOf(321L))
                .build();
        String beerJson = objectMapper.writeValueAsString(beerDto);

        //when
        mockMvc
                .perform(
                        post(BASE_URL)
                                .accept(APPLICATION_JSON)
                                .contentType(APPLICATION_JSON)
                                .content(beerJson))

                //then
                .andExpect(status().isCreated());
    }

    @Test
    void createNewBeer_withValidationErrors() throws Exception {
        //given
        UUID beerId = UUID.randomUUID();
        BeerDto beerDto = BeerDto.builder().build();
        String beerJson = objectMapper.writeValueAsString(beerDto);

        //when
        mockMvc
                .perform(
                        post(BASE_URL)
                                .accept(APPLICATION_JSON)
                                .contentType(APPLICATION_JSON)
                                .content(beerJson))

                //then
                .andExpect(status().is4xxClientError())
                .andExpect(
                        content()
                                .string(allOf(
                                        containsString("beerStyle : must not be null"),
                                        containsString("beerName : must not be blank"),
                                        containsString("upc : must not be null"),
                                        containsString("price : must not be null")
                                )));
    }

    @Test
    void updateBeerById() throws Exception {
        //given
        UUID beerId = UUID.randomUUID();
        BeerDto beerDto = BeerDto.builder().build();
        String beerJson = objectMapper.writeValueAsString(beerDto);

        //when
        mockMvc
                .perform(
                        put(BASE_URL + "/{beerId}", beerId)
                                .accept(APPLICATION_JSON)
                                .contentType(APPLICATION_JSON)
                                .content(beerJson))

                //then
                .andExpect(status().isNoContent());
    }
}