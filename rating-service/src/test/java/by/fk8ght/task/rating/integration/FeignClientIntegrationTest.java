package by.fk8ght.task.rating.integration;

import by.fk8ght.task.rating.client.TripClient;
import by.fk8ght.task.rating.model.dto.TripDTO;
import by.fk8ght.task.rating.model.dto.TripStatus;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.openfeign.EnableFeignClients;

import java.math.BigDecimal;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = {TripClient.class})
@EnableFeignClients(basePackages = "by.fk8ght.task.rating.client")
@WireMockTest(httpPort = 8089)
class FeignClientIntegrationTest {

    @Autowired
    private TripClient tripClient;

    @Test
    void testGetTripSuccess() {
        stubFor(get(urlEqualTo("/api/trip/1"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody("""
                                {
                                    "id": 1,
                                    "driverId": 10,
                                    "passengerId": 20,
                                    "pickupAddress": "Address 1",
                                    "destinationAddress": "Address 2",
                                    "status": "ACCEPTED",
                                    "cost": 10.00
                                }
                                """)));

        TripDTO trip = tripClient.getTrip(1L);

        assertThat(trip).isNotNull();
        assertThat(trip.getId()).isEqualTo(1L);
        assertThat(trip.getDriverId()).isEqualTo(10L);
        assertThat(trip.getPassengerId()).isEqualTo(20L);
        assertThat(trip.getStatus()).isEqualTo(TripStatus.ACCEPTED);
    }

    @Test
    void testGetTripNotFound() {
        stubFor(get(urlEqualTo("/api/trip/999"))
                .willReturn(aResponse()
                        .withStatus(404)));

        try {
            tripClient.getTrip(999L);
        } catch (Exception e) {
            assertThat(e).isInstanceOf(feign.FeignException.NotFound.class);
        }
    }
}
