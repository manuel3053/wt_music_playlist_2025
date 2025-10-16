package it.polimi.tiw.wt_music_playlist_2025.DAO;

import it.polimi.tiw.wt_music_playlist_2025.entity.PlaylistTracks;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlaylistTracksDAO extends JpaRepository<PlaylistTracks, Integer> {
    PlaylistTracks save(PlaylistTracks playlistTracks);
}
