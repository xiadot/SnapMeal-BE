package snapmeal.snapmeal.web.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import snapmeal.snapmeal.global.code.ErrorCode;
import snapmeal.snapmeal.global.swagger.ApiErrorCodeExamples;
import snapmeal.snapmeal.service.KakaoService;
import snapmeal.snapmeal.service.UserCommandService;
import snapmeal.snapmeal.web.dto.KakaoUserInfoResponseDto;
import snapmeal.snapmeal.web.dto.UserRequestDto;
import snapmeal.snapmeal.web.dto.UserResponseDto;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import org.springframework.web.util.UriUtils;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Tag(name = "User Management", description = "유저 관리 API")
public class UserController {

    private final UserCommandService userCommandService;
    private final KakaoService kakaoService;


    @Operation(
            summary = "카카오 로그인 콜백",
            description = "프론트에서 받은 인가 코드(code)를 이용해 카카오 AccessToken을 발급받고, 사용자 정보를 조회하여 회원가입 또는 로그인을 처리합니다."
    )
    @ApiErrorCodeExamples(
            ErrorCode.BAD_REQUEST
    )
    @GetMapping("/oauth/kakao/callback")
    public ResponseEntity<UserResponseDto.LoginDto> kakaoCallback(
            @io.swagger.v3.oas.annotations.Parameter(description = "인가 코드")
            @RequestParam("code") String code) {

        String accessToken = kakaoService.getAccessTokenFromKakao(code);
        KakaoUserInfoResponseDto kakaoUser = kakaoService.getUserInfo(accessToken);
        String email = kakaoUser.getKakaoAccount().getEmail();
        UserResponseDto.LoginDto response = userCommandService.isnewUser(email);


        String jwtToken = response.getTokenServiceResponse().getAccessToken();
        String encodedToken = UriUtils.encode(jwtToken, StandardCharsets.UTF_8);

        URI redirectUri = URI.create("snapmeal://home?token=" + encodedToken);
        return ResponseEntity.status(HttpStatus.FOUND).location(redirectUri).build();
    }
    @PostMapping("/sign-up")
    @Operation(
            summary = "회원가입"
    )
    @ApiErrorCodeExamples(
            ErrorCode.BAD_REQUEST
    )
    public ResponseEntity<UserResponseDto.UserDto> signup(@RequestBody UserRequestDto.JoinDto joinDto) {
        UserResponseDto.UserDto response = userCommandService.joinUser(joinDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/sign-in")
    @Operation(
            summary = "일반 로그인"
    )
    @ApiErrorCodeExamples({
            ErrorCode.USER_NOT_FOUND,
            ErrorCode.INVALID_PASSWORD
    })

    public ResponseEntity<UserResponseDto.LoginDto> singIn(@RequestBody UserRequestDto.SignInRequestDto signInRequestDto) {
        UserResponseDto.LoginDto response = userCommandService.signIn(signInRequestDto);
        return ResponseEntity.ok(response);
    }


}
