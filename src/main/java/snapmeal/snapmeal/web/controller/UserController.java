package snapmeal.snapmeal.web.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.user.UserDestinationResolver;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import snapmeal.snapmeal.service.UserCommandService;
import snapmeal.snapmeal.web.dto.UserRequestDto;
import snapmeal.snapmeal.web.dto.UserResponseDto;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final UserCommandService userCommandService;

    @PostMapping("/sign-up")
    public ResponseEntity<UserResponseDto.LoginDto> signup(@RequestBody UserRequestDto.JoinDto joinDto) {
        UserResponseDto.LoginDto response = userCommandService.saveNewUser(joinDto);
        return ResponseEntity.ok(response);
    }


}
