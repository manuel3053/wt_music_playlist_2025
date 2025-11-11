package it.polimi.tiw.wt_music_playlist_2025.controller;

import it.polimi.tiw.wt_music_playlist_2025.DAO.TrackDAO;
import it.polimi.tiw.wt_music_playlist_2025.entity.Track;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/track")
public class TrackController {

    public TrackController(TrackDAO trackDAO) {
        this.trackDAO = trackDAO;
    }

    private final TrackDAO trackDAO;

    @GetMapping("/view/{user_id}/{track_id}")
    public String showPage(Model model, HttpSession session, @PathVariable("user_id") int userId, @PathVariable("track_id") int trackId) {
        if (!SessionService.checkValidAccess(session, userId)) {
            return Route.LOGIN.go();
        }
        model.addAttribute("track", trackDAO.findTrackById(trackId));
        return Route.TRACK.show();
    }

}
