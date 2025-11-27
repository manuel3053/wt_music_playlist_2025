package it.polimi.tiw.wt_music_playlist_2025.controller;

import it.polimi.tiw.wt_music_playlist_2025.DAO.PlaylistDAO;
import it.polimi.tiw.wt_music_playlist_2025.DAO.PlaylistTracksDAO;
import it.polimi.tiw.wt_music_playlist_2025.DAO.TrackDAO;
import it.polimi.tiw.wt_music_playlist_2025.form.PlaylistForm;
import it.polimi.tiw.wt_music_playlist_2025.form.TrackForm;
import it.polimi.tiw.wt_music_playlist_2025.entity.*;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.util.List;

@Controller
public class HomeController {

    @Value("${db.mediaPath}")
    private String mediaPath;
    private List<Track> tracks;

    public HomeController(PlaylistDAO playlistDAO, TrackDAO trackDAO, PlaylistTracksDAO playlistTracksDAO) {
        this.playlistDAO = playlistDAO;
        this.trackDAO = trackDAO;
        this.playlistTracksDAO = playlistTracksDAO;
    }

    private final PlaylistDAO playlistDAO;
    private final TrackDAO trackDAO;
    private final PlaylistTracksDAO playlistTracksDAO;

    @GetMapping("/home")
    public String showPage(Model model, HttpSession session) {
        int userId = UserDetailsExtractor.getUserId();
        try {
            model.addAttribute("playlists", playlistDAO.findByAuthorIdOrderByCreationDateAsc(userId));
            tracks = trackDAO.getAllByUserIdSorted(userId);
        } catch (RuntimeException e) {
            return Route.LOGIN.go();
        }
        TrackForm trackForm = new TrackForm();
        model.addAttribute("trackForm", trackForm);
        model.addAttribute("playlistForm", new PlaylistForm());
        model.addAttribute("tracks", tracks);
        model.addAttribute("genres", Genre.values());
        model.addAttribute("userId", userId);
        return Route.HOME.show();
    }

    @PostMapping("/home/add_track")
    public String addTrack(TrackForm trackForm) {
        trackForm.prepare(UserDetailsExtractor.getUserId());
        Track track = trackForm.toTrack();
        try {
            saveFile(trackForm.getFile(), trackForm.getMusicPath(), "audio");
            saveFile(trackForm.getImage(), trackForm.getImagePath(), "image");
            trackDAO.save(track);
        } catch (Exception e) {
            return Route.HOME.go();
        }

        return Route.HOME.go();
    }

    private void saveFile(MultipartFile file, String path, String type) throws IOException, InvalidFileType {
        if (!file.getContentType().startsWith(type)) {
            throw new InvalidFileType("type");
        }
        Path p = Paths.get(mediaPath + File.separator + path);
        p.getParent().toFile().mkdirs();
        Files.copy(file.getInputStream(), p, StandardCopyOption.REPLACE_EXISTING);
    }

    @PostMapping("/home/add_playlist")
    public String addPlaylist(PlaylistForm playlistForm) {
        Playlist insertedPlaylist;
        try {
            insertedPlaylist = playlistDAO.save(playlistForm.toPlaylist(UserDetailsExtractor.getUserId(), tracks));
            playlistTracksDAO.saveAll(playlistForm.toPlaylistTracks(insertedPlaylist.getId()));
        } catch (Exception e) {
            return Route.HOME.go();
        }
        return Route.HOME.go();
    }

}
