package cdw.springboot.gatekeeper.configs;

import cdw.springboot.gatekeeper.entities.Users;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;

public interface JwtService {
    public String generateToken(Users user, Collection<SimpleGrantedAuthority> authorities);
    public String getUserFromJwt();
}
