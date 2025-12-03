package it.polimi.tiw.wt_music_playlist_2025.request;

import it.polimi.tiw.wt_music_playlist_2025.entity.Playlist;
import it.polimi.tiw.wt_music_playlist_2025.entity.PlaylistTracks;
import it.polimi.tiw.wt_music_playlist_2025.entity.Track;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class AddPlaylistRequest {
    private String title;
    private List<Integer> selectedTracks = new ArrayList<>();

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<Integer> getSelectedTracks() {
        return selectedTracks;
    }

    public void setSelectedTracks(List<Integer> selectedTracks) {
        this.selectedTracks = selectedTracks;
    }

    public Playlist toPlaylist(int authorId, List<Track> tracks) {
        Playlist playlist = new Playlist();
        playlist.setTitle(title);
        playlist.setAuthorId(authorId);
        playlist.setCustomOrder(false);
        playlist.setCreationDate(new Date(System.currentTimeMillis()));
        return playlist;
    }

    public List<PlaylistTracks> toPlaylistTracks(int playlistId) {
        return selectedTracks.stream().map((trackId) -> {
            PlaylistTracks p = new PlaylistTracks();
            p.setPlaylistId(playlistId);
            p.setTrackId(trackId);
            return p;
        }).toList();
    }

}
