package snapmeal.snapmeal.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.w3c.dom.Text;
import snapmeal.snapmeal.domain.common.BaseEntity;
import snapmeal.snapmeal.domain.enums.MealType;
@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Meals extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long mealId;

    @Lob
    @Column(name = "image_url")
    private String imageUrl;


    @Enumerated(EnumType.STRING)
    @Column(name = "meal_type")
    private MealType mealType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;



}
