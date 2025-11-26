package it.polimi.tiw.wt_music_playlist_2025.request;

public class GetPlaylistTracksGroupRequest {
    private int offset;
    private int playlistId;

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public int getPlaylistId() {
        return playlistId;
    }

    public void setPlaylistId(int playlistId) {
        this.playlistId = playlistId;
    }
}
