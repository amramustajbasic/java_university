package org.example.Entity;

public class User implements IDType{
    private int userId;
    private String username;
    private String password;
    private int role_id;

    @Override
    public void setId(int id) {
        this.userId = id;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setRole(int role) {
        this.role_id = role;
    }

    @Override
    public int getId() {
        return userId;
    }
    public String getUsername() {
        return username;
    }
    public String getPassword() {
        return password;
    }

    public int getRole() {
        return role_id;
    }
}
