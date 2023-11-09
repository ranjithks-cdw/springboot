package cdw.springboot.gatekeeper.configs;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final AuthenticationProvider authenticationProvider;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(c -> {
            c.requestMatchers("/register").permitAll()
                    .requestMatchers("/login").permitAll()
                    .requestMatchers("/passkey").permitAll()
                    .requestMatchers("/signout").hasAnyAuthority("admin", "resident", "gatekeeper")
                    .requestMatchers("/users/**").hasAuthority("admin")
                    .requestMatchers("/registration-requests/**").hasAuthority("admin")
                    .requestMatchers(HttpMethod.GET,"/visit-requests/**").hasAnyAuthority("resident", "gatekeeper")
                    .requestMatchers(HttpMethod.POST, "/visit-requests").hasAuthority("resident")
                    .requestMatchers(HttpMethod.PUT, "/visit-requests/**").hasAuthority("resident")
                    .requestMatchers(HttpMethod.DELETE, "/visit-requests/**").hasAuthority("resident")
                    .requestMatchers(HttpMethod.PATCH, "/visit-requests/**").hasAuthority("gatekeeper")
                    .requestMatchers("/blacklist").hasAnyAuthority("admin", "resident", "gatekeeper")
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
