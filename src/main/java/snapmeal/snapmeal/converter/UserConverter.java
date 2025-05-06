package snapmeal.snapmeal.converter;

import org.springframework.security.crypto.password.PasswordEncoder;
import snapmeal.snapmeal.domain.User;
import snapmeal.snapmeal.domain.enums.Role;
import snapmeal.snapmeal.web.dto.UserRequestDto;
import snapmeal.snapmeal.web.dto.UserResponseDto;

public class UserConverter {
    public static User toUser(UserRequestDto.JoinDto dto, PasswordEncoder passwordEncoder) {
        return User.builder()
                .username(dto.getName())
                .age(dto.getAge())
                .gender(dto.getGender())
                .nickname(dto.getNickname())
                .userId(dto.getUserId())
                .password(passwordEncoder.encode(dto.getPassword()))
                .email(dto.getEmail())
                .type(dto.getType())
                .role(Role.USER)
                .build();
    }

    public static UserResponseDto.UserDto toUserSignUpResponseDto(User user) {
        return UserResponseDto.UserDto.builder()
                .name(user.getUsername())
                .age(user.getAge())
                .gender(user.getGender())
                .email(user.getEmail())
                .nickname(user.getNickname())
                .type(user.getType())
                .build();
    }
}
