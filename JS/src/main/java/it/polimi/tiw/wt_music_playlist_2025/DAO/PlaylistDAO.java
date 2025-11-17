package it.polimi.tiw.wt_music_playlist_2025.DAO;

import it.polimi.tiw.wt_music_playlist_2025.entity.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.NativeQuery;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface PlaylistDAO extends JpaRepository<Playlist, Integer> {
    List<Playlist> findByAuthorIdOrderByCreationDateAsc(int authorId);

    Playlist save(Playlist playlist);
}
