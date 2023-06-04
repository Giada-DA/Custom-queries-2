package co.develhope.customqueries2.controllers;

import co.develhope.customqueries2.entities.Flight;
import co.develhope.customqueries2.entities.FlightStatus;
import co.develhope.customqueries2.repositories.FlightRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;

@RestController
@RequestMapping("/flights")
public class FlightController {
    @Autowired
    FlightRepository flightRepository;

    @PostMapping("/provision")
    public Collection<Flight> provisionFlights(@RequestParam(required = false)Integer n){
        if(n==null){
            n = 100;
        }

        List<Flight> flightList = new ArrayList<>();
        FlightStatus[] values = FlightStatus.values();
        Random random = new Random();
        for (int i= 0; i<n; i++){
            Flight flight = new Flight();
            flight.setDescription(generateRandomString(random, 10));
            flight.setFromAirport(generateRandomString(random, 5));
            flight.setToAirport(generateRandomString(random, 6));

            FlightStatus randomEnum = values[random.nextInt(values.length)];
            flight.setStatus(randomEnum);

            flightRepository.save(flight);
            flightList.add(flight);
        }
        return flightList;
    }
    private String generateRandomString(Random random, int length){
        return random.ints(97,123)
                .limit(length)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
    }

    @GetMapping
    public Page<Flight> getAllFlights(@RequestParam(defaultValue = "0") int page,
                                      @RequestParam(defaultValue = "10") int size){
        Pageable pageable = PageRequest.of(page, size, Sort.by("fromAirport").ascending());
        return flightRepository.findAll(pageable);
    }

    @GetMapping("/onTime")
    public Page<Flight> getFlightsOnTime(@RequestParam(defaultValue = "0") int page,
                                         @RequestParam(defaultValue = "10") int size){
        Pageable pageable = PageRequest.of(page, size);
        return flightRepository.findByStatus(FlightStatus.ONTIME,pageable);
    }

    @GetMapping("/p1p2")
    public Page<Flight> get2Status(@RequestParam FlightStatus p1,
                                     @RequestParam FlightStatus p2,
                                     @RequestParam(defaultValue = "0") int page,
                                     @RequestParam(defaultValue = "10") int size){
        Pageable pageable = PageRequest.of(page, size);
        return flightRepository.findByStatus(p1, p2, pageable);
    }

}
