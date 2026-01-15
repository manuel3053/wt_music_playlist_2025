package it.polimi.tiw.wt_music_playlist_2025.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import it.polimi.tiw.wt_music_playlist_2025.DAO.PlaylistTracksDAO;
import it.polimi.tiw.wt_music_playlist_2025.DAO.TrackDAO;
import it.polimi.tiw.wt_music_playlist_2025.entity.Track;
import it.polimi.tiw.wt_music_playlist_2025.form.PlaylistForm;

@Controller
public class PlaylistController {
    private final TrackDAO trackDAO;
    private final PlaylistTracksDAO playlistTracksDAO;
    private int playlistId;
    private int userId;

    public PlaylistController(TrackDAO trackDAO, PlaylistTracksDAO playlistTracksDAO) {
        this.trackDAO = trackDAO;
        this.playlistTracksDAO = playlistTracksDAO;
    }

    @GetMapping("/playlist/{playlist_id}/{group}")
    public String showPage(Model model, @PathVariable("playlist_id") int playlistId,
            @PathVariable("group") int offset) {
        this.userId = UserDetailsExtractor.getUserId();
        this.playlistId = playlistId;
        List<Track> tracks;
        try {
            tracks = trackDAO.getPlaylistTracksGroup(playlistId, offset * 5, userId);
            if (tracks.isEmpty()) {
                return Route.HOME.go();
            }
            model.addAttribute("playlistSize", playlistTracksDAO.getAllByPlaylistId(playlistId, userId).size());
            model.addAttribute("tracksNotInPlaylist", trackDAO.getAllNotInPlaylist(userId, playlistId));
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

    @PostMapping("/playlist/add_track_to_playlist")
    public String addTrackToPlaylist(PlaylistForm playlistForm) {
        List<Track> userTracks = trackDAO.getAllByUserIdSorted(UserDetailsExtractor.getUserId());
        if (!userTracks.stream().map(Track::getId).toList().containsAll(playlistForm.getSelectedTracks())) {
            return "redirect:/playlist/" + playlistId + "/0";
        }
        try {
            playlistTracksDAO.saveAll(playlistForm.toPlaylistTracks(playlistId));
        } catch (RuntimeException e) {
            return "redirect:/playlist/" + playlistId + "/0";
        }
        return "redirect:/playlist/" + playlistId + "/0";
    }

}
