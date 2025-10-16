package it.polimi.tiw.wt_music_playlist_2025.entity;

import jakarta.persistence.*;

@Entity
public class PlaylistTracks {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private int playlistId;
    private int trackId;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getTrackId() {
        return trackId;
    }

    public void setTrackId(int trackId) {
        this.trackId = trackId;
    }

    public int getPlaylistId() {
        return playlistId;
    }

    public void setPlaylistId(int playlistId) {
        this.playlistId = playlistId;
    }

}

