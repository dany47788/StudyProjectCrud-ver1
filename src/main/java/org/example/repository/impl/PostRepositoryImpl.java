package org.example.repository.impl;

import lombok.extern.slf4j.Slf4j;
import org.example.domain.Post;
import org.example.domain.enums.PostStatus;
import org.example.exception.AppException;
import org.example.model.AppStatusCode;
import org.example.repository.PostRepository;
import org.example.utils.DbConnection.ConnectionPool;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class PostRepositoryImpl implements PostRepository {

    @Override
    public List<Post> findAll() {
        var sql = "SELECT * FROM Post";
        ArrayList<Post> posts = new ArrayList<>();

        try (var connection = ConnectionPool.getDataSource().getConnection();
             var preparedStatement = connection.prepareStatement(sql)) {

            var resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                posts.add(
                    Post.builder()
                        .id(resultSet.getInt("id"))
                        .updated(resultSet.getTimestamp("updated").toLocalDateTime())
                        .created(resultSet.getTimestamp("created").toLocalDateTime())
                        .writerId(resultSet.getInt("writer_id"))
                        .content(resultSet.getString("content"))
                        .postStatus(PostStatus.valueOf(resultSet.getString("status")))
                        .build());
            }
            return posts;
        } catch (SQLException e) {
            throw new AppException(AppStatusCode.SQL_EXCEPTION);
        }
    }

    @Override
    public Post findById(Integer id) {
        var sql = "SELECT * FROM Post WHERE id = ?";

        try (var connection = ConnectionPool.getDataSource().getConnection();
             var preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                return Post.builder()
                    .id(resultSet.getInt("id"))
                    .updated(resultSet.getTimestamp("updated").toLocalDateTime())
                    .created(resultSet.getTimestamp("created").toLocalDateTime())
                    .writerId(resultSet.getInt("writer_id"))
                    .content(resultSet.getString("content"))
                    .postStatus(PostStatus.valueOf(resultSet.getString("status")))
                    .build();
            }
            return null;
        } catch (SQLException e) {
            throw new AppException(AppStatusCode.SQL_EXCEPTION);
        }
    }

    @Override
    public Post create(Post entity) {

        var sql = "INSERT INTO Post (updated, created, writer_id, content, status) VALUES (?,?,?,?,?)";
        Integer postId;

        try (var connection = ConnectionPool.getDataSource().getConnection();
             var preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            preparedStatement.setDate(1, (Date) Date.from(entity.getUpdated().toInstant(ZoneOffset.UTC)));
            preparedStatement.setDate(2, (Date) Date.from(entity.getCreated().toInstant(ZoneOffset.UTC)));
            preparedStatement.setInt(3, entity.getWriterId());
            preparedStatement.setString(4, entity.getContent());
            preparedStatement.setString(5, String.valueOf(entity.getPostStatus()));
            preparedStatement.execute();

            int affectedRows = preparedStatement.executeUpdate();

            if (affectedRows == 0) {
                log.info("Error while creating");
                throw new AppException(AppStatusCode.SQL_EXCEPTION);
            }
            try (ResultSet keys = preparedStatement.getGeneratedKeys()) {
                if (keys.next()) {
                    postId = keys.getInt(1);
                } else {
                    log.info("key have not generated");
                    throw new AppException(AppStatusCode.SQL_EXCEPTION);
                }
            }
        } catch (SQLException e) {
            throw new AppException(AppStatusCode.SQL_EXCEPTION);
        }
        return Post.builder()
            .id(postId)
            .created(entity.getCreated())
            .updated(entity.getUpdated())
            .writerId(entity.getWriterId())
            .content(entity.getContent())
            .postStatus(entity.getPostStatus())
            .labels(entity.getLabels())
            .build();
    }

    @Override
    public Post update(Post entity) {
        var sql = "UPDATE Post SET updated = ?, writer_id = ?, content = ?, status = ? WHERE id = ?";

        try (var connection = ConnectionPool.getDataSource().getConnection();
             var preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setDate(1, (Date) Date.from(entity.getUpdated().toInstant(ZoneOffset.UTC)));
            preparedStatement.setInt(2, entity.getWriterId());
            preparedStatement.setString(3, entity.getContent());
            preparedStatement.setString(4, String.valueOf(entity.getPostStatus()));
            preparedStatement.setInt(5, entity.getId());
            preparedStatement.execute();
        } catch (SQLException e) {
            throw new AppException(AppStatusCode.SQL_EXCEPTION);
        }
        return entity;
    }

    @Override
    public void deleteById(Integer id) {
        var sql = "DELETE FROM Post WHERE id = ?";

        try (var connection = ConnectionPool.getDataSource().getConnection();
             var preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setInt(1, id);
            preparedStatement.execute();
        } catch (SQLException e) {
            throw new AppException(AppStatusCode.SQL_EXCEPTION);
        }
    }

    @Override
    public List<Post> findByLabelId(Integer labelId) {
        var sql = "SELECT Post.* FROM Post JOIN PostLabel ON PostLabel.post_id = Post.id WHERE PostLabel.label_id = ?";
        ArrayList<Post> posts = new ArrayList<>();

        try (var connection = ConnectionPool.getDataSource().getConnection();
             var preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setInt(1, labelId);
            var resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                posts.add(
                    Post.builder()
                        .id(resultSet.getInt("id"))
                        .updated(resultSet.getTimestamp("updated").toLocalDateTime())
                        .created(resultSet.getTimestamp("created").toLocalDateTime())
                        .writerId(resultSet.getInt("writer_id"))
                        .content(resultSet.getString("content"))
                        .postStatus(PostStatus.valueOf(resultSet.getString("status")))
                        .build());
            }
            return posts;
        } catch (SQLException e) {
            throw new AppException(AppStatusCode.SQL_EXCEPTION);
        }
    }

    @Override
    public List<Post> findByWriterId(Integer writerId) {
        var sql = "SELECT Post.* FROM Post JOIN Writer ON Writer.id = Post.writer_id WHERE Writer.id = ?";
        ArrayList<Post> posts = new ArrayList<>();

        try (var connection = ConnectionPool.getDataSource().getConnection();
             var preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setInt(1, writerId);
            var resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                posts.add(
                    Post.builder()
                        .id(resultSet.getInt("id"))
                        .updated(resultSet.getTimestamp("updated").toLocalDateTime())
                        .created(resultSet.getTimestamp("created").toLocalDateTime())
                        .writerId(resultSet.getInt("writer_id"))
                        .content(resultSet.getString("content"))
                        .postStatus(PostStatus.valueOf(resultSet.getString("status")))
                        .build());
            }
            return posts;
        } catch (SQLException e) {
            throw new AppException(AppStatusCode.SQL_EXCEPTION);
        }
    }
}
