package it.polimi.tiw.wt_music_playlist_2025.controller;

import it.polimi.tiw.wt_music_playlist_2025.DAO.PlaylistDAO;
import it.polimi.tiw.wt_music_playlist_2025.DAO.PlaylistTracksDAO;
import it.polimi.tiw.wt_music_playlist_2025.DAO.TrackDAO;
import it.polimi.tiw.wt_music_playlist_2025.DAO.UserDAO;
import it.polimi.tiw.wt_music_playlist_2025.adapter.PlaylistForm;
import it.polimi.tiw.wt_music_playlist_2025.adapter.TrackForm;
import it.polimi.tiw.wt_music_playlist_2025.entity.*;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/home")
public class HomeController {

    @Value("${db.mediaPath}")
    private String mediaPath;
    private List<Track> tracks;

    public HomeController(UserDAO userDAO, PlaylistDAO playlistDAO, TrackDAO trackDAO, PlaylistTracksDAO playlistTracksDAO) {
        this.userDAO = userDAO;
        this.playlistDAO = playlistDAO;
        this.trackDAO = trackDAO;
        this.playlistTracksDAO = playlistTracksDAO;
    }

    private final UserDAO userDAO;
    private final PlaylistDAO playlistDAO;
    private final TrackDAO trackDAO;
    private final PlaylistTracksDAO playlistTracksDAO;
    private int userId;

    @GetMapping("/view")
    public String showPage(Model model, HttpSession session) {
        try {
            userId = SessionService.getUser(session).getId();
        } catch (MissingSessionAttribute e) {
            return Route.LOGIN.go();
        }
        model.addAttribute("playlists", playlistDAO.findByAuthorId(userId));
        model.addAttribute("trackForm", new TrackForm());
        model.addAttribute("playlistForm", new PlaylistForm());
        tracks = trackDAO.getAllByLoaderId(userId);
        model.addAttribute("tracks", tracks);
        model.addAttribute("genres", Genre.values());
        model.addAttribute("userId", userId);
        return Route.HOME.show();
    }

    @PostMapping("/add_track")
    public String addTrack(TrackForm trackForm) {
        trackDAO.save(trackForm.toTrack(mediaPath, userId));
        return Route.HOME.reload();
    }

    @PostMapping("/add_playlist")
    public String addPlaylist(PlaylistForm playlistForm) {
        Playlist insertedPlaylist = playlistDAO.save(playlistForm.toPlaylist(userId, tracks));
        playlistForm.toPlaylistTracks(insertedPlaylist.getId()).forEach(playlistTracksDAO::save);
        return Route.HOME.reload();
    }

}
