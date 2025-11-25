package it.polimi.tiw.wt_music_playlist_2025.controller;

import it.polimi.tiw.wt_music_playlist_2025.DAO.TrackDAO;
import it.polimi.tiw.wt_music_playlist_2025.entity.Track;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class TrackController {

    public TrackController(TrackDAO trackDAO) {
        this.trackDAO = trackDAO;
    }

    private final TrackDAO trackDAO;

    @GetMapping("/track/{track_id}")
    public String showPage(Model model, @PathVariable("track_id") int trackId) {
        try {
            Track track = trackDAO.findTrackByIdAndLoaderId(trackId, UserDetailsExtractor.getUserId());
            if (track == null) {
                return Route.HOME.go();
            }
            model.addAttribute("track", track);
        } catch (RuntimeException e) {
            return Route.HOME.go();
        }
        return Route.TRACK.show();
    }

}
