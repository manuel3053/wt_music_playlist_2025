package it.polimi.tiw.wt_music_playlist_2025.controller;

import it.polimi.tiw.wt_music_playlist_2025.DAO.PlaylistDAO;
import it.polimi.tiw.wt_music_playlist_2025.DAO.PlaylistTracksDAO;
import it.polimi.tiw.wt_music_playlist_2025.DAO.TrackDAO;
import it.polimi.tiw.wt_music_playlist_2025.entity.Playlist;
import it.polimi.tiw.wt_music_playlist_2025.entity.PlaylistTracks;
import it.polimi.tiw.wt_music_playlist_2025.entity.Track;
import it.polimi.tiw.wt_music_playlist_2025.request.AddPlaylistRequest;
import it.polimi.tiw.wt_music_playlist_2025.request.AddTracksToPlaylistRequest;
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


}
