package it.polimi.tiw.wt_music_playlist_2025.DAO;

import it.polimi.tiw.wt_music_playlist_2025.entity.PlaylistTracks;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.NativeQuery;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PlaylistTracksDAO extends JpaRepository<PlaylistTracks, Integer> {
    PlaylistTracks save(PlaylistTracks playlistTracks);
    List<PlaylistTracks> getAllByPlaylistId(int playlistId);
}
