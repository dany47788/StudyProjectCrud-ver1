package org.example.repository.impl;

import lombok.extern.slf4j.Slf4j;
import org.example.domain.Writer;
import org.example.exception.AppException;
import org.example.model.AppStatusCode;
import org.example.repository.WriterRepository;
import org.example.utils.DbConnection.ConnectionPool;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class WriterRepositoryImpl implements WriterRepository {

    @Override
    public List<Writer> findAll() {
        var sql = "SELECT * FROM Writer";
        var writers = new ArrayList<Writer>();

        try (var connection = ConnectionPool.getDataSource().getConnection();
             var preparedStatement = connection.prepareStatement(sql)) {
            var resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                writers.add(
                    new Writer(
                        resultSet.getInt("id"),
                        resultSet.getString("firstName"),
                        resultSet.getString("lastName")));
            }
            return writers;
        } catch (SQLException e) {
            throw new AppException(AppStatusCode.SQL_EXCEPTION, e);
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
                    resultSet.getString("lastName"));
            }
            return null;
        } catch (SQLException e) {
            throw new AppException(AppStatusCode.SQL_EXCEPTION, e);
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

            if (affectedRows == 0) {
                log.info("Error while creating");
                throw new AppException(AppStatusCode.SQL_EXCEPTION);
            }
            try (var keys = preparedStatement.getGeneratedKeys()) {
                if (keys.next()) {
                    writerId = keys.getInt(1);
                } else {
                    log.info("key have not generated");
                    throw new AppException(AppStatusCode.SQL_EXCEPTION);
                }
            }
        } catch (SQLException e) {
            throw new AppException(AppStatusCode.SQL_EXCEPTION, e);
        }
        return Writer.builder()
            .id(writerId)
            .firstName(entity.getFirstName())
            .lastName(entity.getLastName())
            .posts(entity.getPosts())
            .build();
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
            throw new AppException(AppStatusCode.SQL_EXCEPTION, e);
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
            throw new AppException(AppStatusCode.SQL_EXCEPTION, e);
        }
    }
}
