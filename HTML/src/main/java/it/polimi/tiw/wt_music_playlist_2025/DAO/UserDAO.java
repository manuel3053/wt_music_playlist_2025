package it.polimi.tiw.wt_music_playlist_2025.DAO;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import it.polimi.tiw.wt_music_playlist_2025.entity.User;

@Repository
@Transactional
public interface UserDAO extends JpaRepository<User, Integer> {
    User save(User user);

    User findByUsername(String username);
}
