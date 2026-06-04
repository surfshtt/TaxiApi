package by.fk8ght.task.car.service;

import by.fk8ght.task.car.model.dto.CarDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CarService {
    Page<CarDTO> getCars(Pageable pageable);
    CarDTO getCar(long id);
    void save(CarDTO carDTO);
    void update(long id, CarDTO carDTO);
    void delete(long id);
}
