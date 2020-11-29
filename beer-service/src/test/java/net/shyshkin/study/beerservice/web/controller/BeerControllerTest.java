package net.shyshkin.study.beerservice.web.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.shyshkin.study.beerservice.web.model.BeerDto;
import net.shyshkin.study.beerservice.web.model.BeerStyleEnum;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.constraints.ConstraintDescriptions;
import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.UUID;

import static net.shyshkin.study.beerservice.web.controller.BeerController.BASE_URL;
import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.CoreMatchers.containsString;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.restdocs.snippet.Attributes.key;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@ExtendWith(RestDocumentationExtension.class)
@AutoConfigureRestDocs(uriScheme = "https", uriHost = "dev.shyshkin.net", uriPort = 443)
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
        mockMvc
                .perform(
                        get(BASE_URL + "/{beerId}", beerId)
                                .param("iscold", "yes"))

                //then
                .andExpect(status().isOk())
                .andDo(document("v1/beer",
                        pathParameters(
                                parameterWithName("beerId").description("UUID of desired beer to get.")
                        ),
                        requestParameters(
                                parameterWithName("iscold")
                                        .description("Is Beer Cold Query Parameter")
                                        .optional()
                        ),
                        responseFields(
                                fieldWithPath("id").description("Id of Beer"),
                                fieldWithPath("version").description("Version number"),
                                fieldWithPath("createdDate").description("Created Date"),
                                fieldWithPath("lastModifiedDate").description("Last Modified Date"),
                                fieldWithPath("beerName").description("Beer Name"),
                                fieldWithPath("beerStyle").description("Beer Style"),
                                fieldWithPath("upc").description("Upc of Beer"),
                                fieldWithPath("price").description("Price of Beer"),
                                fieldWithPath("quantityOnHand").description("Quantity On Hand")
                        )
                ));
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

        ConstrainedFields field = new ConstrainedFields(BeerDto.class);

        //when
        mockMvc
                .perform(
                        post(BASE_URL)
                                .accept(APPLICATION_JSON)
                                .contentType(APPLICATION_JSON)
                                .content(beerJson))

                //then
                .andExpect(status().isCreated())
                .andDo(
                        document("v1/beer",
                                requestFields(
                                        field.withPath("id").ignored(),
                                        field.withPath("version").ignored(),
                                        field.withPath("createdDate").ignored(),
                                        field.withPath("lastModifiedDate").ignored(),

                                        field.withPath("beerName").description("Beer Name"),
                                        field.withPath("beerStyle").description("Beer Style"),
                                        field.withPath("upc").description("Upc of Beer"),
                                        field.withPath("price").description("Price of Beer"),
                                        field.withPath("quantityOnHand").ignored()
                                )));
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