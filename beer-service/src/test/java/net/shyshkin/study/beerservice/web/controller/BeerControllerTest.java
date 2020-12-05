package net.shyshkin.study.beerservice.web.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.shyshkin.study.beerservice.services.BeerService;
import net.shyshkin.study.beerservice.web.model.BeerDto;
import net.shyshkin.study.beerservice.web.model.BeerPagedList;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.constraints.ConstraintDescriptions;
import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static net.shyshkin.study.beerservice.web.controller.BeerController.BEER_UPC_URL;
import static net.shyshkin.study.beerservice.web.controller.BeerController.BEER_URL;
import static net.shyshkin.study.beerservice.web.model.BeerStyleEnum.ALE;
import static net.shyshkin.study.beerservice.web.model.BeerStyleEnum.PILSNER;
import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.CoreMatchers.containsString;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.responseHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.restdocs.snippet.Attributes.key;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@ExtendWith(RestDocumentationExtension.class)
@AutoConfigureRestDocs(uriScheme = "https", uriHost = "dev.shyshkin.net", uriPort = 443)
@WebMvcTest(BeerController.class)
class BeerControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    BeerService beerService;

    private BeerDto stubBeer;
    private UUID beerId;

    @BeforeEach
    void setUp() {
        beerId = UUID.randomUUID();
        stubBeer = BeerDto.builder()
                .id(beerId)
                .beerName("Beer Name")
                .beerStyle(PILSNER)
                .upc("0631234200036")
                .price(BigDecimal.valueOf(321L))
                .createdDate(OffsetDateTime.now())
                .lastModifiedDate(OffsetDateTime.now())
                .quantityOnHand(4)
                .version(1)
                .build();
    }

    @Nested
    class ListBeersTests {

        @Test
        void listBeers_Success() throws Exception {
            //given
            Boolean showInventoryOnHand = true;
            List<BeerDto> stubBeerList = stubBeerList(5);
            PageRequest pageable = PageRequest.of(1, 5);
            BeerPagedList beerPagedList = new BeerPagedList(stubBeerList, pageable, 10);
            String jsonAnswer = objectMapper.writeValueAsString(beerPagedList);
            given(beerService.listBeer(anyString(), any(), any(), anyBoolean())).willReturn(beerPagedList);

            //when
            mockMvc
                    .perform(
                            get(BEER_URL)
                                    .param("pageNumber", "1")
                                    .param("pageSize", "5")
                                    .param("beerName", "ArtBeer")
                                    .param("beerStyle", ALE.name())
                                    .param("showInventoryOnHand", showInventoryOnHand.toString())
                    )
                    .andExpect(status().isOk())
                    .andExpect(content().string(CoreMatchers.containsString("beerName")))
                    .andExpect(jsonPath("$.number", CoreMatchers.equalTo(1)))
                    .andExpect(jsonPath("$.pageable.pageNumber", CoreMatchers.equalTo(1)))
                    .andExpect(jsonPath("$.size", CoreMatchers.equalTo(5)))
                    .andExpect(jsonPath("$.pageable.pageSize", CoreMatchers.equalTo(5)))
                    .andExpect(content().json(jsonAnswer))
            ;

            //then
            then(beerService).should().listBeer(eq("ArtBeer"), eq(ALE), eq(pageable), eq(showInventoryOnHand));

        }

        @Test
        void listBeers_Success_withDefaults() throws Exception {
            //given
            Boolean showInventoryOnHand = true;
            List<BeerDto> stubBeerList = stubBeerList(25);
            PageRequest pageable = PageRequest.of(0, 25);
            BeerPagedList beerPagedList = new BeerPagedList(stubBeerList, pageable, 10);
            String jsonAnswer = objectMapper.writeValueAsString(beerPagedList);
            given(beerService.listBeer(anyString(), any(), any(), anyBoolean())).willReturn(beerPagedList);

            //when
            mockMvc
                    .perform(
                            get(BEER_URL)
                                    .param("beerName", "ArtBeer")
                                    .param("beerStyle", ALE.name())
                                    .param("showInventoryOnHand", showInventoryOnHand.toString())

                    )
                    .andExpect(status().isOk())
                    .andExpect(content().string(CoreMatchers.containsString("beerName")))
                    .andExpect(jsonPath("$.number", CoreMatchers.equalTo(0)))
                    .andExpect(jsonPath("$.pageable.pageNumber", CoreMatchers.equalTo(0)))
                    .andExpect(jsonPath("$.size", CoreMatchers.equalTo(25)))
                    .andExpect(jsonPath("$.pageable.pageSize", CoreMatchers.equalTo(25)))
                    .andExpect(content().json(jsonAnswer))
            ;

            //then
            then(beerService).should().listBeer(eq("ArtBeer"), eq(ALE), eq(pageable), eq(showInventoryOnHand));

        }

        @Test
        void listBeers_Success_ValidationError() throws Exception {
            //when
            mockMvc
                    .perform(
                            get(BEER_URL)
                                    .param("pageNumber", "-1")
                                    .param("pageSize", "-5")
                                    .param("beerName", "ArtBeer")
                                    .param("beerStyle", ALE.name())
                    )
                    .andExpect(status().is4xxClientError())
                    .andExpect(content().string(
                            CoreMatchers.allOf(
                                    CoreMatchers.containsString("listBeers.pageNumber : must be greater than or equal to 0"),
                                    CoreMatchers.containsString("listBeers.pageSize : must be greater than 0")
                            )
                    ));

            //then
            then(beerService).shouldHaveNoInteractions();

        }

        private static final String BEER_UPC_PATTERN = "06312342003%02d";

        private List<BeerDto> stubBeerList(int size) {
            return IntStream.rangeClosed(1, size)
                    .mapToObj(this::stubBeer)
                    .collect(Collectors.toList());
        }

        private BeerDto stubBeer(int index) {
            return BeerDto.builder()
                    .id(UUID.randomUUID())
                    .version(1)
                    .createdDate(OffsetDateTime.now())
                    .lastModifiedDate(OffsetDateTime.now())
                    .beerName("BeerName" + index)
                    .beerStyle(ALE)
                    .price(BigDecimal.valueOf(1.11 * (index)))
                    .upc(String.format(BEER_UPC_PATTERN, index))
                    .quantityOnHand(111 * index)
                    .build();
        }

    }

    @Test
    void getBeerById() throws Exception {
        //given
        Boolean showInventoryOnHand = true;
        given(beerService.getBeerById(any(UUID.class), anyBoolean())).willReturn(stubBeer);

        //when
        mockMvc
                .perform(
                        get(BEER_URL + "/{beerId}", beerId)
                                .param("showInventoryOnHand", showInventoryOnHand.toString()))

                //then
                .andExpect(status().isOk())
                .andDo(document("v1/beer-get",
                        pathParameters(
                                parameterWithName("beerId").description("UUID of desired beer to get.")
                        ),
                        requestParameters(
                                parameterWithName("showInventoryOnHand")
                                        .description("Show Inventory On Hand")
                                        .optional()
                        ),
                        responseFields(
                                fieldWithPath("id").description("Id of Beer").type("UUID"),
                                fieldWithPath("version").description("Version number"),
                                fieldWithPath("createdDate").description("Created Date").type(OffsetDateTime.class.getSimpleName()),
                                fieldWithPath("lastModifiedDate").description("Last Modified Date").type(OffsetDateTime.class.getName()),
                                fieldWithPath("beerName").description("Beer Name"),
                                fieldWithPath("beerStyle").description("Beer Style"),
                                fieldWithPath("upc").description("Upc of Beer"),
                                fieldWithPath("price").description("Price of Beer"),
                                fieldWithPath("quantityOnHand").description("Quantity On Hand")
                        )
                ));
        then(beerService).should().getBeerById(eq(beerId), eq(showInventoryOnHand));
    }

    @Test
    void getBeerByUpc() throws Exception {
        //given
        given(beerService.getBeerByUpc(anyString())).willReturn(stubBeer);
        String upc= "0631234200036";

        //when
        mockMvc
                .perform(
                        get(BEER_UPC_URL + "/{upc}", upc)                                )

                //then
                .andExpect(status().isOk())
                .andDo(document("v1/beer-get-by-upc",
                        pathParameters(
                                parameterWithName("upc").description("UPC of desired beer to get.")
                        ),
                        responseFields(
                                fieldWithPath("id").description("Id of Beer").type("UUID"),
                                fieldWithPath("version").description("Version number"),
                                fieldWithPath("createdDate").description("Created Date").type(OffsetDateTime.class.getSimpleName()),
                                fieldWithPath("lastModifiedDate").description("Last Modified Date").type(OffsetDateTime.class.getName()),
                                fieldWithPath("beerName").description("Beer Name"),
                                fieldWithPath("beerStyle").description("Beer Style"),
                                fieldWithPath("upc").description("Upc of Beer"),
                                fieldWithPath("price").description("Price of Beer"),
                                fieldWithPath("quantityOnHand").description("Quantity On Hand")
                        )
                ));
        then(beerService).should().getBeerByUpc(eq(upc));
    }

    @Test
    void createNewBeer() throws Exception {
        //given
        BeerDto beerDto = BeerDto.builder()
                .beerName(stubBeer.getBeerName())
                .beerStyle(stubBeer.getBeerStyle())
                .upc(stubBeer.getUpc())
                .price(stubBeer.getPrice())
                .build();
        String beerJson = objectMapper.writeValueAsString(beerDto);
        given(beerService.saveNewBeer(any(BeerDto.class))).willReturn(stubBeer);

        ConstrainedFields field = new ConstrainedFields(BeerDto.class);

        //when
        mockMvc
                .perform(
                        post(BEER_URL)
                                .accept(APPLICATION_JSON)
                                .contentType(APPLICATION_JSON)
                                .content(beerJson))

                //then
                .andExpect(status().isCreated())
                .andDo(
                        document("v1/beer-new",
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
                                ),
                                responseHeaders(
                                        headerWithName(HttpHeaders.LOCATION).description("Location of Resource"))
                        ));
        then(beerService).should().saveNewBeer(any(BeerDto.class));
    }

    @Test
    void createNewBeer_withValidationErrors() throws Exception {
        //given
        BeerDto beerDto = BeerDto.builder().build();
        String beerJson = objectMapper.writeValueAsString(beerDto);

        //when
        mockMvc
                .perform(
                        post(BEER_URL)
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
                                        containsString("upc : must not be blank"),
                                        containsString("price : must not be null")
                                )));
        then(beerService).shouldHaveNoInteractions();
    }

    @Test
    void updateBeerById() throws Exception {
        //given
        BeerDto beerDto = BeerDto.builder()
                .beerName(stubBeer.getBeerName())
                .beerStyle(stubBeer.getBeerStyle())
                .upc(stubBeer.getUpc())
                .price(stubBeer.getPrice())
                .build();
        String beerJson = objectMapper.writeValueAsString(beerDto);

        //when
        mockMvc
                .perform(
                        put(BEER_URL + "/{beerId}", beerId)
                                .accept(APPLICATION_JSON)
                                .contentType(APPLICATION_JSON)
                                .content(beerJson))

                //then
                .andExpect(status().isNoContent());
        then(beerService).should().updateBeer(eq(beerId), any(BeerDto.class));
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