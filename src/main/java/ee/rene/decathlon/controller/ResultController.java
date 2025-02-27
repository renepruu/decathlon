package ee.rene.decathlon.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import ee.rene.decathlon.entity.Athlete;
import ee.rene.decathlon.entity.Result;
import ee.rene.decathlon.repository.AthleteRepository;
import ee.rene.decathlon.repository.ResultRepository;
import ee.rene.decathlon.service.PointsCalculator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/results")
public class ResultController {

    private final ResultRepository resultRepository;
    private final PointsCalculator pointsCalculator;

    @Autowired
    private AthleteRepository athleteRepository;

    public ResultController(ResultRepository resultRepository, PointsCalculator pointsCalculator) {
        this.resultRepository = resultRepository;
        this.pointsCalculator = pointsCalculator;
    }

    // Get all results
    @GetMapping
    public List<Result> getResults() {
        return resultRepository.findAll();
    }

    // Add a result
    @PostMapping
    public ResponseEntity<?> addResult(@RequestBody Result result) {
        System.out.println("Raw JSON received: " + result); // Debugging

        try {
            // Find athlete by ID
            Athlete athlete = athleteRepository.findById(result.getAthlete().getId())
                    .orElseThrow(() -> new RuntimeException("Athlete not found"));

            result.setAthlete(athlete); // Associate the result with the athlete

            int points = pointsCalculator.calculatePoints(result.getEventType(), result.getResult());
            result.setPoints(points);
            return ResponseEntity.status(HttpStatus.CREATED).body(resultRepository.save(result));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    // Get total points for an athlete
    @GetMapping("/athletes/{id}/total-points")
    public ResponseEntity<Integer> getTotalPoints(@PathVariable Long id) {
        // Fetch results for the athlete by ID
        List<Result> results = resultRepository.findByAthleteId(id);

        // Check if results exist for the athlete
        if (results.isEmpty()) {
            return ResponseEntity.notFound().build(); // Return 404 if no results found
        }

        // Calculate total points
        int totalPoints = results.stream()
                .mapToInt(Result::getPoints)
                .sum();

        // Return total points
        return ResponseEntity.ok(totalPoints);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteResult(@PathVariable Long id) {
        try {
            // Check if the result exists
            if (!resultRepository.existsById(id)) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Result not found");
            }

            // Delete the result
            resultRepository.deleteById(id);
            return ResponseEntity.ok("Result deleted successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error: " + e.getMessage());
        }
    }

    // Get all athletes with their total points
}