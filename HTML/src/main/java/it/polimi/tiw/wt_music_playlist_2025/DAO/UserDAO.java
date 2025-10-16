package it.polimi.tiw.wt_music_playlist_2025.DAO;

import it.polimi.tiw.wt_music_playlist_2025.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserDAO extends JpaRepository<User, Integer> {

    User findUserByUsernameAndPassword(String username, String password);

    User save(User user);
    //    public Optional<User> login(String username, String password) throws SQLException {
//        return dao.get(
//                "SELECT * FROM user WHERE username = ? AND password = ?",
//                (preparedStatement -> {
//                    preparedStatement.setString(0, username);
//                    preparedStatement.setString(1, password);
//                })
//        );
//    }
//
//    public void createUser(User user) throws SQLException {
//        dao.insert(
//                "INSERT INTO user (username, password, name, surname) " +
//                        "VALUES (?, ?, ?, ?,)",
//                (preparedStatement -> {
//                    preparedStatement.setString(0, user.username());
//                    preparedStatement.setString(1, user.password());
//                    preparedStatement.setString(2, user.name());
//                    preparedStatement.setString(3, user.surname());
//                })
//        );
//    }


}
