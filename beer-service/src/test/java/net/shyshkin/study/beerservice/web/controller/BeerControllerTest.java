package net.shyshkin.study.beerservice.web.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.shyshkin.study.beerservice.web.model.BeerDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static net.shyshkin.study.beerservice.web.controller.BeerController.BASE_URL;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
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
                .andExpect(status().isCreated());
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