package it.polimi.tiw.wt_music_playlist_2025.adapter;

import it.polimi.tiw.wt_music_playlist_2025.entity.Genre;
import it.polimi.tiw.wt_music_playlist_2025.entity.Track;

import java.io.File;

public class TrackForm {
    private String file;
    private String image;
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
        track.setImagePath(folderMediaPath + userId + File.separator + image);
        track.setFilePath(folderMediaPath + userId + File.separator + file);
        return track;
    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

//    public Part getFile() {
//        return file;
//    }
//
//    public void setFile(Part file) {
//        this.file = file;
//    }
//
//    public Part getImage() {
//        return image;
//    }
//
//    public void setImage(Part image) {
//        this.image = image;
//    }

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
