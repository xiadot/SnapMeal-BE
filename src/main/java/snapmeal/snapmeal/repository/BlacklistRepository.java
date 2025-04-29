package snapmeal.snapmeal.repository;

import org.springframework.stereotype.Repository;
import snapmeal.snapmeal.config.security.JwtTokenProvider;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.concurrent.TimeUnit;
@Repository
public class BlacklistRepository {
    private final StringRedisTemplate redisTemplate;
    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;

    public BlacklistRepository(StringRedisTemplate redisTemplate, UserRepository userRepository, JwtTokenProvider jwtTokenProvider) {
        this.redisTemplate = redisTemplate;
        this.userRepository = userRepository;
        this.jwtTokenProvider = jwtTokenProvider;
    }



    public void addToBlacklist(String accessToken, long expirationTime) {
        String key = "blacklist:" + accessToken;
        redisTemplate.opsForValue().set(key, "blacklisted", expirationTime, TimeUnit.MILLISECONDS);
    }

    // 블랙리스트 확인
    public boolean isBlacklisted(String accessToken) {
        String key = "blacklist:" + accessToken;
        return Boolean.TRUE.equals(redisTemplate.hasKey(key));
    }
}