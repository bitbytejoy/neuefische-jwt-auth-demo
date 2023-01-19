package de.neuefische.neuefischejwttokendemo;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cars")
@RequiredArgsConstructor
public class CarController {
    private final CarService carService;

    @PostMapping
    public Car create (@RequestBody Car car) {
        return carService.create(car);
    }

    @GetMapping
    List<Car> getAll () {
        return carService.getAll();
    }
}
