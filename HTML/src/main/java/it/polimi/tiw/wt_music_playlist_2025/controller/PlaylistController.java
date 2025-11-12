package it.polimi.tiw.wt_music_playlist_2025.controller;

import it.polimi.tiw.wt_music_playlist_2025.DAO.PlaylistTracksDAO;
import it.polimi.tiw.wt_music_playlist_2025.DAO.TrackDAO;
import it.polimi.tiw.wt_music_playlist_2025.entity.Track;
import it.polimi.tiw.wt_music_playlist_2025.form.PlaylistForm;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/playlist")
public class PlaylistController {
    private final TrackDAO trackDAO;
    private final PlaylistTracksDAO playlistTracksDAO;
    private int playlistId;
    private int userId;

    public PlaylistController(TrackDAO trackDAO, PlaylistTracksDAO playlistTracksDAO) {
        this.trackDAO = trackDAO;
        this.playlistTracksDAO = playlistTracksDAO;
    }

    @GetMapping("/view/{user_id}/{playlist_id}/{group}")
    public String showPage(Model model, HttpSession session, @PathVariable("user_id") int userId, @PathVariable("playlist_id") int playlistId, @PathVariable("group") int offset) {
        if (!SessionService.checkValidAccess(session, userId)) {
            return Route.LOGIN.go();
        }
        this.userId = playlistId;
        this.playlistId = playlistId;
        List<Track> tracks = trackDAO.getPlaylistTracksGroup(playlistId, offset * 5);
        model.addAttribute("tracks", tracks);
        model.addAttribute("playlistSize", playlistTracksDAO.getAllByPlaylistId(playlistId).size());
        model.addAttribute("tracksNotInPlaylist", trackDAO.getAllNotInPlaylist(playlistId));
        model.addAttribute("userId", userId);
        model.addAttribute("playlistId", playlistId);
        model.addAttribute("offset", offset);
        model.addAttribute("playlistForm", new PlaylistForm());
        return Route.PLAYLIST.show();
    }

    @PostMapping("/add_track_to_playlist")
    public String addTrackToPlaylist(PlaylistForm playlistForm, HttpSession session) {
        playlistTracksDAO.saveAll(playlistForm.toPlaylistTracks(playlistId));
        return "redirect:view/" + userId + "/" + playlistId + "/0";
    }

}
