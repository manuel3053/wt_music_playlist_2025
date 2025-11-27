package it.polimi.tiw.wt_music_playlist_2025.controller;

import it.polimi.tiw.wt_music_playlist_2025.DAO.PlaylistDAO;
import it.polimi.tiw.wt_music_playlist_2025.DAO.PlaylistTracksDAO;
import it.polimi.tiw.wt_music_playlist_2025.DAO.TrackDAO;
import it.polimi.tiw.wt_music_playlist_2025.entity.Playlist;
import it.polimi.tiw.wt_music_playlist_2025.entity.Track;
import it.polimi.tiw.wt_music_playlist_2025.request.*;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/playlist")
public class PlaylistController {
    private final TrackDAO trackDAO;
    private final PlaylistDAO playlistDAO;
    private final PlaylistTracksDAO playlistTracksDAO;

    public PlaylistController(TrackDAO trackDAO, PlaylistTracksDAO playlistTracksDAO, PlaylistDAO playlistDAO) {
        this.trackDAO = trackDAO;
        this.playlistTracksDAO = playlistTracksDAO;
        this.playlistDAO = playlistDAO;
    }

    private void checkTracks(List<Integer> tracks) {
        int userId = UserDetailsExtractor.getUserId();
        for (Integer id : tracks) {
            if (trackDAO.findTrackByIdAndLoaderId(id, userId) == null) {
                throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "You don't own this track");
            }
        }
    }

    @PostMapping(value = "/create_playlist", consumes = "application/json")
    public void createPlaylist(@RequestBody CreatePlaylistRequest createPlaylistRequest) {
        try {
            checkTracks(createPlaylistRequest.getSelectedTracks());
            Playlist insertedPlaylist = playlistDAO.save(createPlaylistRequest.toPlaylist(UserDetailsExtractor.getUserId()));
            playlistTracksDAO.saveAll(createPlaylistRequest.toPlaylistTracks(insertedPlaylist.getId()));
        } catch (RuntimeException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.toString());
        }
    }

    @PostMapping(value = "/add_tracks_to_playlist", consumes = "application/json")
    public void addTracksToPlaylist(@RequestBody AddTracksToPlaylistRequest addTracksToPlaylistRequest) {
        try {
            checkTracks(addTracksToPlaylistRequest.getSelectedTracks());
            playlistTracksDAO.saveAll(addTracksToPlaylistRequest.toPlaylistTracks());
        } catch (RuntimeException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.toString());
        }
    }

    @GetMapping("/get_playlist_size_by_id")
    public Integer getPlaylistSizeById(@RequestParam(name = "id") Integer id) {
        try {
            return playlistTracksDAO.getAllByPlaylistId(id, UserDetailsExtractor.getUserId()).size();
        } catch (RuntimeException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.toString());
        }
    }

}
