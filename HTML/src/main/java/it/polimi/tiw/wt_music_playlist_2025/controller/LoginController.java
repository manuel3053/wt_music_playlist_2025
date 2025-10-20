package it.polimi.tiw.wt_music_playlist_2025.controller;

import it.polimi.tiw.wt_music_playlist_2025.DAO.UserDAO;
import it.polimi.tiw.wt_music_playlist_2025.adapter.UserForm;
import it.polimi.tiw.wt_music_playlist_2025.entity.User;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/login")
public class LoginController {

    public LoginController(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    private final UserDAO userDAO;

    @GetMapping("/view")
    public String showPage(Model model) {
        model.addAttribute("userForm", new UserForm());
        return Route.LOGIN.show();
    }

    @PostMapping("/login")
    public String login(UserForm userForm, HttpSession session) {
        User user = userDAO.findUserByUsernameAndPassword(userForm.getUsername(), userForm.getPassword());
        if (user == null) {
            return Route.LOGIN.reload();
        } else {
            SessionService.setUser(session, user);
            return Route.HOME.go();
        }
    }

}
