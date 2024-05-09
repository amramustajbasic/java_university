package org.example.Dao;

import org.example.Entity.Teacher;
import org.example.Entity.User;
import org.example.Exceptions.UniException;

import java.sql.*;
import java.util.Map;
import java.util.TreeMap;


public class UserDaoSQL extends AbstractDAO<User> implements UserDAO{
    /**
     * Constructor for AbstractDao that sets connection name and calls specific method to create connection
     *
     * @param tableName String
     */
    public UserDaoSQL(String tableName) {
        super(tableName);
    }

    @Override
    public User getUserById(int userId) {
        User user = new User();

        try{
            PreparedStatement stmt = this.getConnection().prepareStatement("SELECT * FROM User WHERE user_id = ? ");
            stmt.setInt(1,userId);
            ResultSet rs = stmt.executeQuery();
            if(rs.next()){
                user = row2object(rs);
                rs.close();
            }
        }catch (SQLException | UniException e){
            throw new RuntimeException(e);
        }
        return user;
    }

    @Override
    public User getUserByUsernameAndPass(String username, String password) {
        User user = new User();
        try{
            PreparedStatement stmt = this.getConnection().prepareStatement("Select * FROM User WHERE username = ? AND password = ?");
            stmt.setString(1,username);
            stmt.setString(2,password);
            ResultSet rs = stmt.executeQuery();
            if(rs.next()) {
                user = row2object(rs);
                rs.close();
            }
        } catch (SQLException | UniException e) {
            throw new RuntimeException(e);
        }
        return user;
    }
    public User getOldProdekan() {
        User user = new User();
        try{
            PreparedStatement stmt = this.getConnection().prepareStatement("Select * FROM User WHERE role_id = 1");
            ResultSet rs = stmt.executeQuery();
            if(rs.next()) {
                user = row2object(rs);
                rs.close();
            }
        } catch (SQLException | UniException e) {
            throw new RuntimeException(e);
        }
        return user;
    }
    public User getUserByUsername(String username) {
        User user = new User();
        try{
            PreparedStatement stmt = this.getConnection().prepareStatement("Select * FROM User WHERE username = ? ");
            stmt.setString(1,username);
            ResultSet rs = stmt.executeQuery();
            if(rs.next()) {
                user = row2object(rs);
                rs.close();
            }
        } catch (SQLException | UniException e) {
            throw new RuntimeException(e);
        }
        return user;
    }
    public int getMaxUserId() throws UniException {
        int maxUserId = 0;
        try {
            PreparedStatement stmt = this.getConnection().prepareStatement("SELECT MAX(user_id) AS max_id FROM User");
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                maxUserId = rs.getInt("max_id");
            }
        } catch (SQLException e) {
            throw new UniException(e.getMessage(), e);
        }
        return maxUserId;
    }
    @Override
    public void addUser(User user) throws UniException {
        try{
            PreparedStatement stmt = getConnection().prepareStatement("insert into User (user_id, username, password, role_id) " +
                    "VALUES (?,?,?,?)");
            stmt.setInt(1,user.getId());
            stmt.setString(2, user.getUsername());
            stmt.setString(3,user.getPassword());
            stmt.setInt(4,user.getRole());
            stmt.executeUpdate();

        }catch (SQLException  e){
            throw new UniException(e.getMessage(), e);
        }


    }

    @Override
    public void updateUser_role(User user, int new_role) {
            try{
                PreparedStatement stmt = this.getConnection().prepareStatement("UPDATE User SET role_id = ? WHERE user_id = ? ");
                stmt.setInt(1,new_role);
                stmt.setInt(2,user.getId());
                stmt.executeUpdate();

            }catch (SQLException  e){
                throw new RuntimeException(e);
            }

    }

    @Override
    public void deleteUser(User user) throws UniException {
        String sql = "DELETE FROM User WHERE user_id = ?";

        try{
            PreparedStatement stmt = getConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            stmt.setObject(1, user.getId());
            stmt.executeUpdate();

        }catch (SQLException e){
            throw new UniException(e.getMessage(), e);
        }
    }

    @Override
    public User row2object(ResultSet rs) throws UniException {
        try{
            User user = new User();
            user.setId(rs.getInt("user_id"));
            user.setUsername(rs.getString("username"));
            user.setPassword(rs.getString("password"));
            user.setRole(rs.getInt("role_id"));
            return user;
        } catch (SQLException e){
            throw new UniException(e.getMessage(),e);
        }
    }

    @Override
    public Map<String, Object> object2row(User object) throws UniException {
        Map<String, Object> row = new TreeMap<>();
        row.put("user_id", object.getId());
        row.put("username", object.getUsername());
        row.put("password", object.getPassword());
        row.put("role_id", object.getRole());
        return row;
    }
}
