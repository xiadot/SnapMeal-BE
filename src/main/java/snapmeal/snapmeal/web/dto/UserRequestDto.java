package snapmeal.snapmeal.web.dto;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import snapmeal.snapmeal.domain.enums.Gender;

public class UserRequestDto {

    @Getter
    @Setter
    @NoArgsConstructor
    public static class JoinDto {
        private String name;
        private Integer age;
        private Gender gender;
        private String email;
        private String userId;
        private String password;
        private String nickname;
        private String type;
    }
    @Data
    public static class SignInRequestDto {
        private String userId;
        private String password;
    }
}
