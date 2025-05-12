package snapmeal.snapmeal.web.dto;

import lombok.*;
import snapmeal.snapmeal.domain.enums.Gender;
import snapmeal.snapmeal.domain.enums.Role;

public class UserResponseDto {
    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UserDto{
        String name;
        String userId;
        Integer age;
        Gender gender;
        String email;
        String nickname;
        String type;

    }


    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class LoginDto {
        private TokenServiceResponse tokenServiceResponse;
        private Role role;
        private Boolean isNewUser;
    }
}
