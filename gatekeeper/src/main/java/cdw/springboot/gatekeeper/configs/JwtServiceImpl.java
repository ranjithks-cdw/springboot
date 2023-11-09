package cdw.springboot.gatekeeper.configs;

import cdw.springboot.gatekeeper.constants.AppConstants;
import cdw.springboot.gatekeeper.entities.Users;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

@Service
public class JwtServiceImpl implements JwtService{

    @Value("${secret.key}")
    private String secretKey;

    /**
     * To generate token
     * @param user
     * @param authorities
     * @return returns Jwt token
     */
    public String generateToken(Users user, Collection<SimpleGrantedAuthority> authorities){
        Algorithm algorithm= Algorithm.HMAC256(secretKey.getBytes());
        return JWT.create()
                .withSubject(user.getEmail())
                .withExpiresAt(new Date(System.currentTimeMillis()+ AppConstants.USER_TOKEN_EXPIRY_TIME))
                .withClaim("roles", authorities.stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()))
                .sign(algorithm);
    }

    /**
     * Extract the remaining time in minutes for JWT token to expire
     * @param token - users token
     * @return - remaining time left
     */
    public long extractRemainingTime(String token) {
        DecodedJWT decodedJWT = JWT.decode(token);
        Date expirationTime = decodedJWT.getExpiresAt();
        Date currentTime = new Date();
        long remainingMillis = expirationTime.getTime() - currentTime.getTime();
        return remainingMillis ;
    }

    /**
     * @return
     */
    @Override
    public String getUserFromJwt() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getName();
    }
}