package it.polimi.tiw.wt_music_playlist_2025.DAO;

import it.polimi.tiw.wt_music_playlist_2025.entity.Playlist;
import it.polimi.tiw.wt_music_playlist_2025.entity.PlaylistBuilder;
import it.polimi.tiw.wt_music_playlist_2025.entity.Track;
import it.polimi.tiw.wt_music_playlist_2025.entity.TrackBuilder;
import org.jboss.jdeparser.FormatPreferences;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class PlaylistDAO {
    private final Dao<Playlist> dao;

    public PlaylistDAO(Connection connection) {
        this.dao = new Dao<>(connection);
    }

    public List<Playlist> getPlaylistsByAuthor(String author) throws SQLException {
        return dao.getList(
                "SELECT * FROM playlist WHERE author = ?",
                preparedStatement -> {
                    preparedStatement.setString(0, author);
                },
                new PlaylistBuilder()
        );
    }

    public Optional<Playlist> getPlaylistByTitle(String title) throws SQLException {
        return dao.get(
                "SELECT * FROM playlist WHERE title = ?",
                preparedStatement -> {
                    preparedStatement.setString(0, title);
                },
                new PlaylistBuilder()
        );
    }

    public void createPlaylist(Playlist playlist) throws SQLException {
        dao.insert(
                "INSERT INTO playlist (title, creationDate, author, customOrder) " +
                        "VALUES (?, ?, ?, ?,)",
                preparedStatement -> {
                    preparedStatement.setString(0, playlist.title());
                    preparedStatement.setString(1, playlist.creationDate());
                    preparedStatement.setString(2, playlist.author());
                    preparedStatement.setBoolean(3, playlist.customOrder());
                }
        );
    }

}
