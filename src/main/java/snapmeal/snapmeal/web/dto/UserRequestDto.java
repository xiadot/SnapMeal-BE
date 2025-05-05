package snapmeal.snapmeal.web.dto;

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
        String nickname;
        String type;
    }
}
