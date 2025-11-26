package it.polimi.tiw.wt_music_playlist_2025.controller;

import it.polimi.tiw.wt_music_playlist_2025.DAO.PlaylistTracksDAO;
import it.polimi.tiw.wt_music_playlist_2025.DAO.TrackDAO;
import it.polimi.tiw.wt_music_playlist_2025.entity.Track;
import it.polimi.tiw.wt_music_playlist_2025.request.PlaylistForm;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@CrossOrigin
@RestController
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

    @PostMapping
    public Integer

    @GetMapping("/get_all_not_in_playlist")
    public String showPage(Model model, HttpSession session, @PathVariable("user_id") int userId, @PathVariable("playlist_id") int playlistId, @PathVariable("group") int offset) {
        this.userId = playlistId;
        this.playlistId = playlistId;
        List<Track> tracks;
        try {
            tracks = trackDAO.getPlaylistTracksGroup(playlistId, offset * 5);
            model.addAttribute("playlistSize", playlistTracksDAO.getAllByPlaylistId(playlistId).size());
            model.addAttribute("tracksNotInPlaylist", trackDAO.getAllNotInPlaylist(playlistId));
        } catch (RuntimeException e) {
            return Route.HOME.go();
        }
        model.addAttribute("tracks", tracks);
        model.addAttribute("userId", userId);
        model.addAttribute("playlistId", playlistId);
        model.addAttribute("offset", offset);
        model.addAttribute("playlistForm", new PlaylistForm());
        return Route.PLAYLIST.show();
    }

    @PostMapping("/add_track_to_playlist")
    public void addTrackToPlaylist(PlaylistForm playlistForm, HttpSession session) {
        try {
            playlistTracksDAO.saveAll(playlistForm.toPlaylistTracks(playlistId));
        } catch (RuntimeException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.toString());
        }
    }

    @GetMapping("/get_playlist_size_by_id")
    public Integer getPlaylistSizeById(HttpSession session, @RequestParam(name = "id") Integer id) {
        try {
            int size = playlistTracksDAO.getAllByPlaylistId(id).size();
            System.out.println(size);
            return playlistTracksDAO.getAllByPlaylistId(id).size();
        } catch (RuntimeException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.toString());
        }
    }

}
