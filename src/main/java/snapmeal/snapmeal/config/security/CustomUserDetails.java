package snapmeal.snapmeal.config.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import snapmeal.snapmeal.domain.User;

import java.util.Collection;
import java.util.Collections;

public class CustomUserDetails implements UserDetails {

    private final User user;

    public CustomUserDetails(User user) {
        this.user = user;
    }

    // User 엔티티 꺼내는 메서드
    public User getUser() {
        return user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.emptyList();  // 권한 없으면 빈 리스트 리턴
    }

    @Override
    public String getPassword() {
        return user.getPassword();  // User 엔티티에서 비밀번호
    }

    @Override
    public String getUsername() {
        return user.getUsername();  // User 엔티티에서 username
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
