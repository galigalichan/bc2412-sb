package com.bootcamp.sb.demo_sb_customer;

import java.util.Arrays;
import java.util.List;

import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.bootcamp.sb.demo_sb_customer.entity.CustomerEntity;
import com.bootcamp.sb.demo_sb_customer.repository.CustomerRepository;
import com.bootcamp.sb.demo_sb_customer.service.CustomerService;

@SpringBootTest
public class CustomerServiceTest {
    @Autowired
    private CustomerService customerService;

    @MockBean
    private CustomerRepository customerRepository;

    // @Test
    void testGetAllCustomers() {
        // Mock berhavior for the mock bean
        CustomerEntity customerEntity1 = CustomerEntity.builder()
            .email("john@gmail.com")
            .name("john")
            .id(1L).build();
        CustomerEntity customerEntity2 = CustomerEntity.builder()
            .email("mary@gmail.com")
            .name("mary")
            .id(2L).build();

        List<CustomerEntity> dbResult = Arrays.asList(customerEntity1, customerEntity2);

        Mockito.when(customerRepository.findAll()).thenReturn(dbResult);
        // test
        List<CustomerEntity> customerEntities = customerService.getCustomers();
        System.out.println("customerEntities=" + customerEntities);
        MatcherAssert.assertThat(customerEntities, Matchers.containsInAnyOrder( // has to contain all possibilites
            Matchers.hasProperty("name", Matchers.equalTo("john")),
            Matchers.hasProperty("name", Matchers.equalTo("mary"))
        ));

        MatcherAssert.assertThat(customerEntities, Matchers.containsInAnyOrder(
            Matchers.hasProperty("email", Matchers.equalTo("john@gmail.com")),
            Matchers.hasProperty("email", Matchers.equalTo("mary@gmail.com"))
        ));

        MatcherAssert.assertThat(customerEntities, Matchers.containsInAnyOrder(
            Matchers.hasProperty("id", Matchers.equalTo("2L")),
            Matchers.hasProperty("id", Matchers.equalTo("1L"))
        ));
        
    }       
}
