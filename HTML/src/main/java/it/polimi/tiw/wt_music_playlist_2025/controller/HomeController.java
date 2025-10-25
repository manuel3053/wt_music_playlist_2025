package it.polimi.tiw.wt_music_playlist_2025.controller;

import it.polimi.tiw.wt_music_playlist_2025.DAO.PlaylistDAO;
import it.polimi.tiw.wt_music_playlist_2025.DAO.PlaylistTracksDAO;
import it.polimi.tiw.wt_music_playlist_2025.DAO.TrackDAO;
import it.polimi.tiw.wt_music_playlist_2025.DAO.UserDAO;
import it.polimi.tiw.wt_music_playlist_2025.form.PlaylistForm;
import it.polimi.tiw.wt_music_playlist_2025.form.TrackForm;
import it.polimi.tiw.wt_music_playlist_2025.entity.*;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.Part;
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
import java.util.List;

@Controller
@RequestMapping("/home")
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
    private int userId;

    @GetMapping("/view")
    public String showPage(Model model, HttpSession session) {
        try {
            userId = SessionService.getUser(session).getId();
        } catch (MissingSessionAttribute e) {
            return Route.LOGIN.go();
        }
        model.addAttribute("playlists", playlistDAO.findByAuthorIdOrderByCreationDateAsc(userId));
        TrackForm trackForm = new TrackForm();
        model.addAttribute("trackForm", trackForm);
        model.addAttribute("playlistForm", new PlaylistForm());
        tracks = trackDAO.getAllByUserIdSorted(userId);
        model.addAttribute("tracks", tracks);
        model.addAttribute("genres", Genre.values());
        model.addAttribute("userId", userId);
        return Route.HOME.show();
    }

    @PostMapping("/add_track")
    public String addTrack(TrackForm trackForm) {
        trackForm.init(mediaPath, userId);
        Track track = trackForm.toTrack();
        try {
            saveFile(trackForm.getFile(), trackForm.getMusicPath());
            saveFile(trackForm.getImage(), trackForm.getImagePath());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        trackDAO.save(track);
        return Route.HOME.reload();
    }

    private void saveFile(MultipartFile file, String path) throws IOException {
            Path p = Paths.get(path);
            p.getParent().toFile().mkdirs();
            Files.copy(file.getInputStream(), p, StandardCopyOption.REPLACE_EXISTING);
    }

    @PostMapping("/add_playlist")
    public String addPlaylist(PlaylistForm playlistForm) {
        Playlist insertedPlaylist = playlistDAO.save(playlistForm.toPlaylist(userId, tracks));
        playlistTracksDAO.saveAll(playlistForm.toPlaylistTracks(insertedPlaylist.getId()));
        return Route.HOME.reload();
    }

}
