package it.polimi.tiw.wt_music_playlist_2025.DAO;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.NativeQuery;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import it.polimi.tiw.wt_music_playlist_2025.entity.Track;

@Repository
@Transactional
public interface TrackDAO extends JpaRepository<Track, Integer> {

  Track save(Track track);

  Track findTrackByIdAndLoaderId(int id, int loaderId);

  @NativeQuery(value = "select * " +
      "from track " +
      "where loader_id = ?1 " +
      "order by author asc, album_publication_year asc, id asc")
  List<Track> getAllByUserIdSorted(int userId);

  @NativeQuery(value = "select t.* " +
      "from track t join playlist_tracks pt on t.id = pt.track_id " +
      "where pt.playlist_id = ?1 and t.loader_id = ?3 " +
      "order by t.author asc, t.album_publication_year asc, t.id asc " +
      "offset ?2 rows " +
      "fetch next 5 rows only")
  List<Track> getPlaylistTracksGroup(int playlistId, int offset, int userId);

  @NativeQuery(value = "select t.* " +
      "from track t join user u on t.loader_id = u.id " +
      "where u.id = ?1 and t.id not in " +
      "(" +
      "select tx.id " +
      "from track tx join playlist_tracks pt on tx.id = pt.track_id " +
      "where pt.playlist_id = ?2 " +
      ") " +
      "order by author asc, album_publication_year asc, id asc")
  List<Track> getAllNotInPlaylist(int userId, int playlistId);

  @NativeQuery(value = """
      select t.*
      from track t join playlist_tracks pt on t.id = pt.track_id join playlist p on p.id = pt.playlist_id
      where t.loader_id = ?1 and pt.playlist_id = ?2
      order by t.author asc, t.album_publication_year asc, t.id asc
      """)
  List<Track> getAllInPlaylist(int userId, int playlistId);

  @NativeQuery(value = """
      select t.*
      from track t join playlist_tracks pt on t.id = pt.track_id join playlist p on p.id = pt.playlist_id
      where t.loader_id = ?1 and pt.playlist_id = ?2
      order by pt.position
      """)
  List<Track> getAllInPlaylistCustom(int userId, int playlistId);

}
