package it.polimi.tiw.wt_music_playlist_2025.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {

    @GetMapping("/login")
    public String showPage() {
        return Route.LOGIN.show();
    }

}
