package it.polimi.tiw.wt_music_playlist_2025.controller;

import it.polimi.tiw.wt_music_playlist_2025.DAO.UserDAO;
import it.polimi.tiw.wt_music_playlist_2025.entity.User;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("/login")
public class LoginController {

    public LoginController(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    private final UserDAO userDAO;

    @GetMapping("/test")
    public User getUserTest() {
        return userDAO.findUserById(2);
    }

//    @PostMapping("/login")
//    public String login(UserForm userForm, HttpSession session) {
//        User user;
//        try {
//            user = userDAO.findUserByUsernameAndPassword(userForm.getUsername(), userForm.getPassword());
//        } catch (RuntimeException e) {
//            return Route.LOGIN.reload();
//        }
//
//        if (user == null) {
//            return Route.LOGIN.reload();
//        } else {
//            SessionService.setUser(session, user);
//            return Route.HOME.go();
//        }
//    }

}
