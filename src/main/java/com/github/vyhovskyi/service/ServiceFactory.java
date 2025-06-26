package com.github.vyhovskyi.service;

import com.github.vyhovskyi.dao.DBConnectionManager;
import com.github.vyhovskyi.dao.jdbc.JdbcProductDao;
import com.github.vyhovskyi.dao.jdbc.JdbcUserDao;
import com.github.vyhovskyi.exception.ServerException;

import java.sql.Connection;
import java.sql.SQLException;

public class ServiceFactory {

    public static UserService getUserService() {
        try {
            Connection connection = DBConnectionManager.getConnection();
            return new UserService(new JdbcUserDao(connection));
        } catch (SQLException e) {
            throw new ServerException("Can`t connect to database", e);
        }
    }

    public static ProductService getProductService() {
        try{
            Connection connection = DBConnectionManager.getConnection();
            return new ProductService(new JdbcProductDao(connection));
        }catch (SQLException e) {
            throw new ServerException("Can`t connect to database", e);
        }
    }
}
