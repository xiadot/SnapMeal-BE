package snapmeal.snapmeal.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import snapmeal.snapmeal.domain.common.BaseEntity;
import snapmeal.snapmeal.domain.enums.Gender;


import java.util.List;
@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "`user`")
public class User extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false ,length = 255)
    private String username;

    @Column(nullable = false ,length = 255)
    private String password;
    private String kakaoId;


    private String nickname;

    @Column(nullable = true, unique = true)
    private String email;

    private Integer age;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    private String profile_image;

    @OneToMany(mappedBy = "user")
    private List<Meals> meals;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Emojis> emojis;

    @OneToMany(mappedBy = "requester", cascade = CascadeType.ALL)
    private List<Friends> requestedFriends;

    @OneToMany(mappedBy = "receiver", cascade = CascadeType.ALL)
    private List<Friends> receivedFriends;

    @OneToMany(mappedBy = "sender", cascade = CascadeType.ALL)
    private List<ChatMessages> sentMessages;

    @OneToMany(mappedBy = "receiver", cascade = CascadeType.ALL)
    private List<ChatMessages> receivedMessages;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<UserMissions> userMissions;


    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<MonthlyReports> monthlyReports;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<WeeklyReports> weeklyReports;

}
