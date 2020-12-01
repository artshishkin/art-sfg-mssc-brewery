package net.shyshkin.study.artsfgmsscbrewery.web.model.v3;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
@ActiveProfiles("snake")
class BeerDtoV3SnakeTest extends BaseTest {

    @Test
    void testSerializeDto() throws JsonProcessingException {
        //given
        BeerDtoV3 beerDto = getBeerDto();

        //when
        String jsonBeer = objectMapper.writeValueAsString(beerDto);

        //then
        System.out.println(jsonBeer);
        assertThat(jsonBeer).contains("beer_name", "beer_style");
        assertThat(jsonBeer).contains("beerId");
    }

    @Test
    void testDeserializeDto() throws JsonProcessingException {
        //given
        String jsonBeer = "{\"beer_name\":\"Beer Name\",\"beer_style\":\"Ale\",\"upc\":123123123,\"price\":\"12.99\",\"created_date\":\"2020-12-01T16:15:43+0200\",\"last_modified_date\":\"2020-12-01T16:15:43.7909171+02:00\",\"my_local_date\":\"20201201\",\"beerId\":\"bbd61462-2bb7-4b12-b45d-aca9dbc8017b\"}";

        //when
        BeerDtoV3 beerDtoV3 = objectMapper.readValue(jsonBeer, BeerDtoV3.class);

        //then
        System.out.println(beerDtoV3);
        assertThat(beerDtoV3.getBeerName()).isNotEmpty();
        assertThat(beerDtoV3).hasNoNullFieldsOrProperties();
    }
}