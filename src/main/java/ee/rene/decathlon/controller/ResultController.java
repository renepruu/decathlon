package ee.rene.decathlon.controller;

import com.fasterxml.jackson.annotation.JsonFormat;
import ee.rene.decathlon.entity.Result;
import ee.rene.decathlon.repository.ResultRepository;
import ee.rene.decathlon.service.PointsCalculator;
import org.springframework.beans.factory.annotation.Autowired;
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

    // Get all results
    @GetMapping
    public List<Result> getResults() {
        return resultRepository.findAll();
    }

    // Get results by event type
    @GetMapping("/{eventType}")
    public List<Result> getResultsByEvent(@PathVariable String eventType) {
        return resultRepository.findByEventType(eventType);
    }

    // Add a single result
    @PostMapping
    public Result addResult(@RequestBody Result result) {
        int points = pointsCalculator.calculatePoints(result.getEventType(), result.getResult());
        result.setPoints(points);
        return resultRepository.save(result);
    }

    // Add multiple results
    @PostMapping("/batch")
    public List<Result> addResults(@RequestBody List<Result> results) {
        for (Result result : results) {
            int points = pointsCalculator.calculatePoints(result.getEventType(), result.getResult());
            result.setPoints(points);
        }
        resultRepository.saveAll(results);
        return resultRepository.findAll();
    }

    // Delete a result by ID
    @DeleteMapping("/{id}")
    public List<Result> deleteResult(@PathVariable Long id) {
        resultRepository.deleteById(id);
        return resultRepository.findAll();
    }
}