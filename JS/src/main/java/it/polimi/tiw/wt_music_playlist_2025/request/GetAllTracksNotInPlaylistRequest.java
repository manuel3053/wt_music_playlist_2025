package it.polimi.tiw.wt_music_playlist_2025.request;

public class GetAllTracksNotInPlaylistRequest {
    private int playlistId;

    public int getPlaylistId() {
        return playlistId;
    }

    public void setPlaylistId(int playlistId) {
        this.playlistId = playlistId;
    }
}
