package net.shyshkin.study.artsfgmsscbrewery.web.model.v3;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;

@JsonTest
class BeerDtoV3Test extends BaseTest {

    @Autowired
    ObjectMapper objectMapper;

    @Test
    void testSerializeDto() throws JsonProcessingException {
        //given
        BeerDtoV3 beerDto = getBeerDto();

        //when
        String jsonBeer = objectMapper.writeValueAsString(beerDto);

        //then
        System.out.println(jsonBeer);

    }

    @Test
    void testDeserializeDto() throws JsonProcessingException {
        //given
        String jsonBeer = "{\"id\":\"9f5d5906-f507-42e8-aa1c-a4d6f290afb0\",\"beerName\":\"Beer Name\",\"beerStyle\":\"Ale\",\"upc\":123123123,\"price\":12.99,\"createdDate\":\"2020-12-01T13:34:12.7176528+02:00\",\"lastModifiedDate\":\"2020-12-01T13:34:12.7186534+02:00\"}";

        //when
        BeerDtoV3 beerDtoV3 = objectMapper.readValue(jsonBeer, BeerDtoV3.class);

        //then
        System.out.println(beerDtoV3);
    }
}