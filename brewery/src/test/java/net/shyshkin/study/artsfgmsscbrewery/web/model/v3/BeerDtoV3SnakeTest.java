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
        String jsonBeer = "{\"beerId\":\"bdde476e-8243-4c4b-bad2-997a2b138f29\",\"beer_name\":\"Beer Name\",\"beer_style\":\"Ale\",\"upc\":123123123,\"price\":12.99,\"created_date\":\"2020-12-01T14:45:36.5271882+02:00\",\"last_modified_date\":\"2020-12-01T14:45:36.5281853+02:00\"}";

        //when
        BeerDtoV3 beerDtoV3 = objectMapper.readValue(jsonBeer, BeerDtoV3.class);

        //then
        System.out.println(beerDtoV3);
        assertThat(beerDtoV3.getBeerName()).isNotEmpty();
        assertThat(beerDtoV3).hasNoNullFieldsOrProperties();
    }
}