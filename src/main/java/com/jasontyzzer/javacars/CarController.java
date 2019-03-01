package com.jasontyzzer.javacars;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/cars/")
public class CarController {
    private final CarRepository carRepo;
    private final RabbitTemplate rabbitTemplate;

    public CarController(CarRepository carRepo, RabbitTemplate rabbitTemplate) {
        this.carRepo = carRepo;
        this.rabbitTemplate = rabbitTemplate;
    }

    // id/{id}
    @GetMapping  ("id/{id}")
    public Car findCar(@PathVariable Long id){
        Car car = carRepo.findById(id).orElseThrow();
        return car;
    }

    // year/{year}
    @GetMapping ("year/{year}")
    public List<Car> getCarByYear(@PathVariable String year){
        List<Car> carList = carRepo.findAll();
        List<Car> carYear = new ArrayList<>();

        int yearInt = Integer.parseInt(year);
        for (Car c : carList){
            if(c.getYear() == yearInt){
                carYear.add(c);
            }
        }
        return carYear;
    }

    // brand/{brand}
    @GetMapping ("brand/{brand}")
    public List<Car> getCarByBrand(@PathVariable String brand){
        List<Car> carList = carRepo.findAll();
        List<Car> carBrand= new ArrayList<>();

        for (Car c : carList){
            if(c.getBrand().toLowerCase().equals(brand.toLowerCase())){
                carBrand.add(c);
            }
        }
        CarLog message = new CarLog("Search for " + brand);
        rabbitTemplate.convertAndSend(JavaCarsApplication.QUEUE_NAME, message.toString());
        log.info( "Search for " + brand);
        return carBrand;
    }
    // upload
    @PostMapping ("upload")
    public List<Car> loadCarData(@RequestBody List<Car> newData){
        CarLog message = new CarLog("Data loaded");
        rabbitTemplate.convertAndSend(JavaCarsApplication.QUEUE_NAME, message.toString());
        log.info("Data Loaded");
        return carRepo.saveAll(newData);
    }

    // delete/{id}
    @DeleteMapping ("delete/{id}")
    public Car deleteCar(@PathVariable Long id){
        Car car = carRepo.findById(id).orElseThrow();
        carRepo.delete(car);
        CarLog message = new CarLog(id + "Data Deleted");
        rabbitTemplate.convertAndSend(JavaCarsApplication.QUEUE_NAME, message.toString());
        log.info( id+ "Data Deleted");
        return car;
    }
}
