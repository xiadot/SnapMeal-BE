package snapmeal.snapmeal.global.util;

import lombok.AllArgsConstructor;
import org.apache.logging.log4j.Logger;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import snapmeal.snapmeal.domain.User;
import snapmeal.snapmeal.global.code.ErrorCode;
import snapmeal.snapmeal.global.handler.UserHandler;
import snapmeal.snapmeal.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
@Slf4j
@Service
@AllArgsConstructor
public class AuthService {
    private final UserRepository userRepository;

    public User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();


        if (authentication == null || authentication.getName() == null) {
            throw new UserHandler(ErrorCode.AUTHENTICATION_FAILED);
        }

        String email = authentication.getName();

        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UserHandler(ErrorCode.USER_NOT_FOUND));
    }

    public Long getCurrentUserId() {
        return getCurrentUser().getId();
    }

    public String getCurrentUserEmail() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication.getName() == null) {
            throw new UserHandler(ErrorCode.AUTHENTICATION_FAILED);
        }
        return authentication.getName();
    }
}
