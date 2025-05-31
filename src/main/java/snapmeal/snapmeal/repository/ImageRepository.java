package snapmeal.snapmeal.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import snapmeal.snapmeal.domain.Images;

public interface ImageRepository extends JpaRepository<Images, Long> {
}
