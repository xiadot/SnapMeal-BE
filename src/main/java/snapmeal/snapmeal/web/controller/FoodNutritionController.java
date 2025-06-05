package snapmeal.snapmeal.web.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import snapmeal.snapmeal.service.FoodNutritionCommandService;
import snapmeal.snapmeal.web.dto.NutritionRequestDto;

@RestController
@RequestMapping("/nutritions")
@RequiredArgsConstructor
public class FoodNutritionController {

    private final FoodNutritionCommandService foodNutritionCommandService;

    @PostMapping("/analyze")
    public ResponseEntity<NutritionRequestDto.TotalNutritionRequestDto> analyzeNutrition(@RequestBody NutritionRequestDto.FoodNutritionRequestDto request) {
        NutritionRequestDto.TotalNutritionRequestDto result = foodNutritionCommandService.analyze(request);
        return ResponseEntity.ok(result);
    }
}
