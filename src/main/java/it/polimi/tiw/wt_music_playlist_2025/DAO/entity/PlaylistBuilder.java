package it.polimi.tiw.wt_music_playlist_2025.DAO.entity;

import java.sql.ResultSet;
import java.sql.SQLException;

public class PlaylistBuilder implements EntityBuilder<Playlist> {
    @Override
    public Playlist build(ResultSet resultSet) throws SQLException {
        return new Playlist();
    }
}
