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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
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

    @GetMapping("/view")
    public String showPage(Model model, HttpSession session) {
        // L'utente andrà recuperato dalla sessione, quindi dopo il login andra innanzitutto
        // salvato nella sessione
        model.addAttribute("playlists", playlistDAO.findByAuthorId(1));
        model.addAttribute("trackForm", new TrackForm());
        model.addAttribute("playlistForm", new PlaylistForm());
        tracks = trackDAO.getAllByLoaderId(1);
        model.addAttribute("tracks", tracks);
        // Crea tabella per i generi, non ha senso siano solo builtin nel programma
        model.addAttribute("genres", Genre.values());
        return "home";
    }

    @PostMapping("/add_track")
    public String addTrack(TrackForm trackForm) {
        trackDAO.save(trackForm.toTrack(mediaPath, 1));
        return "redirect:view_home";
    }

    @PostMapping("/add_playlist")
    public String addPlaylist(PlaylistForm playlistForm) {
        Playlist insertedPlaylist = playlistDAO.save(playlistForm.toPlaylist(1, tracks));
//        System.out.println(insertedPlaylist.getId() + " " + insertedPlaylist.getAuthorId() + " " + insertedPlaylist.getTitle() + " ");
//
//        playlistForm.getSelectedTracks().forEach(trackId -> System.out.println(insertedPlaylist.getId() + " " + trackId));
        playlistForm.toPlaylistTracks(insertedPlaylist.getId()).forEach(playlistTracksDAO::save);
        return "redirect:view_home";
    }

    @GetMapping("/select_playlist")
    public String selectPlaylist(HttpSession session, int selectedPlaylistId) {
        SessionService.setSelectedPlaylistId(session, selectedPlaylistId);
        return "forward:view_playlist";
    }

}
