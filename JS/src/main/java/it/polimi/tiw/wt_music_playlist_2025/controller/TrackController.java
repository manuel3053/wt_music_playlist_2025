package it.polimi.tiw.wt_music_playlist_2025.controller;

import it.polimi.tiw.wt_music_playlist_2025.DAO.PlaylistDAO;
import it.polimi.tiw.wt_music_playlist_2025.DAO.TrackDAO;
import it.polimi.tiw.wt_music_playlist_2025.entity.Genre;
import it.polimi.tiw.wt_music_playlist_2025.entity.Playlist;
import it.polimi.tiw.wt_music_playlist_2025.entity.Track;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.List;

@RestController
public class TrackController {

    @Value("${db.mediaPath}")
    private String mediaPath;
    private final TrackDAO trackDAO;
    private final PlaylistDAO playlistDAO;

    public TrackController(TrackDAO trackDAO, PlaylistDAO playlistDAO) {
        this.trackDAO = trackDAO;
        this.playlistDAO = playlistDAO;
    }


    @GetMapping("/get_track_by_id/{id}")
    public Track getTrackById(@PathVariable("id") int id) {
        return trackDAO.findTrackByIdAndLoaderId(id, UserDetailsExtractor.getUserId());
    }

    @GetMapping("/get_tracks")
    public List<Track> getTracks() {
        return trackDAO.getAllByUserIdSorted(UserDetailsExtractor.getUserId());
    }

    @GetMapping("/get_all_not_in_playlist/{id}")
    public List<Track> getAllNotInPlaylist(@PathVariable("id") int id) {
        return trackDAO.getAllNotInPlaylist(UserDetailsExtractor.getUserId(), id);
    }

    @GetMapping("/get_all_in_playlist/{id}")
    public List<Track> getAllInPlaylist(@PathVariable("id") int id) {
        Playlist playlist = playlistDAO.findByAuthorIdAndId(UserDetailsExtractor.getUserId(), id);
        if (playlist == null) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }
        if (playlist.getCustomOrder()) {
            return trackDAO.getAllInPlaylistCustom(UserDetailsExtractor.getUserId(), id);
        } else {
            return trackDAO.getAllInPlaylist(UserDetailsExtractor.getUserId(), id);
        }
    }

    @GetMapping("/get_genres")
    public List<String> getGenres() {
        return Arrays.stream(Genre.values()).map(Enum::toString).toList();
    }

    private void saveFile(MultipartFile file, String path, String type) throws IOException, InvalidFileType, NullPointerException {
        if (!file.getContentType().startsWith(type)) {
            throw new InvalidFileType("type");
        }
        Path p = Paths.get(mediaPath + File.separator + path);
        p.getParent().toFile().mkdirs();
        Files.copy(file.getInputStream(), p, StandardCopyOption.REPLACE_EXISTING);
    }

    private String getPath(int userId, String albumTitle, MultipartFile file) {
        return UserDetailsExtractor.getUserId() + File.separator + albumTitle + File.separator + file.getOriginalFilename();
    }

    @PostMapping("/add_track")
    public void addTrack(
            @RequestParam("file") MultipartFile file,
            @RequestParam("image") MultipartFile image,
            @RequestParam("title") String title,
            @RequestParam("author") String author,
            @RequestParam("album_title") String albumTitle,
            @RequestParam("album_publication_year") Integer albumPublicationYear,
            @RequestParam("genre") Genre genre
    ) {
        int userId = UserDetailsExtractor.getUserId();
        Track track = new Track();
        track.setTitle(title);
        track.setAuthor(author);
        track.setAlbumTitle(albumTitle);
        track.setAlbumPublicationYear(albumPublicationYear);
        track.setGenre(genre.toString());
        track.setLoaderId(userId);
        track.setFilePath(getPath(userId, albumTitle, file));
        track.setImagePath(getPath(userId, albumTitle, image));
        try {
            saveFile(file, getPath(userId, albumTitle, file), "audio");
            saveFile(image, getPath(userId, albumTitle, image), "image");
            trackDAO.save(track);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
