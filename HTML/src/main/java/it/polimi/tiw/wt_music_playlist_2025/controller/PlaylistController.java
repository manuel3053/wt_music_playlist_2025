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

//    @GetMapping("/view/{id}")
    @GetMapping("/view/{user_id}/{playlist_id}")
    public String showPage(Model model, HttpSession session, @PathVariable("playlist_id") String playlistId) {
        System.out.println(playlistId);
//            model.addAttribute("tracks", trackDAO.getAllByPlaylistId(Integer.parseInt(playlistId)));
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

    @PostMapping("/scroll_playlist")
    public String scrollPlaylist() {
        // se l'elenco dei brani è già presente in locale non serve il redirect
        // altrimenti basta tenerlo e aggiornare la lista di brani visibili
        return Route.PLAYLIST.reload();
    }

    @GetMapping("/select_track")
    public String selectTrack(HttpSession session, int selectedTrackId) {
        SessionService.setSelectedTrackId(session, selectedTrackId);
        return Route.TRACK.go();
    }

}
