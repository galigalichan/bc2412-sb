package com.bootcamp.sb.demo_sb_customer;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import com.bootcamp.sb.demo_sb_customer.codewave.ApiResp;
import com.bootcamp.sb.demo_sb_customer.controller.impl.CustomerController;
import com.bootcamp.sb.demo_sb_customer.entity.CustomerEntity;
import com.bootcamp.sb.demo_sb_customer.service.CustomerService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

// Every Test file is a testing environment, with your target bean cycle.
// ! For Unit Test, we don't need service & repository layer (bean).
@WebMvcTest(controllers = CustomerController.class) // ! Context includes all web related beans ONLY.
class CustomerControllerTest {
    @MockBean // decouple implementation // We can perform test as long as interface CustomerService is available. We don't have to wait for CustomerServiceImpl.
    private CustomerService customerService;

    // ! @WebMvcTest inject MockMvc Bean into context. 
    @Autowired
    private MockMvc mockMvc; // pretend Postman

    // @Test
    void testGetAllCustomers() throws Exception {
        // ! Mock behavior for the mock bean
        CustomerEntity customerEntity1 = CustomerEntity.builder()
            .email("test123@gmail.com")
            .name("testname1")
            .id(99L).build();
        CustomerEntity customerEntity2 = CustomerEntity.builder()
            .email("test234@gmail.com")
            .name("testname2")
            .id(999L).build();
        List<CustomerEntity> serviceResult = Arrays.asList(customerEntity1, customerEntity2);

        // Assume the behavior/result for the mock bean
        Mockito.when(customerService.getCustomers()).thenReturn(serviceResult);

        // Test
        // verify result
        ResultActions result = mockMvc.perform(get("/customers"));
        result.andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        // To check the data:
        //Approach 1
        result.andExpect(jsonPath("$.code").value("000000"))
            .andExpect(jsonPath("$.message").value("Success."))
            .andExpect(jsonPath("$.data[0].name").value("testname1"))
            .andExpect(jsonPath("$.data[0].email").value("test123@gmail.com"));

        // Approach 2
        String json = result.andReturn().getResponse().getContentAsString();
        // JSON -> object (Deserialization)
        ApiResp<List<CustomerEntity>> customerEntityArray = 
            new ObjectMapper().readValue(json, new TypeReference<ApiResp<List<CustomerEntity>>>() {});
            
        MatcherAssert.assertThat(customerEntityArray.getCode(),
            Matchers.is("000000"));
        MatcherAssert.assertThat(customerEntityArray.getMessage(),
            Matchers.is("Success."));

        List<CustomerEntity> customerEntities = customerEntityArray.getData();

        MatcherAssert.assertThat(customerEntities, Matchers.containsInAnyOrder(
            Matchers.hasProperty("email", Matchers.equalTo("test123@gmail.com")),
            Matchers.hasProperty("email", Matchers.equalTo("test234@gmail.com"))
        ));
    
    }

    // @Test
    void testCreateCustomer() throws Exception {
    // ! Mock behavior
    CustomerEntity customerEntity = CustomerEntity.builder()
        .email("john@gmail.com")
        .name("John Wong")
        .id(10L).build();

    CustomerEntity customerEntityRequest = CustomerEntity.builder()
        .email("mary@gmail.com")
        .name("Mary Chan")
        .id(11L).build();

    Mockito.when(customerService.createCustomer(customerEntityRequest)).thenReturn(customerEntity);
    
    // ! Prepare Request Body Json (Mary)
    // Serialization
    String requestBodyJohn = new ObjectMapper().writeValueAsString(customerEntityRequest);

    ResultActions result = mockMvc.perform(post("/customer")
        .contentType(MediaType.APPLICATION_JSON)
        .content(requestBodyJohn))
        .andExpect(status().isCreated())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON));

    // ! Get Response, extract response data to validate
    String json = result.andReturn().getResponse().getContentAsString();
  
    // Deserialization
    // ! KEY
    ApiResp<CustomerEntity> apiResp = 
    new ObjectMapper().readValue(json, new TypeReference<ApiResp<CustomerEntity>>() {});
    
    MatcherAssert.assertThat(apiResp.getCode(), Matchers.is("000000"));
    MatcherAssert.assertThat(apiResp.getMessage(), Matchers.is("Success."));
    
    CustomerEntity responseData = apiResp.getData();
    MatcherAssert.assertThat(responseData.getName(), Matchers.is("John Wong"));
    
    verify(customerService, times(1)).createCustomer(customerEntityRequest);
    
    }
}
