package org.example.Dao;

import org.example.Entity.Enrollment;
import org.example.Entity.Locked;
import org.example.Exceptions.UniException;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;
import java.util.TreeMap;

public class LockedDaoSQL extends AbstractDAO<Locked> implements LockedDAO {
    /**
     * Constructor for AbstractDao that sets connection name and calls specific method to create connection
     *
     * @param tableName String
     */
    LockedDaoSQL(String tableName) {
        super(tableName);
    }

    @Override
    public Locked row2object(ResultSet rs) throws UniException, SQLException {
        try{
           Locked locked = new Locked();
            locked.setId(rs.getInt("locked_id"));
            locked.setStudent_id(rs.getInt("student_id"));
            locked.setLocked(rs.getBoolean("locked"));
            return locked;
        }catch (SQLException e){
            throw new UniException(e.getMessage(), e);
        }
    }

    @Override
    public Map<String, Object> object2row(Locked object) throws UniException {
        Map<String, Object> row = new TreeMap<>();
        row.put("locked_id", object.getId());
        row.put("student_id", object.getStudent_id());
        row.put("locked", object.getLockedID());
        return row;
    }

    @Override
    public Enrollment getLockedById(int lockedID) {
        return null;
    }



    @Override
    public void addLocked(Locked locked) throws UniException {

    }

    @Override
    public void updateLocked(Locked locked) {

    }

    @Override
    public void deleteLocked(Locked locked) {

    }
}
