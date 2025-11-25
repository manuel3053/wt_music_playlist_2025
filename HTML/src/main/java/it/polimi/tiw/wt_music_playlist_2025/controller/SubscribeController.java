package it.polimi.tiw.wt_music_playlist_2025.controller;

import it.polimi.tiw.wt_music_playlist_2025.DAO.UserDAO;
import it.polimi.tiw.wt_music_playlist_2025.form.UserForm;
import it.polimi.tiw.wt_music_playlist_2025.entity.User;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.server.ResponseStatusException;

@Controller
public class SubscribeController {

    public SubscribeController(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    private final UserDAO userDAO;

    @GetMapping("/subscribe")
    public String showPage(Model model) {
        model.addAttribute("userForm", new UserForm());
        return Route.SUBSCRIBE.show();
    }

    @PostMapping("/subscribe/submit")
    public String subscribe(UserForm userForm) {
        try {
            userDAO.save(userForm.toUser());
        } catch (RuntimeException e) {
            return Route.SUBSCRIBE.go();
        }
        return Route.LOGIN.go();
    }


}
