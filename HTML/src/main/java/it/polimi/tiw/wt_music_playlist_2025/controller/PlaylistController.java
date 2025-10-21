package it.polimi.tiw.wt_music_playlist_2025.controller;

import it.polimi.tiw.wt_music_playlist_2025.DAO.PlaylistDAO;
import it.polimi.tiw.wt_music_playlist_2025.DAO.PlaylistTracksDAO;
import it.polimi.tiw.wt_music_playlist_2025.DAO.TrackDAO;
import it.polimi.tiw.wt_music_playlist_2025.adapter.PlaylistForm;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/playlist")
public class PlaylistController {
    private final PlaylistDAO playlistDAO;
    private final TrackDAO trackDAO;
    private final PlaylistTracksDAO playlistTracksDAO;

    public PlaylistController(PlaylistDAO playlistDAO, TrackDAO trackDAO, PlaylistTracksDAO playlistTracksDAO) {
        this.playlistDAO = playlistDAO;
        this.trackDAO = trackDAO;
        this.playlistTracksDAO = playlistTracksDAO;
    }

    @GetMapping("/view/{user_id}/{playlist_id}/{group}")
    public String showPage(Model model, HttpSession session, @PathVariable("user_id") int userId, @PathVariable("playlist_id") int playlistId, @PathVariable("group") int offset) {
        try {
            if (!SessionService.checkValidAccess(session, userId)) {
                return Route.LOGIN.go();
            }
        } catch (MissingSessionAttribute e) {
            return Route.LOGIN.go();
        }
        model.addAttribute("tracks", trackDAO.getPlaylistTracksGroup(playlistId, offset * 5));
        model.addAttribute("userId", userId);
        model.addAttribute("playlistId", playlistId);
        model.addAttribute("offset", offset);
        // vedere se abbia senso fare l'operazione con il db o qui in locale (togliere le tracce già nella playlist)
//        model.addAttribute("notAddedTracks", trackDAO.getAllByPlaylistId(selectedPlaylistId));
        return Route.PLAYLIST.show();
    }

    @PostMapping("/add_track_to_playlist")
    public String addPlaylist(PlaylistForm playlistForm, HttpSession session) {
        try {
            playlistForm.toPlaylistTracks(SessionService.getSelectedPlaylistId(session)).forEach(playlistTracksDAO::save);
        } catch (MissingSessionAttribute e) {
            return Route.HOME.go();
        }
        return Route.PLAYLIST.reload();
    }

    @GetMapping("/select_track")
    public String selectTrack(HttpSession session, int selectedTrackId) {
        SessionService.setSelectedTrackId(session, selectedTrackId);
        return Route.TRACK.go();
    }

}
