package rs.edu.raf.IAMService.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import rs.edu.raf.IAMService.filters.JwtFilter;
import rs.edu.raf.IAMService.services.UserService;


@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SpringSecurityConfig {

    private final UserService userService;
    private final JwtFilter jwtFilter;
    private final PasswordEncoder bCryptPasswordEncoder;
    private Environment environment;

    public SpringSecurityConfig(UserService userService, JwtFilter jwtFilter, PasswordEncoder bCryptPasswordEncoder, Environment environment) {
        this.userService = userService;
        this.jwtFilter = jwtFilter;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;

    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(this.userService);
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
                                .requestMatchers(new AntPathRequestMatcher("/api/auth/**")).permitAll()
                                .requestMatchers(new AntPathRequestMatcher("/api/users/public/**")).permitAll()
                                .requestMatchers(new AntPathRequestMatcher("/swagger-ui/**")).permitAll()
                                .requestMatchers(new AntPathRequestMatcher("/v3/api-docs/**")).permitAll()
                                .requestMatchers(new AntPathRequestMatcher("/v3/api-docs/**")).permitAll()
                                .requestMatchers(new AntPathRequestMatcher("/api/permissions/**")).hasAnyRole("ADMIN","EMPLOYEE")
                                .requestMatchers(new AntPathRequestMatcher("/api/roles/**")).hasAnyRole("ADMIN","EMPLOYEE")
                                .anyRequest().authenticated()
                )
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                .httpBasic(Customizer.withDefaults());

        return http.build();
    }

}

