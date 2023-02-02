package org.example.repository.impl;

import lombok.extern.slf4j.Slf4j;
import org.example.domain.Label;
import org.example.exception.AppException;
import org.example.model.AppStatusCode;
import org.example.repository.LabelRepository;
import org.example.utils.DbConnection.ConnectionPool;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class LabelRepositoryImpl implements LabelRepository {
    @Override
    public List<Label> findAll() {
        var sql = "SELECT * FROM Label";
        var labels = new ArrayList<Label>();

        try (var connection = ConnectionPool.getDataSource().getConnection();
             var preparedStatement = connection.prepareStatement(sql)) {

            var resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                labels.add(
                    new Label(resultSet.getInt("id"),
                        resultSet.getString("name")));
            }
            return labels;
        } catch (SQLException e) {
            throw new AppException(AppStatusCode.SQL_EXCEPTION, e);
        }
    }

    @Override
    public Label findById(Integer id) {
        var sql = "SELECT * FROM Label WHERE id = ?";

        try (var connection = ConnectionPool.getDataSource().getConnection();
             var preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setInt(1, id);
            var resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                return new Label(resultSet.getInt("id"),
                    resultSet.getString("name"));
            }
            return null;
        } catch (SQLException e) {
            throw new AppException(AppStatusCode.SQL_EXCEPTION, e);
        }
    }

    @Override
    public Label create(Label entity) {
        String sql = "INSERT INTO Label (name) VALUES ?";
        Integer id;

        try (var connection = ConnectionPool.getDataSource().getConnection();
             var preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            preparedStatement.setString(1, entity.getName());

            int affectedRows = preparedStatement.executeUpdate();

            if (affectedRows == 0) {
                log.info("Error while creating");
                throw new AppException(AppStatusCode.SQL_EXCEPTION);
            }
            try (ResultSet keys = preparedStatement.getGeneratedKeys()) {
                if (keys.next()) {
                    id = keys.getInt(1);
                } else {
                    log.info("key have not generated");
                    throw new AppException(AppStatusCode.SQL_EXCEPTION);
                }
            }
        } catch (SQLException e) {
            throw new AppException(AppStatusCode.SQL_EXCEPTION, e);
        }
        return Label.builder()
            .id(id)
            .name(entity.getName())
            .posts(entity.getPosts())
            .build();
    }

    @Override
    public Label update(Label entity) {
        var sql = "UPDATE Label SET name = ? WHERE id = ?";

        try (var connection = ConnectionPool.getDataSource().getConnection();
             var preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, entity.getName());
            preparedStatement.setInt(2, entity.getId());
            preparedStatement.execute();
        } catch (SQLException e) {
            throw new AppException(AppStatusCode.SQL_EXCEPTION, e);
        }
        return entity;
    }

    @Override
    public void deleteById(Integer id) {
        var sqlDeleteLabel = "DELETE FROM Label WHERE id = ?";

        try (var connection = ConnectionPool.getDataSource().getConnection();
             var stmtDeleteLabel = connection.prepareStatement(sqlDeleteLabel)) {

            stmtDeleteLabel.setInt(1, id);
            stmtDeleteLabel.execute();
        } catch (SQLException e) {
            throw new AppException(AppStatusCode.SQL_EXCEPTION, e);
        }
    }

    @Override
    public Label findByName(String name) {
        var sql = "SELECT * FROM Label WHERE name = ?";

        try (var connection = ConnectionPool.getDataSource().getConnection();
             var preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, name);

            var resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                return new Label(
                    resultSet.getInt("id"),
                    resultSet.getString("name"));
            } else {
                return null;
            }
        } catch (SQLException e) {
            throw new AppException(AppStatusCode.SQL_EXCEPTION, e);
        }
    }

    public List<Label> findAllByPostId(Integer postId) {
        var sql = "SELECT Label.* FROM Label JOIN PostLabel ON PostLabel.label_id = Label.id WHERE PostLabel.Post_id = ?";
        var labels = new ArrayList<Label>();

        try (var connection = ConnectionPool.getDataSource().getConnection();
             var preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setInt(1, postId);

            var resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                labels.add(
                    new Label(resultSet.getInt("id"),
                        resultSet.getString("name")));
            }
            return labels;
        } catch (SQLException e) {
            throw new AppException(AppStatusCode.SQL_EXCEPTION, e);
        }
    }
}
