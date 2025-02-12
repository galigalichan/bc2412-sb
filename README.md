# Spring Boot

## Basic Concepts / Skills
- 1. Spring Context -> Store Beans
    - Who create Bean? Before server start, "Spring Manager" manages bean cycle.
    - Bean Cycle: @SpringBootApplication -> @ComponentScan, i.e. Find any class contain @Controller, @Service, @Configuration, @Repository
    - "Spring Manager" creates objects for the above classes, put them into Spring Context (Beans)
- 2. Get Beans from Context (Resolve Dependencies between beans)
    - @Autowired on Class Attribute (Field Injection)
        - "Spring Manager" resolves the dependency by finding an appropriate object fit into the attribute type
        - @Autowired on Constructor (Constructor Injection)
- 3. Flow
    - Controller Bean always @Autowired Service Bean
    - Service Bean always @Autowired Repository Bean
    - If "Spring Manager" cannot find any dependency, server start will fail.
- 4. RESTful API (GET/POST/DELETE/PUT/PATCH)
    - GET: Without create, update or delete on resource
    - POST: Create resource ONLY
    - DELETE: Delete resource ONLY (by id, or other resource attribute)
    - PUT: Make sure target resource already exists (Find by id). Then replace the resource by the new resource directly.
    - PATCH: Make sure target resource already exists (Find by id). Then revise the target object attribute, but not replace the object.

## Spring Boot Project Development
- Create Project by VSCode. Add dependencies for your scenario.
- After project creation, restart the VSCode.
- Check pom.xml and application.yml
- If you need add/remove dependency, restart VSCode.
- Create controller folder
    - Insert the folder, create interface (XXXXOperation.java)
    - create impl folder, create implementation class for the interface.
- Create service folder
    - Insert the folder, create interface (XXXXService.java)
    - create impl folder, create implementation class for the interface.
- When Controller @Autowired Service, remember to use service interface, but not implementation class
- test "mvn clean install"

## Week 10/11
Spring Boot:
- RESTful API (Controller)
- Database (Repository + Entity)
- Functions (Service)
- Additional Beans (Configuration)
- Invoke external API (RestTemplate)
- Data Transfer Object (DTO) x Mapper
- Read Custom Variable from yml (@Value)
- GlobalExceptionHandler
- ApiResp.class (SysCode, BusinessException.class)