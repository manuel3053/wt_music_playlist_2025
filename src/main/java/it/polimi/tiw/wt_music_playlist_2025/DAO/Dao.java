package it.polimi.tiw.wt_music_playlist_2025.DAO;

import it.polimi.tiw.wt_music_playlist_2025.entity.Entity;
import it.polimi.tiw.wt_music_playlist_2025.entity.EntityBuilder;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Dao<T extends Entity> {
    private final Connection connection;

    public Dao(Connection connection) {
        this.connection = connection;
    }

    public Optional<T> get(String query, QueryFiller filler, EntityBuilder<T> builder) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(query);
        filler.fill(statement);
        ResultSet resultSet = statement.executeQuery();
        if (resultSet.next()) {
            close(statement, resultSet);
            return Optional.of(builder.build(resultSet));
        }
        close(statement, resultSet);
        return Optional.empty();
    }

    public List<T> getList(String query, QueryFiller filler, EntityBuilder<T> builder) throws SQLException {
        List<T> list = new ArrayList<>();
        PreparedStatement statement = connection.prepareStatement(query);
        filler.fill(statement);
        ResultSet resultSet = statement.executeQuery();
        while (resultSet.next()) {
            list.add(builder.build(resultSet));
        }
        close(statement, resultSet);
        return list;
    }

    public void update(String query, QueryFiller filler) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(query);
        filler.fill(statement);
        ResultSet resultSet = statement.executeQuery();
        close(statement, resultSet);
    }

    public void insert(String query, QueryFiller filler) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(query);
        filler.fill(statement);
        ResultSet resultSet = statement.executeQuery();
        close(statement, resultSet);
    }

    private void close(PreparedStatement statement, ResultSet resultSet) throws SQLException {
        resultSet.close();
        statement.close();
    }
}
