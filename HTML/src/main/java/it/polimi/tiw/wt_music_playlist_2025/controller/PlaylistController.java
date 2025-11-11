package it.polimi.tiw.wt_music_playlist_2025.controller;

import it.polimi.tiw.wt_music_playlist_2025.DAO.PlaylistDAO;
import it.polimi.tiw.wt_music_playlist_2025.DAO.PlaylistTracksDAO;
import it.polimi.tiw.wt_music_playlist_2025.DAO.TrackDAO;
import it.polimi.tiw.wt_music_playlist_2025.entity.Track;
import it.polimi.tiw.wt_music_playlist_2025.form.PlaylistForm;
import jakarta.servlet.http.HttpSession;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Controller
@RequestMapping("/playlist")
public class PlaylistController {
    private final PlaylistDAO playlistDAO;
    private final TrackDAO trackDAO;
    private final PlaylistTracksDAO playlistTracksDAO;
    private final ResourceLoader resourceLoader;
    private int playlistId;
    private int userId;

    public PlaylistController(ResourceLoader resourceLoader, PlaylistDAO playlistDAO, TrackDAO trackDAO, PlaylistTracksDAO playlistTracksDAO) {
        this.playlistDAO = playlistDAO;
        this.trackDAO = trackDAO;
        this.playlistTracksDAO = playlistTracksDAO;
        this.resourceLoader = resourceLoader;
    }

    @GetMapping("/view/{user_id}/{playlist_id}/{group}")
    public String showPage(Model model, HttpSession session, @PathVariable("user_id") int userId, @PathVariable("playlist_id") int playlistId, @PathVariable("group") int offset) {
        if (!SessionService.checkValidAccess(session, userId)) {
            return Route.LOGIN.go();
        }
        this.userId = playlistId;
        this.playlistId = playlistId;
        List<Track> tracks = trackDAO.getPlaylistTracksGroup(playlistId, offset * 5);
        model.addAttribute("tracks", tracks);
        List<Resource> covers = tracks.stream().map((track) -> {
            return resourceLoader.getResource("~/tiw_db/2/dlkfjldkjgl/20251012_11h07m25s_grim.png");
        }).toList();
        model.addAttribute("tracksCovers", covers);
        model.addAttribute("tracksNotInPlaylist", trackDAO.getAllNotInPlaylist(playlistId));
        model.addAttribute("userId", userId);
        model.addAttribute("playlistId", playlistId);
        model.addAttribute("offset", offset);
        model.addAttribute("playlistForm", new PlaylistForm());
        return Route.PLAYLIST.show();
    }

    @GetMapping("/view/cover/{user_id}/{playlist_id}/{group}")
    public Resource getCover(HttpSession session, @PathVariable("user_id") int userId, @PathVariable("playlist_id") int playlistId, @PathVariable("group") int offset) {
        System.out.println("getCover");
        return resourceLoader.getResource("/home/zarch/Documents/tiw_db/2/dlkfjldkjgl/20251012_11h07m25s_grim.png");
    }

    @PostMapping("/add_track_to_playlist")
    public String addPlaylist(PlaylistForm playlistForm, HttpSession session) {
        playlistTracksDAO.saveAll(playlistForm.toPlaylistTracks(playlistId));
        return "redirect:view/" + userId + "/" + playlistId + "/0";
    }

    @GetMapping("/select_track")
    public String selectTrack(HttpSession session, int selectedTrackId) {
        SessionService.setSelectedTrackId(session, selectedTrackId);
        return Route.TRACK.go();
    }

}
