package by.fk8ght.jwtsecurity.security;

import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Getter
public class JwtUserPrincipal implements UserDetails {

    private final Long userId;
    private final String role;
    private final String login;

    public JwtUserPrincipal(Long userId, String role, String login) {
        this.userId = userId;
        this.role = role;
        this.login = login;
    }

    @Override
    public List<GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + role));
    }

    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public String getUsername() {
        return login;
    }
}
