package snapmeal.snapmeal.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import snapmeal.snapmeal.config.security.JwtTokenProvider;
import snapmeal.snapmeal.converter.UserConverter;
import snapmeal.snapmeal.domain.User;
import snapmeal.snapmeal.domain.enums.Role;
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



    @Override
    @Transactional
    public UserResponseDto.UserDto joinUser(UserRequestDto.JoinDto request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        // 1. 기존 사용자 조회 또는 새로운 사용자 생성
        User user = userRepository.findByEmail(email).orElseGet(() -> saveNewUser(email, request));

        // 2. 기존 사용자일 경우 업데이트
        if (user.getId() != null) {
            updateUserData(user, request);
            userRepository.save(user);

        }

        return UserConverter.toUserSignUpResponseDto(user);
    }

    @Override
    public User saveNewUser(String email, UserRequestDto.JoinDto request) {
        return userRepository.findByEmail(email)
                .orElseGet(() -> {
                    User newUser = User.builder()
                            .email(request.getEmail())
                            .username(request.getName()) // name → username 매핑
                            .age(request.getAge())
                            .gender(request.getGender())
                            .nickname(request.getNickname())
                            .type(request.getType())
                            .role(Role.USER) // 기본 권한 직접 지정
                            .build();


                    return userRepository.save(newUser);
                });
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
                .email(email)// 기본값 설정
                .role(Role.USER)
                .build();

        userRepository.save(newUser);
        TokenServiceResponse token = jwtTokenProvider.createToken(newUser);

        return UserResponseDto.LoginDto.builder()
                .tokenServiceResponse(token)
                .isNewUser(true)  // 신규 유저임을 표시
                .build();
    }


    private void updateUserData(User user, UserRequestDto.JoinDto request) {

        // 값이 존재하는 경우에만 업데이트
        user.updateAll(request);
    }

//
//    @Override
//    public UserResponseDto.Userdto getMyUsers() {
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        String email = authentication.getName();
//
//        User user = userRepository.findByEmail(email)
//                .orElseThrow(() -> new MemberHandler(ErrorStatus.MEMBER_NOT_FOUND));
//
//        if(user.getRole() == Role.UNKNOWN){
//            throw new MemberHandler(ErrorStatus.MEMBER_NOT_FOUND);
//        }
//
//        // User 정보를 UserResponseDto로 변환
//        return UserConverter.toDto(user);
//    }
//
//    @Override
//    public UserResponseDto.Userdto getUsers(Long UserId) {
//        User user = userRepository.findById(UserId)
//                .orElseThrow(() -> new MemberHandler(ErrorStatus.MEMBER_NOT_FOUND));
//
//        if(user.getRole() == Role.UNKNOWN){
//            throw new MemberHandler(ErrorStatus.MEMBER_NOT_FOUND);
//        }
//
//        return UserConverter.toDto(user);
//    }
//
//



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

