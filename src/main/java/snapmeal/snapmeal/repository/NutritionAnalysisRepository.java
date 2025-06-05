package snapmeal.snapmeal.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import snapmeal.snapmeal.domain.NutritionAnalysis;

public interface NutritionAnalysisRepository extends JpaRepository<NutritionAnalysis, Long> {
}
