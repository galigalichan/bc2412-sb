package com.bootcamp.sb.demo_sb_customer.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

// @Autowired(required=false)
// CommandLineRunner commandLineRunner;
// instance method -> commandLineRunner.run();

// Aspect of Programming (AOP)

// make sure it only runs if there is no exception

@Component // bean
public class PreServerStartConfig implements CommandLineRunner {

    @Override
    public void run(String... args) throws Exception {
    // System.out.println("Hello!!!!!");
    // int x = 3;
    // if (x < 4) {
    //     throw new Exception();
    // }

    // call JPH users api
    // call JPH post api
    // call JPH comment api
    // save DB
    
    }
}
