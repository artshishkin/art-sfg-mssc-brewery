package net.shyshkin.study.artsfgmsscbrewery.web.model.v3;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.test.context.TestPropertySource;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
@TestPropertySource(
        properties = {"spring.jackson.property-naming-strategy=KEBAB_CASE"}
)
class BeerDtoV3KebabTest extends BaseTest {

    @Test
    void testSerializeDto() throws JsonProcessingException {
        //given
        BeerDtoV3 beerDto = getBeerDto();

        //when
        String jsonBeer = objectMapper.writeValueAsString(beerDto);

        //then
        System.out.println(jsonBeer);
        assertThat(jsonBeer).contains("beer-name", "beer-style");
        assertThat(jsonBeer).contains("beerId");
    }

    @Test
    void testDeserializeDto() throws JsonProcessingException {
        //given
        String jsonBeer = "{\"beerId\":\"74412658-2c4c-4f15-a13c-9c70cfc1ba10\",\"beer-name\":\"Beer Name\",\"beer-style\":\"Ale\",\"upc\":123123123,\"price\":12.99,\"created-date\":\"2020-12-01T15:12:14.0674114+02:00\",\"last-modified-date\":\"2020-12-01T15:12:14.0694112+02:00\"}";

        //when
        BeerDtoV3 beerDtoV3 = objectMapper.readValue(jsonBeer, BeerDtoV3.class);

        //then
        System.out.println(beerDtoV3);
        assertThat(beerDtoV3.getBeerName()).isNotEmpty();
        assertThat(beerDtoV3).hasNoNullFieldsOrProperties();
    }
}