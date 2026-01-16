package it.polimi.tiw.wt_music_playlist_2025.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import it.polimi.tiw.wt_music_playlist_2025.DAO.PlaylistDAO;
import it.polimi.tiw.wt_music_playlist_2025.DAO.PlaylistTracksDAO;
import it.polimi.tiw.wt_music_playlist_2025.DAO.TrackDAO;
import it.polimi.tiw.wt_music_playlist_2025.entity.Playlist;
import it.polimi.tiw.wt_music_playlist_2025.entity.PlaylistTracks;
import it.polimi.tiw.wt_music_playlist_2025.entity.Track;

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
            @RequestParam("selected_tracks") List<Integer> selectedTracks) {
        Playlist insertedPlaylist;
        try {
            int userId = UserDetailsExtractor.getUserId();
            List<Integer> userTracks = trackDAO.getAllByUserIdSorted(userId).stream()
                    .map(Track::getId)
                    .toList();

            if (!userTracks.containsAll(selectedTracks)) {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN);
            }

            Playlist playlist = new Playlist();
            playlist.setTitle(title);
            playlist.setAuthorId(userId);
            playlist.setCustomOrder(false);
            playlist.setCreationDate(new Date(System.currentTimeMillis()));
            insertedPlaylist = playlistDAO.save(playlist);

            playlistTracksDAO.saveAll(selectedTracks.stream().map((trackId) -> {
                PlaylistTracks p = new PlaylistTracks();
                p.setPlaylistId(insertedPlaylist.getId());
                p.setTrackId(trackId);
                return p;
            }).toList());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/add_tracks_to_playlist")
    public void addTracksToPlaylist(
            @RequestParam("playlist_id") Integer playlistId,
            @RequestParam("selected_tracks") List<Integer> selectedTracks) {
        int userId = UserDetailsExtractor.getUserId();
        Playlist playlist = playlistDAO.findByAuthorIdAndId(userId, playlistId);
        if (playlist == null) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }

        List<Integer> userTracks = trackDAO.getAllByUserIdSorted(userId).stream()
                .map(Track::getId)
                .toList();

        if (!userTracks.containsAll(selectedTracks)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }

        List<PlaylistTracks> pt = new ArrayList<>();
        if (playlist.getCustomOrder()) {
            int lastPosition = trackDAO.getAllInPlaylist(userId, playlistId).size() - 1;
            for (int i = 0; i < selectedTracks.size(); i++) {
                PlaylistTracks p = new PlaylistTracks();
                p.setPlaylistId(playlistId);
                p.setTrackId(selectedTracks.get(i));
                p.setPosition(lastPosition + i);
                pt.add(p);
            }
        } else {
            pt.addAll(selectedTracks.stream().map((trackId) -> {
                PlaylistTracks p = new PlaylistTracks();
                p.setPlaylistId(playlistId);
                p.setTrackId(trackId);
                p.setPosition(0);
                return p;
            }).toList());
        }

        try {
            playlistTracksDAO.saveAll(pt);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/set_custom_order")
    public void setCustomOrder(
            @RequestParam("playlist_id") Integer playlistId,
            @RequestParam("tracks") String tracksJson) {
        // Integer playlistId = setCustomOrderDTO.getPlaylistId();
        List<Integer> tracks;
        try {
            tracks = new ObjectMapper().readValue(tracksJson, new TypeReference<List<Integer>>() {
            });
        } catch (JsonProcessingException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        Playlist playlist = playlistDAO.findByAuthorIdAndId(UserDetailsExtractor.getUserId(), playlistId);
        if (playlist == null) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }

        int userId = UserDetailsExtractor.getUserId();
        List<Integer> userTracks = trackDAO.getAllByUserIdSorted(userId).stream()
                .map(Track::getId)
                .toList();

        if (!userTracks.containsAll(tracks)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }

        try {

            playlistDAO.setCustomOrder(playlistId);
            for (int i = 0; i < tracks.size(); i++) {
                playlistTracksDAO.updatePosition(i, tracks.get(i), playlistId);
            }

        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
