package org.example.Entity;

public class Role implements IDType{
    private int roleId;
    private String roleName;

    @Override
    public void setId(int id) {
        this.roleId = id;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    @Override
    public int getId() {
        return roleId;
    }

    public String getRoleName() {
        return roleName;
    }
}
