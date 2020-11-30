package net.shyshkin.study.artsfgmsscbrewery.web.controller.v2;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.shyshkin.study.artsfgmsscbrewery.services.v2.BeerServiceV2;
import net.shyshkin.study.artsfgmsscbrewery.web.model.v2.BeerDtoV2;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.constraints.ConstraintDescriptions;
import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.StringUtils;

import java.util.UUID;

import static net.shyshkin.study.artsfgmsscbrewery.web.controller.v2.BeerControllerV2.BASE_URL;
import static net.shyshkin.study.artsfgmsscbrewery.web.model.v2.BeerStyle.*;
import static org.hamcrest.CoreMatchers.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.responseHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.restdocs.snippet.Attributes.key;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;


@ExtendWith(RestDocumentationExtension.class)
@AutoConfigureRestDocs(uriScheme = "http", uriHost = "dev.shyshkin.net", uriPort = 80)
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
        BeerDtoV2 stubBeerDtoV2 = BeerDtoV2.builder().id(beerId)
                .beerName("BName").beerStyle(LAGER).upc(123L).build();
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
                .andExpect(jsonPath("$.beerStyle", notNullValue()))
                .andDo(document(BASE_URL.substring(1) + "-get",
                        pathParameters(
                                parameterWithName("beerId").description("UUID of Beer")
                        ),
                        responseFields(
                                fieldWithPath("id").description("Id of Beer").type(UUID.class.getSimpleName()),
                                fieldWithPath("beerName").description("Name of Beer"),
                                fieldWithPath("beerStyle").description("Beer Style"),
                                fieldWithPath("upc").description("UPC of Beer")
                        )))
        ;

        //then
        then(beerService).should().getBeerById(eq(beerId));
    }

    @Test
    void getFakeBeer_withValidationError() throws Exception {
        //given
        String beerName = "fu";

        //when
        mockMvc
                .perform(
                        get(BASE_URL + "/fake/{beerName}", beerName)
                                .accept(APPLICATION_JSON))
                .andExpect(status().is4xxClientError())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(content().string(containsString("getBeerFakeExample.beerName : length must be between 3 and 10")))
        ;
        //then
        then(beerService).shouldHaveNoInteractions();
    }

    @Test
    void handlePost() throws Exception {
        //given
        UUID beerId = UUID.randomUUID();
        BeerDtoV2 stubBeerDtoV2 = BeerDtoV2.builder().id(beerId).beerName("BName").beerStyle(ALE).upc(123L).build();
        BeerDtoV2 beerToSave = BeerDtoV2.builder().beerName("BName").beerStyle(ALE).upc(123L).build();
        given(beerService.saveNewBeer(ArgumentMatchers.any(BeerDtoV2.class))).willReturn(stubBeerDtoV2);
        String beerJsonString = objectMapper.writeValueAsString(beerToSave);

        ConstrainedFields field = new ConstrainedFields(BeerDtoV2.class);

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
                .andExpect(header().string("Location", containsString(BASE_URL)))

                .andDo(document(BASE_URL.substring(1) + "-new",
                        requestFields(
                                field.withPath("id").ignored(),
                                field.withPath("beerName").description("Name of Beer"),
                                field.withPath("beerStyle").description("Beer Style"),
                                field.withPath("upc").description("UPC of Beer")
                        ),
                        responseHeaders(
                                headerWithName(HttpHeaders.LOCATION).description("Location of Resource")
                        )

                ));

        //then
        then(beerService).should().saveNewBeer(eq(beerToSave));
    }

    @Test
    void updateBeer() throws Exception {
        //given
        UUID beerId = UUID.randomUUID();
        BeerDtoV2 stubBeerDtoV2 = BeerDtoV2.builder().beerName("BName").upc(123L).beerStyle(PILSNER).build();
        String beerJsonString = objectMapper.writeValueAsString(stubBeerDtoV2);

        ConstrainedFields field = new ConstrainedFields(BeerDtoV2.class);

        //when
        mockMvc
                .perform(
                        put(BASE_URL + "/{beerId}", beerId)
                                .contentType(APPLICATION_JSON)
                                .content(beerJsonString)
                                .accept(APPLICATION_JSON))
                .andExpect(status().isNoContent())

                .andDo(document(BASE_URL.substring(1) + "-update",
                        pathParameters(
                                parameterWithName("beerId").description("UUID of Beer")
                        ),
                        requestFields(
                                field.withPath("id").ignored(),
                                field.withPath("beerName").description("Name of Beer"),
                                field.withPath("beerStyle").description("Beer Style"),
                                field.withPath("upc").description("UPC of Beer")
                        )
                ));

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
                .andExpect(content().string(allOf(containsString("upc : must not be null"), containsString("beerName : must not be blank"))));

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
                .andExpect(status().isNoContent())

                .andDo(document(BASE_URL.substring(1) + "-delete",
                        pathParameters(
                                parameterWithName("beerId").description("UUID of Beer")
                        )
                ));

        //then
        then(beerService).should().deleteById(eq(beerId));
    }

    //from docs https://github.com/spring-projects/spring-restdocs/blob/v2.0.5.RELEASE/samples/rest-notes-spring-hateoas/src/test/java/com/example/notes/ApiDocumentation.java
    private static class ConstrainedFields {

        private final ConstraintDescriptions constraintDescriptions;

        ConstrainedFields(Class<?> input) {
            this.constraintDescriptions = new ConstraintDescriptions(input);
        }

        private FieldDescriptor withPath(String path) {
            return fieldWithPath(path).attributes(key("constraints").value(StringUtils
                    .collectionToDelimitedString(this.constraintDescriptions
                            .descriptionsForProperty(path), ". ")));
        }
    }

}