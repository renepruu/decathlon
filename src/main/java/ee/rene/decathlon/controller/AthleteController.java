package ee.rene.decathlon.controller;

import ee.rene.decathlon.entity.Athlete;
import ee.rene.decathlon.repository.AthleteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
public class AthleteController {
    @Autowired
    AthleteRepository athleteRepository;

    @GetMapping("athletes")
    public List<Athlete> getAthletes() {
        return athleteRepository.findAll();
    }
    @PostMapping("athletes")
    public List<Athlete> addAthlete(@RequestBody Athlete athlete) {
        if (athlete.getId() != null) {
            throw new RuntimeException("Id of athlete is already set");
        }
        if (athlete.getAge() <= 16){
            throw new RuntimeException("Age of athlete is too low");
        }
        athleteRepository.save(athlete);
        return athleteRepository.findAll();
    }
    @DeleteMapping("athletes{id}")
    public List<Athlete> deleteAthlete(@PathVariable Long id) {
        athleteRepository.deleteById(id);
        return athleteRepository.findAll();
    }
    @PutMapping("athletes")
    public List<Athlete> editAthlete(@RequestBody Athlete athlete) {
        if (athlete.getId() != null) {
            throw new RuntimeException("Id of athlete is already set");
        }
        if (athlete.getAge() <= 16){
            throw new RuntimeException("Age of athlete is too low");
        }
        athleteRepository.save(athlete);
        return athleteRepository.findAll();
    }
    @GetMapping("athletes{id}")
    public Athlete getAthlete(@PathVariable Long id) {
        return athleteRepository.findById(id).orElseThrow();
    }
    @GetMapping("/category-athletes")
    public Page<Athlete> getCategoryAthletes(@RequestParam Long categoryId, Pageable pageable){
        if (categoryId == -1){
            return athleteRepository.findAll(pageable);
        }
        return athleteRepository.findByCategory_Id(categoryId, pageable);
    }
}