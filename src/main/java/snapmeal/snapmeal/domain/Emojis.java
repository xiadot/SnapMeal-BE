package snapmeal.snapmeal.domain;

import jakarta.persistence.*;
import lombok.*;
import snapmeal.snapmeal.domain.common.BaseEntity;
import snapmeal.snapmeal.domain.enums.EmojiType;

@Entity
@Table(name = "emojis")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Emojis extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long emojiId;

    @Enumerated(EnumType.STRING)
    @Column(name = "emoji_type")
    private EmojiType emojiType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "meal_id")
    private Meals meal;
}
