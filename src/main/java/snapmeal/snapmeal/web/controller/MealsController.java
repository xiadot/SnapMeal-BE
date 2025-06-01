/*

package snapmeal.snapmeal.web.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import snapmeal.snapmeal.global.ApiResponse;
import snapmeal.snapmeal.global.code.ErrorCode;
import snapmeal.snapmeal.global.swagger.ApiErrorCodeExamples;
import snapmeal.snapmeal.service.MealsService;
import snapmeal.snapmeal.web.dto.MealsRequestDto;
import snapmeal.snapmeal.web.dto.MealsResponseDto;

import java.util.List;

@RestController
@RequestMapping("/meals")
@RequiredArgsConstructor


public class MealController {

    private final MealsService mealService;

    @PostMapping
    @Operation(summary = "식사 기록 생성", description = "사용자가 식사(음식, 시간, 장소, 메모 등)를 기록합니다.")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "201",
            description = "식사 기록 생성 성공",
            content = @Content(schema = @Schema(implementation = ApiResponse.class))
    )
    @ApiErrorCodeExamples({
            ErrorCode.BAD_REQUEST
    })
    public ResponseEntity<ApiResponse<MealsResponseDto>> createMeal(@RequestBody MealsRequestDto requestDto) {
        MealsResponseDto created = MealsService.createMeal(requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.onSuccess(created));
    }

    @GetMapping
    @Operation(summary = "식사 기록 전체 조회", description = "사용자의 모든 식사 기록을 조회합니다.")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "식사 기록 전체 조회 성공",
            content = @Content(schema = @Schema(implementation = ApiResponse.class))
    )
    public ResponseEntity<ApiResponse<List<MealsResponseDto>>> getAllMeals() {
        List<MealsResponseDto> meals = mealService.getAllMeals();
        return ResponseEntity.ok(ApiResponse.onSuccess(meals));
    }

    @GetMapping("/{mealId}")
    @Operation(summary = "단일 식사 기록 조회", description = "특정 식사 기록을 조회합니다.")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "단일 식사 기록 조회 성공",
            content = @Content(schema = @Schema(implementation = ApiResponse.class))
    )
    @ApiErrorCodeExamples({
            ErrorCode.BAD_REQUEST
    })
    public ResponseEntity<ApiResponse<MealsResponseDto>> getMeal(@PathVariable Long mealId) {
        MealsResponseDto meal = mealService.getMeal(mealId);
        return ResponseEntity.ok(ApiResponse.onSuccess(meal));
    }


    @PutMapping("/{mealId}")
    @Operation(summary = "식사 기록 수정", description = "기존 식사 기록을 수정합니다.")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "식사 기록 수정 성공",
            content = @Content(schema = @Schema(implementation = ApiResponse.class))
    )
    @ApiErrorCodeExamples({
            ErrorCode.BAD_REQUEST,
            ErrorCode.UNAUTHORIZED_ACTION
    })
    public ResponseEntity<ApiResponse<MealsResponseDto>> updateMeal(
            @PathVariable Long mealId,
            @RequestBody MealsRequestDto requestDto) {
        MealsResponseDto updated = mealService.updateMeal(mealId, requestDto);
        return ResponseEntity.ok(ApiResponse.onSuccess(updated));
    }


    @DeleteMapping("/{mealId}")
    @Operation(summary = "식사 기록 삭제", description = "식사 기록을 삭제합니다.")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "204",
            description = "식사 기록 삭제 성공"
    )
    @ApiErrorCodeExamples({
            ErrorCode.BAD_REQUEST,
            ErrorCode.UNAUTHORIZED_ACTION
    })
    public ResponseEntity<Void> deleteMeal(@PathVariable Long mealId) {
        mealService.deleteMeal(mealId);
        return ResponseEntity.noContent().build();
    }

}
*/