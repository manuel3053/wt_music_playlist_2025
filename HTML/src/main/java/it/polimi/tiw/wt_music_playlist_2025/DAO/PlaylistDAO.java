package it.polimi.tiw.wt_music_playlist_2025.DAO;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import it.polimi.tiw.wt_music_playlist_2025.entity.Playlist;

@Repository
@Transactional
public interface PlaylistDAO extends JpaRepository<Playlist, Integer> {
    List<Playlist> findByAuthorIdOrderByCreationDateAsc(int authorId);

    Playlist save(Playlist playlist);
}
