package org.online.cinema.security.verfication.service;

import org.online.cinema.store.entity.User;
import org.springframework.stereotype.Service;

import java.util.concurrent.ConcurrentHashMap;

@Service
public class TempUserService {

    private final ConcurrentHashMap<String, User> tempUsers = new ConcurrentHashMap<>();

    public void saveTempUser(String email, User user) {
        tempUsers.put(email, user);
    }

    public User getTempUser(String email) {
        return tempUsers.get(email);
    }

    public void removeTempUser(String email) {
        tempUsers.remove(email);
    }
}
