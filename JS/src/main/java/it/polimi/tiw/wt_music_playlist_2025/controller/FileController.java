package it.polimi.tiw.wt_music_playlist_2025.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FileController {

    @GetMapping("/file/{user_id}/{playlist_name}/{cover_name}")
    public ResponseEntity<UrlResource> serveSafeFile(@PathVariable("user_id") String userId,
            @PathVariable("playlist_name") String playlistName, @PathVariable("cover_name") String coverName) {
        if (UserDetailsExtractor.getUserId() != Integer.parseInt(userId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        Path baseDir = Paths.get("/home/zarch/Documents/tiw_db/").toAbsolutePath();
        Path requested = baseDir.resolve(userId + File.separator + playlistName + File.separator + coverName)
                .normalize();

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
