package ee.rene.decathlon.repository;

import ee.rene.decathlon.entity.Result;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ResultRepository extends JpaRepository<Result, Long> {
    List<Result> findByEventType(String eventType);  // Change to match the entity field name
    List<Result> findByName(String name);
}