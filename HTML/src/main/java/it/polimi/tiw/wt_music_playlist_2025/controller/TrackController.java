package it.polimi.tiw.wt_music_playlist_2025.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import it.polimi.tiw.wt_music_playlist_2025.DAO.TrackDAO;
import it.polimi.tiw.wt_music_playlist_2025.entity.Track;

@Controller
public class TrackController {

    private final TrackDAO trackDAO;

    public TrackController(TrackDAO trackDAO) {
        this.trackDAO = trackDAO;
    }

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
