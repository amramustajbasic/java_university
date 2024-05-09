package org.example.Dao;
import org.example.Entity.*;
import org.example.Exceptions.UniException;

public interface UserDAO {
    User getUserById(int userId);

    User getUserByUsernameAndPass(String username, String password);

    void addUser(User user) throws UniException;
    void updateUser_role(User user,int new_role);
    void deleteUser(User user) throws UniException;
    // Other methods related to User entity
}