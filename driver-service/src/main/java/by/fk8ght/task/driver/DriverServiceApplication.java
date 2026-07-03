package by.fk8ght.task.driver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = "by.fk8ght.task")
@EnableJpaRepositories(basePackages = {
        "by.fk8ght.task.driver.repository",
        "by.fk8ght.task.car.repository"
})
@EntityScan(basePackages = {
        "by.fk8ght.task.driver.model.entity",
        "by.fk8ght.task.car.model.entity"
})
public class DriverServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(DriverServiceApplication.class, args);
    }
}
