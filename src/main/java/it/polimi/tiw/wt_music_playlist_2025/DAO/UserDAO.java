package it.polimi.tiw.wt_music_playlist_2025.DAO;

import it.polimi.tiw.wt_music_playlist_2025.DAO.entity.User;
import it.polimi.tiw.wt_music_playlist_2025.DAO.entity.UserBuilder;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Optional;

public class UserDAO {
    private final Dao<User> dao;

    public UserDAO(Connection connection) {
        this.dao = new Dao<>(connection);
    }

    public Optional<User> login(String username, String password) throws SQLException {
        return dao.get(
                "SELECT * FROM user WHERE nickname = ? AND password = ?",
                (preparedStatement -> {}),
                new UserBuilder()
        );
    }
}
