package it.polimi.tiw.wt_music_playlist_2025.DAO;

import it.polimi.tiw.wt_music_playlist_2025.entity.Playlist;
import it.polimi.tiw.wt_music_playlist_2025.entity.Track;
import it.polimi.tiw.wt_music_playlist_2025.entity.TrackBuilder;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class TrackDAO {
    private final Dao<Track> dao;

    public TrackDAO(Connection connection) {
        this.dao = new Dao<>(connection);
    }

    public void createTrack(Track track) throws SQLException {
        dao.insert(
                "INSERT INTO track (" +
                        "file_path, " +
                        "image_path, " +
                        "title, " +
                        "author, " +
                        "album_title, " +
                        "album_publication_year, " +
                        "genre, " +
                        "position) " +
                        "VALUES (?, ?, ?, ?, ?, ?, ?, ?)",
                preparedStatement -> {
                    preparedStatement.setString(0, track.filePath());
                    preparedStatement.setString(1, track.imagePath());
                    preparedStatement.setString(2, track.title());
                    preparedStatement.setString(3, track.author());
                    preparedStatement.setString(4, track.albumTitle());
                    preparedStatement.setInt(5, track.albumPublicationYear());
                    preparedStatement.setString(6, track.genre());
                    // verificare se non dia problemi quando null
                    preparedStatement.setInt(7, track.position());
                }
        );
    }

    public List<Track> getTracksByPlaylistTitle(String title) throws SQLException {
        return dao.getList(
                "",
                preparedStatement -> {
                },
                new TrackBuilder()
        );
    }

    public Optional<Track> getTrackById(int id) throws SQLException {
        return dao.get(
                "SELECT * FROM track WHERE id = ?",
                preparedStatement -> {
                    preparedStatement.setInt(0, id);
                },
                new TrackBuilder()
        );
    }

    public void addTrackToPlaylist(Playlist playlist, Track track) throws SQLException {
        dao.insert(
                "INSERT INTO playlist_tracks(playlist_id, track_id)" +
                        "VALUES(?, ?,)",
                preparedStatement -> {
                    preparedStatement.setInt(0, playlist.id());
                    preparedStatement.setInt(1, track.id());
                }
        );
    }

    public void moveTrack(int position, Track track) throws SQLException {
        dao.update(
                "",
                preparedStatement -> {
                    preparedStatement.setInt(0, position);
                    preparedStatement.setInt(1, track.id());
                }
        );
    }


}
