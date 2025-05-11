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
        String name;
        Integer age;
        Gender gender;
        String email;
        String userId;
        String password;
        String nickname;
        String type;
    }
    @Data
    public static class SignInRequestDto {
        private String userId;
        private String password;
    }
}
