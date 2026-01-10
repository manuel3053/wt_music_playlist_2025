package it.polimi.tiw.wt_music_playlist_2025.controller;

import it.polimi.tiw.wt_music_playlist_2025.DAO.PlaylistDAO;
import it.polimi.tiw.wt_music_playlist_2025.DAO.PlaylistTracksDAO;
import it.polimi.tiw.wt_music_playlist_2025.DAO.TrackDAO;
import it.polimi.tiw.wt_music_playlist_2025.entity.Playlist;
import it.polimi.tiw.wt_music_playlist_2025.entity.PlaylistTracks;
import it.polimi.tiw.wt_music_playlist_2025.entity.Track;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
public class PlaylistController {
    private final TrackDAO trackDAO;
    private final PlaylistDAO playlistDAO;
    private final PlaylistTracksDAO playlistTracksDAO;

    public PlaylistController(TrackDAO trackDAO, PlaylistDAO playlistDAO, PlaylistTracksDAO playlistTracksDAO) {
        this.trackDAO = trackDAO;
        this.playlistDAO = playlistDAO;
        this.playlistTracksDAO = playlistTracksDAO;
    }

    @GetMapping("/get_playlists")
    public List<Playlist> getPlaylists() {
        return playlistDAO.findByAuthorIdOrderByCreationDateAsc(UserDetailsExtractor.getUserId());
    }

    @GetMapping("/get_playlist_size_by_id/{id}")
    public int getPlaylistSizeByid(@PathVariable("id") int id) {
        return playlistTracksDAO.getAllByPlaylistId(id, UserDetailsExtractor.getUserId()).size();
    }

    @PostMapping("/add_playlist")
    public void addPlaylist(
            @RequestParam("title") String title,
            @RequestParam("selected_tracks") List<Integer> selectedTracks
    ) {
        Playlist insertedPlaylist;
        try {
            int userId = UserDetailsExtractor.getUserId();
            Playlist playlist = new Playlist();
            playlist.setTitle(title);
            playlist.setAuthorId(userId);
            playlist.setCustomOrder(false);
            playlist.setCreationDate(new Date(System.currentTimeMillis()));
            insertedPlaylist = playlistDAO.save(playlist);

            List<Integer> userTracks = trackDAO.getAllByUserIdSorted(userId).stream()
                    .map(Track::getId).toList();
            playlistTracksDAO.saveAll(selectedTracks.stream().map((trackId) -> {
                if (userTracks.contains(trackId)) {
                    PlaylistTracks p = new PlaylistTracks();
                    p.setPlaylistId(insertedPlaylist.getId());
                    p.setTrackId(trackId);
                    return p;
                } else {
                    throw new ResponseStatusException(HttpStatus.FORBIDDEN);
                }
            }).toList());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/add_tracks_to_playlist")
    public void addTracksToPlaylist(
            @RequestParam("playlist_id") Integer playlistId,
            @RequestParam("selected_tracks") List<Integer> selectedTracks
    ) {
        int userId = UserDetailsExtractor.getUserId();
        Playlist playlist = playlistDAO.findByAuthorIdAndId(userId, playlistId);
        if (playlist == null) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }

        if (playlist.getCustomOrder()) {
            int lastPosition = trackDAO.getAllInPlaylist(userId, playlistId).size() - 1;
            for (int i = 0; i < selectedTracks.size(); i++) {
                trackDAO.updatePosition(lastPosition + i, selectedTracks.get(i));
            }
        }

        try {
            List<Integer> userTracks = trackDAO.getAllByUserIdSorted(userId).stream()
                    .map(Track::getId).toList();
            playlistTracksDAO.saveAll(selectedTracks.stream().map((trackId) -> {
                if (userTracks.contains(trackId)) {
                    PlaylistTracks p = new PlaylistTracks();
                    p.setPlaylistId(playlistId);
                    p.setTrackId(trackId);
                    return p;
                } else {
                    throw new ResponseStatusException(HttpStatus.FORBIDDEN);
                }
            }).toList());

        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/set_custom_order")
    public void setCustomOrder(
            @RequestParam("playlist_id") Integer playlistId,
            @RequestParam("tracks") List<Integer> tracks
    ) {
        Playlist playlist = playlistDAO.findByAuthorIdAndId(UserDetailsExtractor.getUserId(), playlistId);
        if (playlist == null) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }
        try {
            int userId = UserDetailsExtractor.getUserId();
            List<Integer> userTracks = trackDAO.getAllByUserIdSorted(userId).stream()
                    .map(Track::getId).toList();

            playlistDAO.setCustomOrder(playlistId);
            for (int i = 0; i < tracks.size(); i++) {
                if (userTracks.contains(tracks.get(i))) {
                    trackDAO.updatePosition(i, tracks.get(i));
                } else {
                    throw new ResponseStatusException(HttpStatus.FORBIDDEN);
                }
            }

        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
