package com.github.vyhovskyi.server;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.vyhovskyi.dao.DBConnectionManager;
import com.github.vyhovskyi.dao.jdbc.JdbcUserDao;
import com.github.vyhovskyi.entity.Group;
import com.github.vyhovskyi.entity.User;
import com.github.vyhovskyi.service.ServiceFactory;
import com.github.vyhovskyi.service.UserService;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.Connection;

public class PasswordUtils {



    public static String hashPassword(String plainPassword) {
        return BCrypt.hashpw(plainPassword, BCrypt.gensalt());
    }

    public static boolean checkPassword(String plainPassword, String hashedPassword) {
        return BCrypt.checkpw(plainPassword, hashedPassword);
    }
}