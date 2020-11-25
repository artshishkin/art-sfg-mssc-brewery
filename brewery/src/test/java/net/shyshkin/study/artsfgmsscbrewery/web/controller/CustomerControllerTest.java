package net.shyshkin.study.artsfgmsscbrewery.web.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.shyshkin.study.artsfgmsscbrewery.services.CustomerService;
import net.shyshkin.study.artsfgmsscbrewery.web.model.CustomerDto;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static net.shyshkin.study.artsfgmsscbrewery.web.controller.CustomerController.BASE_URL;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CustomerController.class)
class CustomerControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    CustomerService customerService;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    void getCustomer() throws Exception {
        //given
        UUID customerId = UUID.randomUUID();
        CustomerDto stubCustomerDto = CustomerDto.builder().id(customerId).name("BName").build();
        given(customerService.getCustomerById(ArgumentMatchers.any(UUID.class))).willReturn(stubCustomerDto);

        //when
        mockMvc
                .perform(
                        get(BASE_URL + "/{customerId}", customerId)
                                .accept(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(jsonPath("$.id", notNullValue()))
                .andExpect(jsonPath("$.name", notNullValue()));

        //then
        then(customerService).should().getCustomerById(eq(customerId));
    }

    @Test
    void newCustomer() throws Exception {
        //given
        UUID customerId = UUID.randomUUID();
        CustomerDto stubCustomerDto = CustomerDto.builder().id(customerId).name("BName").build();
        given(customerService.saveNewCustomer(ArgumentMatchers.any(CustomerDto.class))).willReturn(stubCustomerDto);
        String customerJsonString = objectMapper.writeValueAsString(stubCustomerDto);

        //when
        mockMvc
                .perform(
                        post(BASE_URL)
                                .contentType(APPLICATION_JSON)
                                .content(customerJsonString)
                                .accept(APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(jsonPath("$.id", notNullValue()))
                .andExpect(jsonPath("$.name", notNullValue()))
                .andExpect(header().string("Location", containsString(BASE_URL)));

        //then
        then(customerService).should().saveNewCustomer(eq(stubCustomerDto));
    }

    @Test
    void updateCustomer() throws Exception {
        //given
        UUID customerId = UUID.randomUUID();
        CustomerDto stubCustomerDto = CustomerDto.builder().name("BName").build();
        String customerJsonString = objectMapper.writeValueAsString(stubCustomerDto);

        //when
        mockMvc
                .perform(
                        put(BASE_URL + "/{customerId}", customerId)
                                .contentType(APPLICATION_JSON)
                                .content(customerJsonString)
                                .accept(APPLICATION_JSON))
                .andExpect(status().isNoContent());

        //then
        then(customerService).should().updateCustomer(eq(customerId), eq(stubCustomerDto));
    }

    @Test
    void deleteCustomer() throws Exception {
        //given
        UUID customerId = UUID.randomUUID();

        //when
        mockMvc
                .perform(
                        delete(BASE_URL + "/{customerId}", customerId)
                                .accept(APPLICATION_JSON))
                .andExpect(status().isNoContent());

        //then
        then(customerService).should().deleteById(eq(customerId));
    }
}