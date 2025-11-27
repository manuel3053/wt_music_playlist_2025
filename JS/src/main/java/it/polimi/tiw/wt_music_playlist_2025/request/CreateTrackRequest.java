package it.polimi.tiw.wt_music_playlist_2025.request;

import it.polimi.tiw.wt_music_playlist_2025.entity.Genre;
import it.polimi.tiw.wt_music_playlist_2025.entity.Track;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

public class CreateTrackRequest {
    private MultipartFile file;
    private MultipartFile image;
    private String title;
    private String author;
    private String albumTitle;
    private int albumPublicationYear;
    private Genre genre;
    private int userId;

    public void prepare(int userId) {
        this.userId = userId;
    }

    public Track toTrack() {
        Track track = new Track();
        track.setTitle(title);
        track.setAuthor(author);
        track.setAlbumTitle(albumTitle);
        track.setAlbumPublicationYear(albumPublicationYear);
        track.setGenre(genre.toString());
        track.setLoaderId(userId);
        track.setFilePath(getMusicPath());
        track.setImagePath(getImagePath());
        return track;
    }

    private String getPath(MultipartFile file) {
        return userId + File.separator + albumTitle + File.separator + file.getOriginalFilename();
    }

    public String getMusicPath() {
        return getPath(file);
    }

    public String getImagePath() {
        return getPath(image);
    }

    public MultipartFile getFile() {
        return file;
    }

    public void setFile(MultipartFile file) {
        this.file = file;
    }

    public MultipartFile getImage() {
        return image;
    }

    public void setImage(MultipartFile image) {
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getAlbumTitle() {
        return albumTitle;
    }

    public void setAlbumTitle(String albumTitle) {
        this.albumTitle = albumTitle;
    }

    public int getAlbumPublicationYear() {
        return albumPublicationYear;
    }

    public void setAlbumPublicationYear(int albumPublicationYear) {
        this.albumPublicationYear = albumPublicationYear;
    }

    public Genre getGenre() {
        return genre;
    }

    public void setGenre(Genre genre) {
        this.genre = genre;
    }
}
