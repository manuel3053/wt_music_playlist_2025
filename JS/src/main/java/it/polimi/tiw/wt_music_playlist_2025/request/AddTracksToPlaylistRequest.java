package it.polimi.tiw.wt_music_playlist_2025.request;

import it.polimi.tiw.wt_music_playlist_2025.entity.PlaylistTracks;

import java.util.ArrayList;
import java.util.List;

public class AddTracksToPlaylistRequest {
    private Integer playlistId;
    private List<Integer> selectedTracks = new ArrayList<>();

    public List<PlaylistTracks> toPlaylistTracks() {
        return selectedTracks.stream().map((trackId) -> {
            PlaylistTracks p = new PlaylistTracks();
            p.setPlaylistId(playlistId);
            p.setTrackId(trackId);
            return p;
        }).toList();
    }

    public Integer getPlaylistId() {
        return playlistId;
    }

    public void setPlaylistId(Integer playlistId) {
        this.playlistId = playlistId;
    }

    public List<Integer> getSelectedTracks() {
        return selectedTracks;
    }

    public void setSelectedTracks(List<Integer> selectedTracks) {
        this.selectedTracks = selectedTracks;
    }
}
