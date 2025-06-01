package snapmeal.snapmeal.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import snapmeal.snapmeal.domain.Meals;

public interface MealsRepository extends JpaRepository<Meals, Long> {
}