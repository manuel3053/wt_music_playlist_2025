package it.polimi.tiw.wt_music_playlist_2025.DAO.entity;

import java.sql.ResultSet;
import java.sql.SQLException;

public class TrackBuilder implements EntityBuilder<Track> {
    @Override
    public Track build(ResultSet resultSet) throws SQLException {
        return new Track();
    }
}
