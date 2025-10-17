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
@RequestMapping("/subscribe")
public class SubscribeController {

    public SubscribeController(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    private final UserDAO userDAO;

    @GetMapping("/view")
    public String showPage(Model model) {
        model.addAttribute("userForm", new UserForm());
        return "subscribe";
    }

    @PostMapping("/subscribe")
    public String subscribe(UserForm userForm, HttpSession session) {
        User user = userDAO.save(userForm.toUser());
        SessionService.setUser(session, user);
        return "redirect:/home/view";
    }


}
