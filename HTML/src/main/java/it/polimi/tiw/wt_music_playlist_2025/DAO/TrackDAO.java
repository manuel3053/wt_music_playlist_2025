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
                    "from track t join playlist_tracks pt on t.id = pt.track_id " +
                    "where pt.playlist_id = ?1 " +
                    "order by t.author asc, t.album_publication_year asc, t.id asc " +
                    "offset ?2 rows " +
                    "fetch next 5 rows only"
    )
    List<Track> getPlaylistTracksGroup(int playlistId, int offset);
//    private final Dao<Track> dao;
//
//    public TrackDAO(Connection connection) {
//        this.dao = new Dao<>(connection, resultSet -> new Track(
//                resultSet.getInt("id"),
//                resultSet.getString("file_path"),
//                resultSet.getString("image_path"),
//                resultSet.getString("title"),
//                resultSet.getString("author"),
//                resultSet.getString("album_title"),
//                resultSet.getInt("album_publication_year"),
//                resultSet.getString("genre"),
//                resultSet.getInt("position")
//        ));
//    }
//
//    public void createTrack(Track track) throws SQLException {
//        dao.insert(
//                "INSERT INTO track (" +
//                        "file_path, " +
//                        "image_path, " +
//                        "title, " +
//                        "author, " +
//                        "album_title, " +
//                        "album_publication_year, " +
//                        "genre, " +
//                        "position) " +
//                        "VALUES (?, ?, ?, ?, ?, ?, ?, ?)",
//                preparedStatement -> {
//                    preparedStatement.setString(0, track.filePath());
//                    preparedStatement.setString(1, track.imagePath());
//                    preparedStatement.setString(2, track.title());
//                    preparedStatement.setString(3, track.author());
//                    preparedStatement.setString(4, track.albumTitle());
//                    preparedStatement.setInt(5, track.albumPublicationYear());
//                    preparedStatement.setString(6, track.genre());
//                    // verificare se non dia problemi quando null
//                    preparedStatement.setInt(7, track.position());
//                }
//        );
//    }
//
//    public List<Track> getTracksByPlaylistTitle(String title) throws SQLException {
//        return dao.getList(
//                "",
//                preparedStatement -> {
//                }
//        );
//    }
//
//    public Optional<Track> getTrackById(int id) throws SQLException {
//        return dao.get(
//                "SELECT * FROM track WHERE id = ?",
//                preparedStatement -> {
//                    preparedStatement.setInt(0, id);
//                }
//        );
//    }
//
//    public void addTrackToPlaylist(Playlist playlist, Track track) throws SQLException {
//        dao.insert(
//                "INSERT INTO playlist_tracks(playlist_id, track_id)" +
//                        "VALUES(?, ?,)",
//                preparedStatement -> {
//                    preparedStatement.setInt(0, playlist.id());
//                    preparedStatement.setInt(1, track.id());
//                }
//        );
//    }
//
//    public void moveTrack(int position, Track track) throws SQLException {
//        dao.update(
//                "",
//                preparedStatement -> {
//                    preparedStatement.setInt(0, position);
//                    preparedStatement.setInt(1, track.id());
//                }
//        );
//    }
//
//
}
