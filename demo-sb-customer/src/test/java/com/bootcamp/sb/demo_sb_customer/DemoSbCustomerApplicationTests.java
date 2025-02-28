package com.bootcamp.sb.demo_sb_customer;

import org.springframework.boot.test.context.SpringBootTest;

// "mvn install" includes "mvn test"
// "mvn compile" -> ensures main code syntax alright
// "mvn test-compile" -> ensures test code syntax alright
// "mvn test" -> execute test -> SpringBootTest -> test once for the target testing env.
@SpringBootTest // simulate the actual Spring Bean Cycle.
class DemoSbCustomerApplicationTests {

	// ! test all dependencies among components (controllers, services, beans, applications, etc.)
	// @Test // Trigger Point
	void contextLoads() {
	}

}
