package de.neuefische.neuefischejwttokendemo;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CarService {
    private final CarRepository carRepository;

    public Car create(Car car) {
        return carRepository.save(car);
    }

    public List<Car> getAll() {
        return carRepository.findAll();
    }
}
