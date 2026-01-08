package it.polimi.tiw.wt_music_playlist_2025.DAO;

import it.polimi.tiw.wt_music_playlist_2025.entity.Playlist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.NativeQuery;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface PlaylistDAO extends JpaRepository<Playlist, Integer> {
    @Transactional
    List<Playlist> findByAuthorIdOrderByCreationDateAsc(int authorId);
    @Transactional
    Playlist findByAuthorIdAndId(int authorId, int id);
    @Transactional
    Playlist save(Playlist playlist);

    @Transactional
    @NativeQuery(
            value = """
                    update playlist
                    set custom_order = true
                    where id = ?1
                    """
    )
    void setCustomOrder(int id);
}
