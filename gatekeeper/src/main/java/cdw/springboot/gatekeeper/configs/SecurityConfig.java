package cdw.springboot.gatekeeper.configs;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static cdw.springboot.gatekeeper.constants.AppConstants.*;

/**
 * Custom security config for the app
 */
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final AuthenticationProvider authenticationProvider;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(c -> {
            c.requestMatchers(ENDPOINT_REGISTER).permitAll()
                    .requestMatchers(ENDPOINT_SIGNIN).permitAll()
                    .requestMatchers(ENDPOINT_SIGNOUT).hasAnyAuthority(ROLE_ADMIN, ROLE_GATEKEEPER, ROLE_RESIDENT)
                    .requestMatchers(ENDPOINT_ADMIN).hasAuthority(ROLE_ADMIN)
                    .requestMatchers(ENDPOINT_RESIDENT).hasAuthority(ROLE_RESIDENT)
                    .requestMatchers(ENDPOINT_GATEKEEPER).hasAuthority(ROLE_GATEKEEPER)
                    .requestMatchers(ENDPOINT_VISITOR).permitAll()
                    .requestMatchers(ENDPOINT_BLACKLIST).hasAnyAuthority( ROLE_RESIDENT, ROLE_GATEKEEPER)
                    .anyRequest().authenticated();
        })
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .csrf(csrf -> csrf.disable())
                .cors(Customizer.withDefaults())
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
}
