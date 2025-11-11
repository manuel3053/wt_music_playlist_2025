package it.polimi.tiw.wt_music_playlist_2025.controller;

import it.polimi.tiw.wt_music_playlist_2025.DAO.PlaylistDAO;
import it.polimi.tiw.wt_music_playlist_2025.DAO.PlaylistTracksDAO;
import it.polimi.tiw.wt_music_playlist_2025.DAO.TrackDAO;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Controller
@RequestMapping("/cover")
public class CoverController {
    private final PlaylistDAO playlistDAO;
    private final TrackDAO trackDAO;
    private final PlaylistTracksDAO playlistTracksDAO;
    private final ResourceLoader resourceLoader;

    public CoverController(ResourceLoader resourceLoader, PlaylistDAO playlistDAO, TrackDAO trackDAO, PlaylistTracksDAO playlistTracksDAO) {
        this.playlistDAO = playlistDAO;
        this.trackDAO = trackDAO;
        this.playlistTracksDAO = playlistTracksDAO;
        this.resourceLoader = resourceLoader;
    }

    @GetMapping("/{user_id}/{album_name}/{file_name}")
    public ResponseEntity<UrlResource> serveSafeFile(HttpServletResponse response, HttpSession session, @PathVariable("user_id") String userId, @PathVariable("album_name") String albumName, @PathVariable("file_name") String fileName) throws IOException {
        if (!SessionService.checkValidAccess(session, Integer.parseInt(userId))) {
            response.sendRedirect(Route.LOGIN.go());
            return ResponseEntity.status(HttpStatus.FOUND).build();
        }
        Path baseDir = Paths.get("/home/zarch/Documents/tiw_db").toAbsolutePath();
        Path requested = baseDir.resolve(userId + File.separator + albumName + File.separator + fileName).normalize();

        if (!Files.exists(requested) || !Files.isRegularFile(requested)) {
            return ResponseEntity.notFound().build();
        }

        try {
            Path realFile = requested.toRealPath();
            if (!realFile.startsWith(baseDir)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }

            UrlResource file = new UrlResource(realFile.toUri());
            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .body(file);

        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

}
