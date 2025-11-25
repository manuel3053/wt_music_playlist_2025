package it.polimi.tiw.wt_music_playlist_2025;

import it.polimi.tiw.wt_music_playlist_2025.DAO.UserDAO;
import jakarta.servlet.DispatcherType;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.servlet.util.matcher.PathPatternRequestMatcher;
import org.springframework.util.AntPathMatcher;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final UserDAO userDAO;

    public SecurityConfig(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(registry -> registry
                        .dispatcherTypeMatchers(DispatcherType.FORWARD, DispatcherType.ERROR).permitAll()
                        .requestMatchers("/login", "/subscribe", "/css/**").permitAll()
                        .anyRequest().authenticated()
                )
                .formLogin(httpForm -> httpForm
                        .loginPage("/login")
                        .defaultSuccessUrl("/home")
                        .permitAll()
                )
                .logout(logout -> logout.permitAll()
//                        .logoutUrl("/logout")
//                        .logoutSuccessUrl("/login")
                );

        return http.build();
    }

}
