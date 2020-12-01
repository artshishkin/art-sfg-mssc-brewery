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
        String jsonBeer = "{\"beer-name\":\"Beer Name\",\"beer-style\":\"Ale\",\"upc\":123123123,\"price\":\"12.99\",\"created-date\":\"2020-12-01T16:15:43+0200\",\"last-modified-date\":\"2020-12-01T16:15:43.9809135+02:00\",\"my-local-date\":\"20201201\",\"beerId\":\"fd2c8266-6580-4fe9-93b2-1560743f7c85\"}";

        //when
        BeerDtoV3 beerDtoV3 = objectMapper.readValue(jsonBeer, BeerDtoV3.class);

        //then
        System.out.println(beerDtoV3);
        assertThat(beerDtoV3.getBeerName()).isNotEmpty();
        assertThat(beerDtoV3).hasNoNullFieldsOrProperties();
    }
}