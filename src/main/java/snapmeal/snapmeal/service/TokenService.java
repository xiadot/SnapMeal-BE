package snapmeal.snapmeal.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import snapmeal.snapmeal.config.security.JwtTokenProvider;
import snapmeal.snapmeal.domain.User;
import snapmeal.snapmeal.repository.RefreshTokenRepository;
import snapmeal.snapmeal.repository.UserRepository;
import snapmeal.snapmeal.web.dto.TokenServiceResponse;
@Service
@RequiredArgsConstructor
public class TokenService {

    private final RefreshTokenRepository refreshTokenRepository;
    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;

    public TokenServiceResponse generateAccessToken(String refreshToken) {

        if (!jwtTokenProvider.validateToken(refreshToken)) {
            refreshTokenRepository.deleteToken(refreshToken);
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid or expired refresh token. Please log in again.");
        }

        if(!refreshTokenRepository.existsByToken(refreshToken)){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "올바르지 않은 토큰입니다.");
        }

        Long userId = refreshTokenRepository.getUserIdByToken(refreshToken);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        refreshTokenRepository.deleteToken(refreshToken);
        return jwtTokenProvider.createToken(user);
    }

}
