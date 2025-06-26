package com.github.vyhovskyi.dao.jdbc;

import com.github.vyhovskyi.dao.UserDao;
import com.github.vyhovskyi.entity.User;
import com.github.vyhovskyi.exception.ServerException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public class JdbcUserDao implements UserDao {
    private static final String GET_BY_USERNAME = "SELECT * FROM `user` WHERE `username` = ?";
    private static final String CREATE = "INSERT INTO `user` (`username`, `password`) VALUES (?, ?)";
    private static final String DELETE = "DELETE FROM `user` WHERE `username` = ?";

    private Connection connection;

    public JdbcUserDao(Connection connection) {this.connection = connection;}

    @Override
    public Optional<User> getUserByUsername(String username) {
        Optional<User> user = Optional.empty();
        try(PreparedStatement query = connection.prepareStatement(GET_BY_USERNAME)){
            query.setString(1, username);
            ResultSet rs = query.executeQuery();
            if(rs.next()) {
                user = Optional.of(User.builder().username(rs.getString("username")).password(rs.getString("password")).build());
            }
        }catch (SQLException e){
            throw new ServerException(e);
        }
        return user;
    }

    @Override
    public void saveUser(User user) {
        try(PreparedStatement query = connection.prepareStatement(CREATE)){
            query.setString(1, user.getUsername());
            query.setString(2, user.getPassword());
            query.executeUpdate();
        }catch (SQLException e){
            throw new ServerException(e);
        }
    }


    @Override
    public void deleteUser(User user) {
        try(PreparedStatement query = connection.prepareStatement(DELETE)){
            query.setString(1, user.getUsername());
            query.executeUpdate();
        }catch (SQLException e){
            throw new ServerException(e);
        }
    }

    @Override
    public void close(){
        try{
            connection.close();
        }catch (SQLException e) {
            throw new ServerException("Error closing connection", e);
        }
    }
}
