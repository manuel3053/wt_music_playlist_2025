package it.polimi.tiw.wt_music_playlist_2025.controller;

import it.polimi.tiw.wt_music_playlist_2025.DAO.PlaylistDAO;
import it.polimi.tiw.wt_music_playlist_2025.DAO.PlaylistTracksDAO;
import it.polimi.tiw.wt_music_playlist_2025.DAO.TrackDAO;
import it.polimi.tiw.wt_music_playlist_2025.DAO.UserDAO;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/track")
public class TrackController {

    public TrackController(TrackDAO trackDAO) {
        this.trackDAO = trackDAO;
    }

    private final TrackDAO trackDAO;

    @GetMapping("/view")
    public String showPage(Model model, HttpSession session) {
        try {
            model.addAttribute("track", trackDAO.findTrackById(SessionService.getSelectedTrackId(session)));
        } catch (MissingSessionAttribute e) {
            return SitePath.PLAYLIST.go();
        }
        return SitePath.TRACK.show();
    }

}
