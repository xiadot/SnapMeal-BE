package snapmeal.snapmeal.domain;


import jakarta.persistence.*;
import lombok.*;
import snapmeal.snapmeal.domain.common.BaseEntity;

import java.time.LocalDate;

@Entity
@Table(name = "weekly_reports")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WeeklyReports extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reportId;

    @Column(name = "report_date")
    private LocalDate reportDate;

    @Column(name = "total_calories")
    private Float totalCalories;

    @Column(name = "total_protein")
    private Float totalProtein;

    @Column(name = "total_fat")
    private Float totalFat;

    @Column(name = "total_carbs")
    private Float totalCarbs;

    @Column(name = "recommended_exercise")
    private String recommendedExercise;

    @Column(name = "food_suggestion")
    private String foodSuggestion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id2")
    private User user;
}
