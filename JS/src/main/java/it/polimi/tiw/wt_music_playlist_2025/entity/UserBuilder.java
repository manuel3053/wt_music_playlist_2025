package it.polimi.tiw.wt_music_playlist_2025.entity;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UserBuilder implements EntityBuilder<User> {

    @Override
    public User build(ResultSet resultSet) throws SQLException {
        return new User();
    }
}
