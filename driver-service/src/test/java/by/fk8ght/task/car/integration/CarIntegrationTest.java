package by.fk8ght.task.car.integration;

import by.fk8ght.task.car.model.dto.CarDTO;
import by.fk8ght.task.driver.model.dto.DriverDTO;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
@ActiveProfiles("test")
class CarIntegrationTest {

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>(
            DockerImageName.parse("postgres:16-alpine")
    );

    @DynamicPropertySource
    static void registerProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

    @Autowired
    TestRestTemplate restTemplate;

    @Test
    @Order(1)
    void createDriverForCar() {
        DriverDTO driverRequest = new DriverDTO();
        driverRequest.setName("John");
        driverRequest.setEmail("john@example.com");
        driverRequest.setPhone("123456789");

        ResponseEntity<Void> created = restTemplate.postForEntity("/api/driver", driverRequest, Void.class);

        assertThat(created.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    }

    @Test
    @Order(2)
    void createCarAndGetById() {
        CarDTO request = new CarDTO();
        request.setColor("Red");
        request.setBrand("Toyota");
        request.setPlateNumber("1234 AB-1");
        request.setDriverId(1L);

        ResponseEntity<Void> created = restTemplate.postForEntity("/api/car", request, Void.class);

        assertThat(created.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    }

    @Test
    void getCarNotFound() {
        ResponseEntity<String> response = restTemplate.getForEntity("/api/car/999", String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }
}
