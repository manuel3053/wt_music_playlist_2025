package it.polimi.tiw.wt_music_playlist_2025.DAO;

import it.polimi.tiw.wt_music_playlist_2025.entity.Track;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.NativeQuery;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TrackDAO extends JpaRepository<Track, Integer> {

    Track save(Track track);
    Track findTrackById(int id);
    List<Track> getAllByLoaderId(int loaderId);
    @NativeQuery(
            value = "select t.* " +
                    "from track t join user u on t.loader_id = u.id " +
                    "where u.id = ?1 " +
                    "order by t.author asc, t.album_publication_year asc, t.id asc"
    )
    List<Track> getAllByUserIdSorted(int userId);
    @NativeQuery(
            value = "select t.* " +
                    "from track t join playlist_tracks pt on t.id = pt.track_id " +
                    "where pt.playlist_id = ?1 " +
                    "order by t.author asc, t.album_publication_year asc, t.id asc " +
                    "offset ?2 rows " +
                    "fetch next 5 rows only"
    )
    List<Track> getPlaylistTracksGroup(int playlistId, int offset);
    @NativeQuery(
            value = "select * " +
                    "from track " +
                    "where id not in " +
                    "(" +
                    "select t.id " +
                    "from track t join playlist_tracks pt on t.id = pt.track_id " +
                    "where pt.playlist_id = ?1 " +
                    ") " +
                    "order by author asc, album_publication_year asc, id asc"
    )
    List<Track> getAllNotInPlaylist(int playlistId);

}
