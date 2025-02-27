package ee.rene.decathlon.controller;

import ee.rene.decathlon.entity.Athlete;
import ee.rene.decathlon.entity.Result;
import ee.rene.decathlon.repository.AthleteRepository;
import ee.rene.decathlon.repository.ResultRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/athletes")
public class AthleteController {

    @Autowired
    private AthleteRepository athleteRepository;
    @Autowired
    private ResultRepository resultRepository;

    // Get all athletes
    @GetMapping
    public List<Athlete> getAllAthletes() {
        return athleteRepository.findAll();
    }

    // Get athlete by ID
    @GetMapping("/{id}")
    public ResponseEntity<Athlete> getAthleteById(@PathVariable Long id) {
        return athleteRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    @GetMapping("/{id}/total-points")
    public ResponseEntity<Integer> getTotalPoints(@PathVariable Long id) {
        List<Result> results = resultRepository.findByAthleteId(id);
        if (results.isEmpty()) {
            return ResponseEntity.notFound().build(); // Return 404 if no results found
        }
        int totalPoints = results.stream()
                .mapToInt(Result::getPoints)
                .sum();
        return ResponseEntity.ok(totalPoints);
    }
    // Add a new athlete
    @PostMapping
    public Athlete addAthlete(@RequestBody Athlete athlete) {
        return athleteRepository.save(athlete);
    }
}