package it.polimi.tiw.wt_music_playlist_2025.controller;

import it.polimi.tiw.wt_music_playlist_2025.DAO.PlaylistTracksDAO;
import it.polimi.tiw.wt_music_playlist_2025.DAO.TrackDAO;
import it.polimi.tiw.wt_music_playlist_2025.entity.Playlist;
import it.polimi.tiw.wt_music_playlist_2025.entity.Track;
import it.polimi.tiw.wt_music_playlist_2025.request.CreateTrackRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/track")
public class TrackController {
    private final TrackDAO trackDAO;
    private final PlaylistTracksDAO playlistTracksDAO;

    public TrackController(TrackDAO trackDAO, PlaylistTracksDAO playlistTracksDAO) {
        this.trackDAO = trackDAO;
        this.playlistTracksDAO = playlistTracksDAO;
    }

    @PostMapping(value = "/create_track", consumes = "application/json")
    public void createTrack(@RequestBody CreateTrackRequest createTrackRequest) {
        try {
            trackDAO.save(createTrackRequest.toTrack());
        } catch (RuntimeException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.toString());
        }
    }

    @GetMapping("/get_all_tracks_sorted")
    public List<Track> getAllTracksSorted() {
        try {
            return trackDAO.getAllByUserIdSorted(UserDetailsExtractor.getUserId());
        } catch (RuntimeException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.toString());
        }
    }

    @GetMapping("/get_playlist_tracks_group")
    public List<Track> addTrackToPlaylist(@RequestParam(name = "offset") Integer offset, @RequestParam(name = "playlistId") Integer playlistId) {
        try {
            return trackDAO.getPlaylistTracksGroup(playlistId, offset, UserDetailsExtractor.getUserId());
        } catch (RuntimeException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.toString());
        }
    }

    @GetMapping("/get_all_tracks_not_in_playlist")
    public List<Track> addTrackToPlaylist(@RequestParam(name = "playlistId") Integer playlistId) {
        try {
            return trackDAO.getAllNotInPlaylist(UserDetailsExtractor.getUserId(), playlistId);
        } catch (RuntimeException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.toString());
        }
    }

}
