package it.polimi.tiw.wt_music_playlist_2025.form;

import it.polimi.tiw.wt_music_playlist_2025.entity.Genre;
import it.polimi.tiw.wt_music_playlist_2025.entity.Track;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

public class TrackForm {
//    private String file;
//    private String image;
    private MultipartFile file;
    private MultipartFile image;
    private String title;
    private String author;
    private String albumTitle;
    private int albumPublicationYear;
    private Genre genre;

    public Track toTrack(String folderMediaPath, int userId) {
        Track track = new Track();
        track.setTitle(title);
        track.setAuthor(author);
        track.setAlbumTitle(albumTitle);
        track.setAlbumPublicationYear(albumPublicationYear);
        track.setGenre(genre.toString());
        track.setLoaderId(userId);
        track.setImagePath(folderMediaPath + userId + File.separator + image.getName());
        track.setFilePath(folderMediaPath + userId + File.separator + file.getName());
//        track.setImagePath(folderMediaPath + userId + File.separator + image);
//        track.setFilePath(folderMediaPath + userId + File.separator + file);
        return track;
    }

//    public String getFile() {
//        return file;
//    }
//
//    public void setFile(String file) {
//        this.file = file;
//    }
//
//    public String getImage() {
//        return image;
//    }
//
//    public void setImage(String image) {
//        this.image = image;
//    }

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
