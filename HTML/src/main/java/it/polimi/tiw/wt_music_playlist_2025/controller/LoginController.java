package it.polimi.tiw.wt_music_playlist_2025.controller;

import it.polimi.tiw.wt_music_playlist_2025.DAO.PlaylistDAO;
import it.polimi.tiw.wt_music_playlist_2025.DAO.PlaylistTracksDAO;
import it.polimi.tiw.wt_music_playlist_2025.DAO.TrackDAO;
import it.polimi.tiw.wt_music_playlist_2025.DAO.UserDAO;
import it.polimi.tiw.wt_music_playlist_2025.adapter.PlaylistForm;
import it.polimi.tiw.wt_music_playlist_2025.adapter.TrackForm;
import it.polimi.tiw.wt_music_playlist_2025.entity.Genre;
import it.polimi.tiw.wt_music_playlist_2025.adapter.LoginForm;
import it.polimi.tiw.wt_music_playlist_2025.entity.Playlist;
import it.polimi.tiw.wt_music_playlist_2025.entity.Track;
import it.polimi.tiw.wt_music_playlist_2025.entity.User;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/login")
public class LoginController {

    public LoginController(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    private final UserDAO userDAO;

    @GetMapping("/view")
    public String showPage(Model model) {
        model.addAttribute("loginForm", new LoginForm());
        return "login";
    }

    @PostMapping("/login")
    public String login(LoginForm loginForm, HttpSession session) {
        User user = userDAO.findUserByUsernameAndPassword(loginForm.getUsername(), loginForm.getPassword());
        if (user == null) {
            return "redirect:view_login";
        } else {
            SessionService.setUser(session, user);
            return "redirect:view_home";
        }
    }

    @PostMapping("/subscribe")
    public String subscribe(LoginForm loginForm, HttpSession session) {
        User user = userDAO.save(loginForm.toUser());
        SessionService.setUser(session, user);
        return "redirect:view_home";
    }


}
