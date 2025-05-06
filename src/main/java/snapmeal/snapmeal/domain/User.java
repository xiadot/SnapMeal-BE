package snapmeal.snapmeal.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import snapmeal.snapmeal.domain.common.BaseEntity;
import snapmeal.snapmeal.domain.enums.Gender;
import snapmeal.snapmeal.domain.enums.Role;
import snapmeal.snapmeal.web.dto.UserRequestDto;


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

    @Column(nullable = true)
    private String username;

    @Column(nullable = true)
    private String userId;

    @Column(nullable = true, length = 255)
    private String password;


    private Role role;

    @Column(nullable = true, length = 255)
    private String nickname;

    @Column(nullable = true, unique = true)
    private String email;

    @Column(nullable = true, length = 255)
    private Integer age;

    @Column(nullable = true, length = 255)
    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Column(nullable = true, length = 255)
    private String type;

    @Column(nullable = true, length = 255)
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

    public void updateAll(UserRequestDto.JoinDto dto) {
        if (dto.getName() != null) {
            this.username = dto.getName(); // name → username 매핑
        }
        if (dto.getAge() != null) {
            this.age = dto.getAge();
        }
        if (dto.getGender() != null) {
            this.gender = dto.getGender();
        }
        if (dto.getEmail() != null) {
            this.email = dto.getEmail();
        }
        if (dto.getNickname() != null) {
            this.nickname = dto.getNickname();
        }
        if (dto.getType() != null) {
            this.type = dto.getType();
        }
    }


}
