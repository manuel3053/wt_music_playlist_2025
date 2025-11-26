package it.polimi.tiw.wt_music_playlist_2025.controller;

import it.polimi.tiw.wt_music_playlist_2025.DAO.PlaylistTracksDAO;
import it.polimi.tiw.wt_music_playlist_2025.DAO.TrackDAO;
import it.polimi.tiw.wt_music_playlist_2025.entity.Track;
import it.polimi.tiw.wt_music_playlist_2025.request.GetAllTracksNotInPlaylistRequest;
import it.polimi.tiw.wt_music_playlist_2025.request.GetPlaylistTracksGroupRequest;
import it.polimi.tiw.wt_music_playlist_2025.request.PlaylistForm;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/track")
public class TrackController {
    private final TrackDAO trackDAO;
    private final PlaylistTracksDAO playlistTracksDAO;
    private int playlistId;
    private int userId;

    public TrackController(TrackDAO trackDAO, PlaylistTracksDAO playlistTracksDAO) {
        this.trackDAO = trackDAO;
        this.playlistTracksDAO = playlistTracksDAO;
    }

    @GetMapping("/get_all_tracks_sorted")
    public List<Track> getAllTracksSorted() {
        return trackDAO.getAllByUserIdSorted(UserDetailsExtractor.getUserId());
    }

    @GetMapping("/get_playlist_tracks_group")
    public List<Track> addTrackToPlaylist(@RequestParam Integer offset, @RequestParam Integer playlistId) {
        return trackDAO.getPlaylistTracksGroup(playlistId, offset);
    }

    @GetMapping("/get_all_tracks_not_in_playlist")
    public List<Track> addTrackToPlaylist(@RequestParam Integer playlistId) {
        return trackDAO.getAllNotInPlaylist(playlistId);
    }

}
