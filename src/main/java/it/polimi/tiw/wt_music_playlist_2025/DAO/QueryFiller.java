package it.polimi.tiw.wt_music_playlist_2025.DAO;

import java.sql.PreparedStatement;

public interface QueryFiller {
    void fill(PreparedStatement preparedStatement);
}
