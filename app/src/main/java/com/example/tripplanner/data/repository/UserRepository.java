package com.example.tripplanner.data.repository;

import com.example.tripplanner.data.local.dao.UserDao;
import com.example.tripplanner.data.local.db.AppDatabase;
import com.example.tripplanner.data.local.entity.UserEntity;
import com.example.tripplanner.security.PasswordHasher;

/**
 * Repositorio de usuarios. Delega en UserDao.
 */
public class UserRepository {

    private final UserDao userDao;

    public UserRepository(AppDatabase database) {
        this.userDao = database.userDao();
    }

    public long register(UserEntity user) {
        return userDao.insert(user);
    }

    public UserEntity login(String email, String password) {
        UserEntity user = userDao.getUserByEmail(email);
        if (user == null) {
            return null;
        }
        String stored = user.getPasswordHash();
        if (!PasswordHasher.verify(password, stored)) {
            return null;
        }
        if (PasswordHasher.needsMigration(stored)) {
            user.setPasswordHash(PasswordHasher.hash(password));
            userDao.update(user);
        }
        return user;
    }

    public UserEntity getUserById(int id) {
        return userDao.getUserById(id);
    }

    public UserEntity getUserByEmail(String email) {
        return userDao.getUserByEmail(email);
    }
}
