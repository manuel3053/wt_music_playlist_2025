package it.polimi.tiw.wt_music_playlist_2025.DAO;

import it.polimi.tiw.wt_music_playlist_2025.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface UserDAO extends JpaRepository<User, Integer> {
    @Transactional
    User save(User user);
    @Transactional
    User findByUsername(String username);
}
