package it.polimi.tiw.wt_music_playlist_2025.entity;

public record Playlist(
        int id,
        String title,
        String creationDate,
        String author,
        boolean customOrder
) implements Entity {

}
