package it.polimi.tiw.wt_music_playlist_2025.entity;

public record User(
        int id,
        String username,
        String password,
        String name,
        String surname
) implements Entity {

}
