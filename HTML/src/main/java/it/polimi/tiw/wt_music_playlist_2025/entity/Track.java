package it.polimi.tiw.wt_music_playlist_2025.entity;

public record Track(
        int id,
        String filePath,
        String imagePath,
        String title,
        String author,
        String albumTitle,
        int albumPublicationYear,
        String genre,
        int position
) implements Entity {

}
