package it.polimi.tiw.wt_music_playlist_2025.DAO;

import it.polimi.tiw.wt_music_playlist_2025.entity.PlaylistTracks;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.NativeQuery;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface PlaylistTracksDAO extends JpaRepository<PlaylistTracks, Integer> {
    @Transactional
    PlaylistTracks save(PlaylistTracks playlistTracks);

    @Transactional
    @NativeQuery(
            value = "select pt.* " +
                    "from track tx join playlist_tracks pt on tx.id = pt.track_id " +
                    "where pt.playlist_id = ?1 and tx.loader_id = ?2 "
    )
    List<PlaylistTracks> getAllByPlaylistId(int playlistId, int userId);
}
