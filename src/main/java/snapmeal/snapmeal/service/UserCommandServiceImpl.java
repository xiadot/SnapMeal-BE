package snapmeal.snapmeal.service;

import ch.qos.logback.core.status.ErrorStatus;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.w3c.dom.UserDataHandler;
import snapmeal.snapmeal.config.security.JwtTokenProvider;
import snapmeal.snapmeal.converter.UserConverter;
import snapmeal.snapmeal.domain.User;
import snapmeal.snapmeal.domain.enums.Role;
import snapmeal.snapmeal.global.code.ErrorCode;
import snapmeal.snapmeal.repository.BlacklistRepository;
import snapmeal.snapmeal.repository.RefreshTokenRepository;
import snapmeal.snapmeal.repository.UserRepository;
import snapmeal.snapmeal.web.dto.TokenServiceResponse;
import snapmeal.snapmeal.web.dto.UserRequestDto;
import snapmeal.snapmeal.web.dto.UserResponseDto;
import java.util.Optional;


@Slf4j
@Service
@RequiredArgsConstructor
public class UserCommandServiceImpl implements UserCommandService {

    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final RefreshTokenRepository refreshTokenRepository;
    private final BlacklistRepository blacklistRepository;
    private final PasswordEncoder passwordEncoder;


    @Override
    @Transactional
    public UserResponseDto.UserDto joinUser(UserRequestDto.JoinDto request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName(); // 인증 정보에서 이메일 추출

        User user = userRepository.findByEmail(email)
                .map(existingUser -> {
                    updateUserData(existingUser, request);
                    return existingUser;
                })
                .orElseGet(() -> {
                    User newUser = UserConverter.toUser(request,passwordEncoder);
                    return userRepository.save(newUser);
                });

        return UserConverter.toUserSignUpResponseDto(user);
    }



    @Override
    public UserResponseDto.LoginDto saveNewUser(UserRequestDto.JoinDto request) {


        User user = UserConverter.toUser(request, passwordEncoder);
        userRepository.save(user);

        TokenServiceResponse token = jwtTokenProvider.createToken(user);

        return UserResponseDto.LoginDto.builder()
                .tokenServiceResponse(token)
                .isNewUser(true)
                .build();
    }




    @Override
    public UserResponseDto.LoginDto isnewUser(String email) {
        // 이메일로 기존 유저 조회
        Optional<User> existingUser = userRepository.findByEmail(email);

        if (existingUser.isPresent()) {
            User user = existingUser.get();
            TokenServiceResponse token = jwtTokenProvider.createToken(user);

                return UserResponseDto.LoginDto.builder()
                        .tokenServiceResponse(token)
                        .isNewUser(false)
                        .build();


        }
        // 신규 유저 생성
        User newUser = User.builder()
                .email(email)
                .role(Role.USER)
                .build();

        userRepository.save(newUser);
        TokenServiceResponse token = jwtTokenProvider.createToken(newUser);

        return UserResponseDto.LoginDto.builder()
                .tokenServiceResponse(token)
                .isNewUser(true)  // 신규 유저임을 표시
                .build();
    }
    public UserResponseDto.LoginDto signIn(String userId, String password) {
        Optional<User> existingUser = userRepository.findByUserId(userId);
        if (existingUser.isPresent()) {
            User user = existingUser.get();
            if(!passwordEncoder.matches(password,user.getPassword())){
                throw new UserDataHandler(ErrorCode.INVALID_PASSWORD);
            }
            TokenServiceResponse token = jwtTokenProvider.createToken(user);
            return UserResponseDto.LoginDto.builder()
                    .tokenServiceResponse(token)
                    .isNewUser(false)
                    .build();

        }
    }

    private void updateUserData(User user, UserRequestDto.JoinDto request) {

        // 값이 존재하는 경우에만 업데이트
        user.updateAll(request);
    }



    @Override
    public String logout(String aToken, String refreshToken) {
        String accessToken = aToken.replace("Bearer ", "");
        String email;
        try {
            email = jwtTokenProvider.getEmailFromToken(accessToken);
        } catch (Exception e) {
            throw new IllegalArgumentException("로그아웃 실패: 유효하지 않은 토큰입니다.", e);
        }

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("로그아웃 실패: 사용자를 찾을 수 없습니다."));

        try {
            // refreshToken 삭제 (존재하지 않아도 예외를 던지지 않도록 함)
            refreshTokenRepository.deleteToken(refreshToken);
        } catch (Exception e) {
            throw new RuntimeException("로그아웃 실패: Refresh Token 삭제 중 오류 발생", e);
        }

        long expiration = jwtTokenProvider.getExpiration(accessToken);

        try {
            // 블랙리스트에 추가
            blacklistRepository.addToBlacklist(accessToken, expiration);
        } catch (Exception e) {
            throw new RuntimeException("로그아웃 실패: 토큰 블랙리스트 등록 중 오류 발생", e);
        }

        return "로그아웃이 성공적으로 완료되었습니다.";
    }



}

