package it.polimi.tiw.wt_music_playlist_2025;

import it.polimi.tiw.wt_music_playlist_2025.DAO.UserDAO;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final UserDAO userDAO;

    public SecurityConfig(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .formLogin(
                        httpForm -> httpForm
                                .loginPage("/login")
                                .permitAll()
                )
                .authorizeHttpRequests(
                        registry -> registry
                                .requestMatchers("/login/**", "/subscribe/**", "/css/**").permitAll()
                                .anyRequest().authenticated()
                );

        return http.build();
    }

}
