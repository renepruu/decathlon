package ee.rene.decathlon.controller;

import com.fasterxml.jackson.annotation.JsonFormat;
import ee.rene.decathlon.entity.Athlete;
import ee.rene.decathlon.repository.AthleteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/athletes")
public class AthleteController {
    @Autowired
    private AthleteRepository athleteRepository;


    @GetMapping
    public List<Athlete> getAllAthletes() {
        return athleteRepository.findAll();
    }

    @PostMapping
    public Athlete addAthlete(@RequestBody Athlete athlete) {
        return athleteRepository.save(athlete);
    }
}