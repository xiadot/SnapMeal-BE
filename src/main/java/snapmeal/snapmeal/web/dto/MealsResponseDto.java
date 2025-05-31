package snapmeal.snapmeal.web.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class MealsResponseDto {
    private Long id;
    private String menu;
    private int calories;
    private int protein;
    private int carbs;
    private int sugar;
    private int fat;
    private int etc;
    private String time;
    private String memo;
    private String location;
    private LocalDateTime createdAt;
}
