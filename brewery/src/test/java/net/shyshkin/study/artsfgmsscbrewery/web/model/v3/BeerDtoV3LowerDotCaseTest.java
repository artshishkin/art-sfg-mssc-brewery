package net.shyshkin.study.artsfgmsscbrewery.web.model.v3;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.test.context.TestPropertySource;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
@TestPropertySource(
        properties = {"spring.jackson.property-naming-strategy=LOWER_DOT_CASE"}
)
class BeerDtoV3LowerDotCaseTest extends BaseTest {

    @Test
    void testSerializeDto() throws JsonProcessingException {
        //given
        BeerDtoV3 beerDto = getBeerDto();

        //when
        String jsonBeer = objectMapper.writeValueAsString(beerDto);

        //then
        System.out.println(jsonBeer);
        assertThat(jsonBeer).contains("beer.name", "beer.style");
        assertThat(jsonBeer).contains("beerId");
    }

    @Test
    void testDeserializeDto() throws JsonProcessingException {
        //given
        String jsonBeer = "{\"beerId\":\"bc86a975-bddc-4e47-8521-89d77b2181c9\",\"beer.name\":\"Beer Name\",\"beer.style\":\"Ale\",\"upc\":123123123,\"price\":12.99,\"created.date\":\"2020-12-01T15:23:39.1527031+02:00\",\"last.modified.date\":\"2020-12-01T15:23:39.1537029+02:00\"}";

        //when
        BeerDtoV3 beerDtoV3 = objectMapper.readValue(jsonBeer, BeerDtoV3.class);

        //then
        System.out.println(beerDtoV3);
        assertThat(beerDtoV3.getBeerName()).isNotEmpty();
        assertThat(beerDtoV3).hasNoNullFieldsOrProperties();
    }
}