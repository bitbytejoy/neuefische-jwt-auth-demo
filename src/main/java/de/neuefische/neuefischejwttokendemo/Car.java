package de.neuefische.neuefischejwttokendemo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Car {
    private String id;
    private String brand;
    private String model;
}
