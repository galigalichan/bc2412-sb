package com.bootcamp.sb.demo_sb_customer.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CustomerDataBase {
    public static List<Customer> customers = new ArrayList<>();

    public static boolean put(Customer customer) {
        if (!(customers.contains(customer))) {
              customers.add(customer);
              return true;
            }
            return false;
    }

    public static Optional<Customer> find(Long id) {
      for (Customer customer : customers) {
        if (customer.getId() == id)
          return Optional.of(customer);
      }
      return Optional.empty();
    }

    public static Boolean delete(Long id) {
      for (Customer customer : customers) {
        if (customer.getId() == id) {
          customer = null;
          return true;
        }
      }
      return false;
    }
}
