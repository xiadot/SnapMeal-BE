package snapmeal.snapmeal.web.dto;

import lombok.Data;

@Data
public class MealsRequestDto {
    private String menu;       // 예: "샐러드"
    private int calories;      // 예: 152
    private int protein;       // 단백질 (g)
    private int carbs;         // 탄수화물 (g)
    private int sugar;         // 당 (g)
    private int fat;           // 지방 (g)
    private int etc;           // 기타 영양소 (g)
    private String time;       // 아침, 점심, 저녁, 간식 등
    private String memo;       // 자유 메모
    private String location;   // 장소 (ex: "집", "카페")
}
