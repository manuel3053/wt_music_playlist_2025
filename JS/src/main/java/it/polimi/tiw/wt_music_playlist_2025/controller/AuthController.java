package it.polimi.tiw.wt_music_playlist_2025.controller;

import it.polimi.tiw.wt_music_playlist_2025.DAO.UserDAO;
import it.polimi.tiw.wt_music_playlist_2025.entity.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.security.web.csrf.CsrfTokenRepository;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
public class AuthController {

    private final UserDAO userDAO;
    private final AuthenticationManager authenticationManager;
    private final SecurityContextRepository securityContextRepository;

    public AuthController(UserDAO userDAO, AuthenticationManager authenticationManager, SecurityContextRepository securityContextRepository) {
        this.userDAO = userDAO;
        this.authenticationManager = authenticationManager;
        this.securityContextRepository = securityContextRepository;
    }

    @PostMapping("/login")
    public void login(
            @RequestParam("username") String username,
            @RequestParam("password") String password,
            HttpServletRequest request,
            HttpServletResponse response
    ) {
        UsernamePasswordAuthenticationToken token = UsernamePasswordAuthenticationToken.unauthenticated(username, password);
        Authentication authentication = authenticationManager.authenticate(token);
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(authentication);
        SecurityContextHolder.setContext(context);
        securityContextRepository.saveContext(context, request, response);
    }

    @PostMapping("/subscribe")
    public void subscribe(
            @RequestParam("username") String username,
            @RequestParam("password") String password,
            @RequestParam("name") String name,
            @RequestParam("surname") String surname
    ) {
        try {
            User user = new User();
            user.setUsername(username);
            user.setPassword(password);
            user.setName(name);
            user.setSurname(surname);
            userDAO.save(user);
        } catch (RuntimeException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
