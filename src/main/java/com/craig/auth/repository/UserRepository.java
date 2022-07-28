package com.craig.auth.repository;

import com.craig.auth.domain.User;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class UserRepository {
    Map<String, User> userDb = new ConcurrentHashMap<>(); //mock db

    public User insert(User user){
        if (user == null) {
            throw new RuntimeException("invalid op, argument can't be null");
        }
        if(userDb.containsKey(user.getName())){
            throw new RuntimeException("user name " + user.getName() + " already exist");
        }
        userDb.putIfAbsent(user.getName(), user);

        return user;
    }

    public boolean deleteUser(User user){
        if (userDb.containsKey(user.getName())) {
            userDb.remove(user.getName());
            return true;
        }
        return false;
    }

    public User getUser(String userName){
        return userDb.get(userName);
    }
}
