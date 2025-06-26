package com.github.vyhovskyi.service;

import com.github.vyhovskyi.dao.UserDao;
import com.github.vyhovskyi.entity.User;
import com.github.vyhovskyi.exception.ServerException;
import com.github.vyhovskyi.server.PasswordUtils;

import java.util.Optional;

public class UserService {
    private UserDao userDao;

    public UserService(UserDao userDao) {
        this.userDao = userDao;
    }

    public void createUser(User user){
        try{
            user.setPassword(PasswordUtils.hashPassword(user.getPassword()));
            userDao.saveUser(user);
        }catch(Exception e){
            throw new ServerException("Error creating user", e);
        }
    }

    public void deleteUser(User user){
        try{
            userDao.deleteUser(user);
        }catch(Exception e){
            throw new ServerException("Error deleting user", e);
        }
    }

    public boolean isUserExist(String username){
        Optional<User> user = userDao.getUserByUsername(username);
        return user.isPresent();
    }


    public boolean authenticate(User user){
        if (user == null){
            return false;
        }

        if (user.getPassword() == null || user.getPassword().isEmpty()){
            return false;
        }
        if ( user.getUsername() == null || user.getUsername().isEmpty()){
            return false;
        }

        Optional<User> userOptional = userDao.getUserByUsername(user.getUsername());
        if(!userOptional.isPresent()){
            return false;
        }
        return  PasswordUtils.checkPassword(user.getPassword(), userOptional.get().getPassword());

    }
}
