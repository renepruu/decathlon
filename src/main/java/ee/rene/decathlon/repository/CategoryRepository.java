package ee.rene.decathlon.repository;

import ee.rene.decathlon.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}