package it.polimi.tiw.wt_music_playlist_2025.entity;

import jakarta.persistence.*;

@Entity
public class Track{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private int loaderId;
    private String filePath;
    private String imagePath;
    private String title;
    private String author;
    private String albumTitle;
    private int albumPublicationYear;
    private String genre;
    private int position;

    public void setLoaderId(int loaderId) {
        this.loaderId = loaderId;
    }

    public int getLoaderId() {
        return loaderId;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public int getAlbumPublicationYear() {
        return albumPublicationYear;
    }

    public void setAlbumPublicationYear(int albumPublicationYear) {
        this.albumPublicationYear = albumPublicationYear;
    }

    public String getAlbumTitle() {
        return albumTitle;
    }

    public void setAlbumTitle(String albumTitle) {
        this.albumTitle = albumTitle;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
