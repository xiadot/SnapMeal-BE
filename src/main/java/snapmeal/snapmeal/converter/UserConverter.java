package snapmeal.snapmeal.converter;

import snapmeal.snapmeal.domain.User;
import snapmeal.snapmeal.domain.enums.Role;
import snapmeal.snapmeal.web.dto.UserRequestDto;
import snapmeal.snapmeal.web.dto.UserResponseDto;

public class UserConverter {
    public static User toUser(UserRequestDto.JoinDto user) {
        return User.builder()
                .username(user.getName())
                .age(user.getAge())
                .gender(user.getGender())
                .nickname(user.getNickname())
                .email(user.getEmail())
                .type(user.getType())
                .role(Role.USER) // 기본 권한
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
