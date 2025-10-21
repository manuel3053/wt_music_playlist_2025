package it.polimi.tiw.wt_music_playlist_2025.DAO;

import it.polimi.tiw.wt_music_playlist_2025.entity.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.NativeQuery;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface PlaylistDAO extends JpaRepository<Playlist, Integer> {
    List<Playlist> findByAuthorId(int authorId);
    List<Playlist> findByAuthorIdOrderByCreationDateAsc(int authorId);
    Playlist findByTitle(String title);
    Playlist save(Playlist playlist);
    @NativeQuery(
            value = "insert into playlist_tracks " +
                    "(playlist_id, track_id) " +
                    "values (?1, ?2)"
    )
    Map<String, Object> addTrack(int playlistId, int trackId);

//    private final Dao<Playlist> dao;
//
//    public PlaylistDAO(Connection connection) {
//        this.dao = new Dao<>(connection, resultSet -> new Playlist(
//                resultSet.getInt("id"),
//                resultSet.getString("title"),
//                resultSet.getString("creation_date"),
//                resultSet.getString("author"),
//                resultSet.getBoolean("custom_order")
//        ));
////        this.dao = new Dao<>(connection, new PlaylistBuilder());
//    }
//
//    public List<Playlist> getPlaylistsByAuthor(String author) throws SQLException {
//        return dao.getList(
//                "SELECT * FROM playlist WHERE author = ?",
//                preparedStatement -> {
//                    preparedStatement.setString(0, author);
//                }
//        );
//    }
//
//    public Optional<Playlist> getPlaylistByTitle(String title) throws SQLException {
//        return dao.get(
//                "SELECT * FROM playlist WHERE title = ?",
//                preparedStatement -> {
//                    preparedStatement.setString(0, title);
//                }
//        );
//    }
//
//    public void createPlaylist(Playlist playlist) throws SQLException {
//        dao.insert(
//                "INSERT INTO playlist (title, creationDate, author, customOrder) " +
//                        "VALUES (?, ?, ?, ?,)",
//                preparedStatement -> {
//                    preparedStatement.setString(0, playlist.title());
//                    preparedStatement.setString(1, playlist.creationDate());
//                    preparedStatement.setString(2, playlist.author());
//                    preparedStatement.setBoolean(3, playlist.customOrder());
//                }
//        );
//    }

}
