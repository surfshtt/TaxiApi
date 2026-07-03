package by.fk8ght.jwtsecurity.security;

import java.util.Optional;

public class SecurityUtils {

    public static Optional<JwtUserPrincipal> getCurrentUserPrincipal() {
        return Optional.ofNullable(org.springframework.security.core.context.SecurityContextHolder.getContext().getAuthentication())
                .filter(auth -> auth.getPrincipal() instanceof JwtUserPrincipal)
                .map(auth -> (JwtUserPrincipal) auth.getPrincipal());
    }

    public static Optional<Long> getCurrentUserId() {
        return getCurrentUserPrincipal().map(JwtUserPrincipal::getUserId);
    }
}
