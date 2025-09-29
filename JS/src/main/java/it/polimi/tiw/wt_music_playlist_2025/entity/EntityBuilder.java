package it.polimi.tiw.wt_music_playlist_2025.entity;

import java.sql.ResultSet;
import java.sql.SQLException;

public interface EntityBuilder<T extends Entity> {
    T build(ResultSet resultSet) throws SQLException;
}
