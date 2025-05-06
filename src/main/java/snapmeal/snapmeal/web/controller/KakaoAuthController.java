package snapmeal.snapmeal.web.controller;

import com.nimbusds.oauth2.sdk.TokenResponse;
import lombok.RequiredArgsConstructor;
import org.hibernate.annotations.Parameter;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import snapmeal.snapmeal.config.security.JwtTokenProvider;
import snapmeal.snapmeal.domain.User;
import snapmeal.snapmeal.service.KakaoService;
import snapmeal.snapmeal.service.UserCommandService;
import snapmeal.snapmeal.web.dto.KakaoUserInfoResponseDto;
import snapmeal.snapmeal.web.dto.UserResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;

@RestController
@RequestMapping("/users/oauth/kakao")
@Tag(name = "카카오 로그인", description = "카카오 소셜 로그인 API")
public class KakaoAuthController {

    private final KakaoService kakaoService;
    private final UserCommandService userCommandService;

    public KakaoAuthController(KakaoService kakaoService, UserCommandService userCommandService) {
        this.kakaoService = kakaoService;
        this.userCommandService = userCommandService;
    }

    @Operation(
            summary = "카카오 로그인 콜백",
            description = "프론트에서 받은 인가 코드(code)를 이용해 카카오 AccessToken을 발급받고, 사용자 정보를 조회하여 회원가입 또는 로그인을 처리합니다."
    )
    @ApiResponse(
            responseCode = "200",
            description = "로그인 성공",
            content = @Content(schema = @Schema(implementation = UserResponseDto.LoginDto.class))
    )
    @GetMapping("/callback")
    public ResponseEntity<UserResponseDto.LoginDto> kakaoCallback(
            @io.swagger.v3.oas.annotations.Parameter(description = "인가 코드")
            @RequestParam("code") String code) {

        String accessToken = kakaoService.getAccessTokenFromKakao(code);
        KakaoUserInfoResponseDto kakaoUser = kakaoService.getUserInfo(accessToken);
        String email = kakaoUser.getKakaoAccount().getEmail();
        UserResponseDto.LoginDto response = userCommandService.isnewUser(email);

        return ResponseEntity.ok(response);
    }
}
