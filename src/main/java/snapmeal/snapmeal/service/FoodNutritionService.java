package snapmeal.snapmeal.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import snapmeal.snapmeal.converter.NutritionConverter;
import snapmeal.snapmeal.domain.Images;
import snapmeal.snapmeal.domain.NutritionAnalysis;
import snapmeal.snapmeal.domain.User;
import snapmeal.snapmeal.global.OpenAiClient;
import snapmeal.snapmeal.global.code.ErrorCode;
import snapmeal.snapmeal.global.handler.RecommendationHandler;
import snapmeal.snapmeal.global.handler.UserHandler;
import snapmeal.snapmeal.repository.ImageRepository;
import snapmeal.snapmeal.repository.NutritionAnalysisRepository;
import snapmeal.snapmeal.web.dto.NutritionRequestDto;
import snapmeal.snapmeal.global.util.OpenAiConverter;
import snapmeal.snapmeal.global.util.AuthService;
import snapmeal.snapmeal.web.dto.TodayNutritionResponseDto;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class FoodNutritionService {
    private final OpenAiClient openAiClient;
    private final ImageRepository imagesRepository;
    private final AuthService authService;
    private final NutritionAnalysisRepository nutritionAnalysisRepository;
    private static final String CACHE_PREFIX = "todayNutrition:";
    private final RedisTemplate<String, TodayNutritionResponseDto> nutritionRedisTemplate;

    @Transactional
    public NutritionRequestDto.TotalNutritionRequestDto analyze(NutritionRequestDto.FoodNutritionRequestDto request) {
        try {
            String systemPrompt = "당신은 영양 분석 전문가입니다.";
            String userPrompt = OpenAiConverter.getFoodNutritionInfo(request.getFoodNames());
            String response = openAiClient.requestCompletion(systemPrompt, userPrompt);
            System.out.println("🥣 OpenAI 응답: " + response);

            NutritionRequestDto.TotalNutritionRequestDto result = NutritionConverter.fromOpenAiJson(response);

            User currentUser = authService.getCurrentUser();
            Images image = imagesRepository.findById(request.getImageId())
                    .orElseThrow(() -> new IllegalArgumentException("이미지를 찾을 수 없습니다."));

            NutritionAnalysis analysis = NutritionAnalysis.builder()
                    .image(image)
                    .calories(result.getCalories())
                    .protein(result.getProtein())
                    .carbs(result.getCarbs())
                    .sugar(result.getSugar())
                    .fat(result.getFat())
                    .sodium(result.getSodium())
                    .foodNames(String.join(", ", request.getFoodNames()))
                    .user(currentUser)
                    .build();

            NutritionAnalysis saved = nutritionAnalysisRepository.save(analysis);
            result.setNutritionId(saved.getId());
            return result;

        } catch (Exception e) {
            return new NutritionRequestDto.TotalNutritionRequestDto(0, 0.0, 0.0, 0.0, 0.0, 0.0, 0L);
        }
    }
    @Transactional(readOnly = true)
    public TodayNutritionResponseDto getTodaySummary() {
        User user = authService.getCurrentUser();
        if (user == null) {
            throw new UserHandler(ErrorCode.USER_NOT_FOUND);
        }

        String todayKey = CACHE_PREFIX + user.getId() + ":" + LocalDate.now();

        try {
            TodayNutritionResponseDto cached = nutritionRedisTemplate.opsForValue().get(todayKey);
            if (cached != null) {
                log.info("[TodayNutrition] 캐시 HIT → {}", todayKey);
                return cached;
            }

            LocalDate today = LocalDate.now();
            LocalDateTime startOfDay = today.atStartOfDay();
            LocalDateTime endOfDay = today.atTime(LocalTime.MAX);

            List<NutritionAnalysis> analyses =
                    nutritionAnalysisRepository.findAllByUserAndCreatedAtBetween(user, startOfDay, endOfDay);

            if (analyses.isEmpty()) {
                throw new RecommendationHandler(ErrorCode.NUTRITION_DATA_NOT_FOUND);
            }

            int totalCalories   = analyses.stream().mapToInt(NutritionAnalysis::getCalories).sum();
            double totalProtein = analyses.stream().mapToDouble(NutritionAnalysis::getProtein).sum();
            double totalCarbs   = analyses.stream().mapToDouble(NutritionAnalysis::getCarbs).sum();
            double totalSugar   = analyses.stream().mapToDouble(NutritionAnalysis::getSugar).sum();
            double totalFat     = analyses.stream().mapToDouble(NutritionAnalysis::getFat).sum();
            double totalSodium  = analyses.stream().mapToDouble(NutritionAnalysis::getSodium).sum();

            // 권장량 AI 호출
            String prompt = String.format("""
            당신은 영양학 전문가입니다.
            사용자의 성별은 %s, 나이는 %d세입니다.
            오늘 섭취한 값은 다음과 같습니다:
            - 칼로리: %d kcal
            - 단백질: %.1f g
            - 탄수화물: %.1f g
            - 당: %.1f g
            - 지방: %.1f g
            - 나트륨: %.1f g

            이 사용자의 성별/나이에 따른 권장 섭취량을 JSON 형식으로 알려주세요.
            반드시 JSON만 출력하세요. 설명, 문장, 주석은 포함하지 마세요.
            다른 말 없이 { } JSON만 출력하세요.
            {
              "calories": 2200,
              "protein": 120.0,
              "carbs": 300.0,
              "sugar": 50.0,
              "fat": 65.0,
              "sodium": 75.0
            }
           """,
                    user.getGender().name(), user.getAge(),
                    totalCalories, totalProtein, totalCarbs, totalSugar, totalFat, totalSodium
            );

            String aiResponse = openAiClient.requestCompletion("당신은 영양학 전문가입니다.", prompt);

            log.info("[TodayNutrition] AI raw response: {}", aiResponse);

            String cleanResponse = extractJson(aiResponse);
            log.info("[TodayNutrition] Cleaned response: {}", cleanResponse);

            JSONObject json = new JSONObject(cleanResponse);

            int recommendedCalories    = json.optInt("calories", 2000);
            double recommendedProtein  = json.optDouble("protein", 120.0);
            double recommendedCarbs    = json.optDouble("carbs", 300.0);
            double recommendedSugar    = json.optDouble("sugar", 50.0);
            double recommendedFat      = json.optDouble("fat", 65.0);
            double recommendedSodium    = json.optDouble("sodium", 75.0);

            TodayNutritionResponseDto dto = TodayNutritionResponseDto.builder()
                    .date(today)
                    .calories(buildNutrient(totalCalories, recommendedCalories))
                    .protein(buildNutrient(totalProtein, recommendedProtein))
                    .carbs(buildNutrient(totalCarbs, recommendedCarbs))
                    .sugar(buildNutrient(totalSugar, recommendedSugar))
                    .fat(buildNutrient(totalFat, recommendedFat))
                    .sodium(buildNutrient(totalSodium, recommendedSodium))
                    .build();

            nutritionRedisTemplate.opsForValue().set(todayKey, dto, Duration.ofDays(1));
            log.info("[TodayNutrition] 캐시 MISS → 새로 계산 후 저장: {}", todayKey);
            return dto;

        } catch (UserHandler | RecommendationHandler e) {
            throw e;
        } catch (Exception e) {
            log.error("[TodayNutrition] 권장량 생성 실패", e);
            throw new RecommendationHandler(ErrorCode.RECOMMENDATION_GENERATION_FAILED);
        }
    }
    private TodayNutritionResponseDto.NutrientSummary buildNutrient(double consumed, double recommended) {
        double remaining = recommended - consumed;
        String status;

        if (consumed < recommended * 0.8) {
            status = "부족";
        } else if (consumed > recommended * 1.2) {
            status = "과다";
        } else {
            status = "적정";
        }

        return TodayNutritionResponseDto.NutrientSummary.builder()
                .consumed(consumed)
                .recommended(recommended)
                .remaining(Math.max(0, remaining))
                .status(status)
                .build();
    }


    private String extractJson(String response) {
        String trimmed = response.trim();
        if (trimmed.startsWith("```")) {
            return trimmed.replaceAll("```(json)?", "").trim();
        }
        return trimmed;
    }


}
