package it.polimi.tiw.wt_music_playlist_2025.controller;

import it.polimi.tiw.wt_music_playlist_2025.DAO.UserDAO;
import it.polimi.tiw.wt_music_playlist_2025.entity.User;
import it.polimi.tiw.wt_music_playlist_2025.request.LoginRequest;
import it.polimi.tiw.wt_music_playlist_2025.request.SubscribeRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@CrossOrigin
@RestController
@RequestMapping("/user")
public class UserController {

    public UserController(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    private final UserDAO userDAO;

//    @PostMapping(value = "/login", consumes = "application/json")
//    public User login(HttpSession session, @RequestBody LoginRequest body) {
//        User user = null;
//        try {
//            user = userDAO.findUserByUsernameAndPassword(body.getUsername(), body.getPassword());
//        } catch (RuntimeException e) {
//            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.toString());
//        }
//
//        if (user == null) {
//            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "The user doesn't exists");
//        } else {
//            return user;
//        }
//    }

    @PostMapping(value = "/subscribe", consumes = "application/json")
    public void subscribe(HttpSession session, @RequestBody SubscribeRequest body) {
        User user;
        try {
            user = userDAO.save(body.toUser());
        } catch (RuntimeException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.toString());
        }
    }



}
