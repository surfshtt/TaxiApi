package by.fk8ght.task.trip.integration;

import by.fk8ght.task.trip.client.DriverClient;
import by.fk8ght.task.trip.client.PassengerClient;
import by.fk8ght.task.trip.model.dto.DriverDTO;
import by.fk8ght.task.trip.model.dto.PassengerDTO;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.openfeign.EnableFeignClients;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = {DriverClient.class, PassengerClient.class})
@EnableFeignClients(basePackages = "by.fk8ght.task.trip.client")
@WireMockTest(httpPort = 8089)
class FeignClientIntegrationTest {

    @Autowired
    private DriverClient driverClient;

    @Autowired
    private PassengerClient passengerClient;

    @Test
    void testGetDriverSuccess() {
        stubFor(get(urlEqualTo("/api/driver/1"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody("""
                                {
                                    "id": 1,
                                    "name": "John",
                                    "email": "john@example.com",
                                    "phone": "123456789"
                                }
                                """)));

        DriverDTO driver = driverClient.getDriver(1L);

        assertThat(driver).isNotNull();
        assertThat(driver.getId()).isEqualTo(1L);
        assertThat(driver.getName()).isEqualTo("John");
        assertThat(driver.getEmail()).isEqualTo("john@example.com");
    }

    @Test
    void testGetDriverNotFound() {
        stubFor(get(urlEqualTo("/api/driver/999"))
                .willReturn(aResponse()
                        .withStatus(404)));

        try {
            driverClient.getDriver(999L);
        } catch (Exception e) {
            assertThat(e).isInstanceOf(feign.FeignException.NotFound.class);
        }
    }

    @Test
    void testGetPassengerSuccess() {
        stubFor(get(urlEqualTo("/api/passenger/1"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody("""
                                {
                                    "id": 1,
                                    "name": "Jane",
                                    "email": "jane@example.com",
                                    "phone": "987654321"
                                }
                                """)));

        PassengerDTO passenger = passengerClient.getPassenger(1L);

        assertThat(passenger).isNotNull();
        assertThat(passenger.getId()).isEqualTo(1L);
        assertThat(passenger.getName()).isEqualTo("Jane");
        assertThat(passenger.getEmail()).isEqualTo("jane@example.com");
    }

    @Test
    void testGetPassengerNotFound() {
        stubFor(get(urlEqualTo("/api/passenger/999"))
                .willReturn(aResponse()
                        .withStatus(404)));

        try {
            passengerClient.getPassenger(999L);
        } catch (Exception e) {
            assertThat(e).isInstanceOf(feign.FeignException.NotFound.class);
        }
    }
}
