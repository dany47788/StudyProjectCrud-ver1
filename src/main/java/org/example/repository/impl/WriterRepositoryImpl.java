package org.example.repository.impl;

import org.example.domain.Writer;
import org.example.exception.NotFoundException;
import org.example.repository.WriterRepository;
import org.example.utils.DbConnection.ConnectionPool;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class WriterRepositoryImpl implements WriterRepository {

    @Override
    public List<Writer> findAll() {

        var sql = "SELECT * FROM Writer";
        ArrayList<Writer> writers = new ArrayList<>();

        try (var connection = ConnectionPool.getDataSource().getConnection();
             var preparedStatement = connection.prepareStatement(sql)) {
            var resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                writers.add(new Writer(
                    resultSet.getInt("id"),
                    resultSet.getString("firstName"),
                    resultSet.getString("lastName"),
                    new ArrayList<>()));
            }

            return writers;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Writer findById(Integer id) {

        String sql = "SELECT Writer.* FROM Writer WHERE Writer.id = ?";

        try (var connection = ConnectionPool.getDataSource().getConnection();
             var preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setInt(1, id);

            var resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {

                return new Writer(
                    resultSet.getInt("id"),
                    resultSet.getString("firstName"),
                    resultSet.getString("lastName"),
                    new ArrayList<>());
            }
            return null;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Writer create(Writer entity) {

        String sql = "INSERT INTO Writer (firstName, lastName) VALUES (?, ?)";
        Integer writerId;

        try (var connection = ConnectionPool.getDataSource().getConnection();
             var preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            preparedStatement.setString(1, entity.getLastName());
            preparedStatement.setString(2, entity.getFirstName());
            var affectedRows = preparedStatement.executeUpdate();

            if (affectedRows == 0)
                throw new SQLException("Creation error.");

            try (var keys = preparedStatement.getGeneratedKeys()) {
                if (keys.next()) {
                    writerId = keys.getInt(1);
                } else
                    throw new SQLException("Key wasn't generated!");
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return new Writer(writerId, entity.getFirstName(), entity.getLastName(), entity.getPosts());
    }

    @Override
    public Writer update(Writer entity) {

        var sql = "UPDATE Writer SET lastName = ?, firstName = ?  WHERE id = ?";

        try (var connection = ConnectionPool.getDataSource().getConnection();
             var preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, entity.getLastName());
            preparedStatement.setString(2, entity.getFirstName());
            preparedStatement.setInt(3, entity.getId());
            preparedStatement.execute();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return entity;
    }

    @Override
    public void deleteById(Integer id) {

        var sql = "DELETE FROM Writer WHERE id = ?";

        try (var connection = ConnectionPool.getDataSource().getConnection();
             var preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setInt(1, id);
            preparedStatement.execute();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
