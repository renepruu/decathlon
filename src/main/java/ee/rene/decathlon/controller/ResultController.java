package ee.rene.decathlon.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import ee.rene.decathlon.entity.Result;
import ee.rene.decathlon.repository.ResultRepository;
import ee.rene.decathlon.service.PointsCalculator;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/results")
public class ResultController {

    private final ResultRepository resultRepository;
    private final PointsCalculator pointsCalculator;

    public ResultController(ResultRepository resultRepository, PointsCalculator pointsCalculator) {
        this.resultRepository = resultRepository;
        this.pointsCalculator = pointsCalculator;
    }

    // Get all
    @GetMapping
    public List<Result> getResults() {
        return resultRepository.findAll();
    }

    @PostMapping
    public ResponseEntity<?> addResult(@RequestBody String rawJson) {
        System.out.println("Raw JSON received: " + rawJson); // Debugging

        ObjectMapper objectMapper = new ObjectMapper();
        try {
            Result result = objectMapper.readValue(rawJson, Result.class);
            System.out.println("Mapped Result object: " + result);

            if (result.getEventType() == null || result.getEventType().trim().isEmpty()) {
                return ResponseEntity.badRequest().body("Error: Event type is required");
            }

            int points = pointsCalculator.calculatePoints(result.getEventType(), result.getResult());
            result.setPoints(points);
            return ResponseEntity.status(HttpStatus.CREATED).body(resultRepository.save(result));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("JSON Mapping Error: " + e.getMessage());
        }
    } //näide post: {
            //"name": "Usain Bolt",
          //"event_type": "100m Sprint",
          //"result": 9.58
                    //}


//Error handler
    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleGeneralException(Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Server Error: " + e.getMessage());
        }

    // Delete by ID
    @DeleteMapping("/{id}")
    public List<Result> deleteResult(@PathVariable Long id) {
        resultRepository.deleteById(id);
        return resultRepository.findAll();
    }
}