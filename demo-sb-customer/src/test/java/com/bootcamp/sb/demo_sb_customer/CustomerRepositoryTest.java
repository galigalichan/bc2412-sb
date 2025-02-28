package com.bootcamp.sb.demo_sb_customer;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.bootcamp.sb.demo_sb_customer.entity.CustomerEntity;
import com.bootcamp.sb.demo_sb_customer.repository.CustomerRepository;

@DataJpaTest // load repository-related beans only
public class CustomerRepositoryTest {
    // Can't assume John and Mary in this case
    @Autowired
    private CustomerRepository customerRepository;

    // ! This Unit Test provided the repository bean, generated by Hibernate, is correct
    // @Test
    void testSaveAndFindAll() {
        List<CustomerEntity> beforeSave = customerRepository.findAll();
        assertEquals(0, beforeSave.size());

        CustomerEntity john = CustomerEntity.builder()
            .email("john@gmail.com")
            .name("john")
            .build();
            // id is auto-generated
        CustomerEntity customerEntity = customerRepository.save(john);
        
        assertEquals("john", customerEntity.getName());
        assertEquals("john@gmail.com", customerEntity.getEmail());
        assertEquals("1L", customerEntity.getId());

        List<CustomerEntity> afterSaveJohn = customerRepository.findAll();
        assertEquals(1, afterSaveJohn.size());

        CustomerEntity mary = CustomerEntity.builder()
        .email("mary@gmail.com")
        .name("mary")
        .build();

        CustomerEntity customerEntity2 = customerRepository.save(mary);
        assertEquals("mary", customerEntity2.getName());
        assertEquals("mary@gmail.com", customerEntity2.getEmail());
        assertEquals("2L", customerEntity2.getId());
        // test if id of mary is 2
        // also test findAll() in case John is deleted when Mary is saved.

        List<CustomerEntity> afterSaveMary = customerRepository.findAll();
        assertEquals(2, afterSaveMary.size());

    }

}
