package rs.edu.raf.OTCService.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import rs.edu.raf.OTCService.filters.JwtFilter;

@Configuration
@EnableWebSecurity
public class SpringSecurityConfig {

    private final JwtFilter jwtFilter;
    private final PasswordEncoder bCryptPasswordEncoder;

    public SpringSecurityConfig(JwtFilter jwtFilter, PasswordEncoder bCryptPasswordEncoder) {
        this.jwtFilter = jwtFilter;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(username -> null);
        authProvider.setPasswordEncoder(this.bCryptPasswordEncoder);
        return authProvider;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
                .cors(Customizer.withDefaults())
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(
                        requests -> requests
                                .requestMatchers(new AntPathRequestMatcher("/api/otc/**")).permitAll()
                                .requestMatchers(new AntPathRequestMatcher("/api/v1/otcTrade/**")).permitAll()
                                .requestMatchers(new AntPathRequestMatcher("/swagger-ui/**")).permitAll()
                                .requestMatchers(new AntPathRequestMatcher("/v3/api-docs/**")).permitAll()
                                .requestMatchers(new AntPathRequestMatcher("/v3/api-docs/**")).permitAll()
                                .anyRequest().permitAll()
                )
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                .httpBasic(Customizer.withDefaults());

        return http.build();
    }

}