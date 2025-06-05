package snapmeal.snapmeal.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import snapmeal.snapmeal.domain.Meals;
import snapmeal.snapmeal.repository.MealsRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MealsService {

    private final MealsRepository mealsRepository;

    // 식단 저장
    public Meals createMeal(Meals meal) {
        return mealsRepository.save(meal);
    }

    // 모든 식단 조회
    public List<Meals> getAllMeals() {
        return mealsRepository.findAll();
    }

    // 식단 개별 조회
    public Meals getMeal(Long mealId) {
        return mealsRepository.findById(mealId)
                .orElseThrow(() -> new IllegalArgumentException("해당 식단이 없습니다. id=" + mealId));
    }

    // 식단 수정
//    public void updateMeal(Long mealId) {
    // mealsRepository.update(mealId);
    //}

    // 식단 삭제
    public void deleteMeal(Long mealId) {
        mealsRepository.deleteById(mealId);
    }

}
