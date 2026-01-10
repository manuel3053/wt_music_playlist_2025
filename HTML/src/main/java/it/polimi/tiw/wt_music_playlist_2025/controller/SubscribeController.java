package it.polimi.tiw.wt_music_playlist_2025.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import it.polimi.tiw.wt_music_playlist_2025.DAO.UserDAO;
import it.polimi.tiw.wt_music_playlist_2025.form.UserForm;

@Controller
public class SubscribeController {

    private final UserDAO userDAO;

    public SubscribeController(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

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
