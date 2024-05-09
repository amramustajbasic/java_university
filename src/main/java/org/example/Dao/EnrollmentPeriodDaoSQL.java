package org.example.Dao;

import org.example.Entity.Enrollment;
import org.example.Entity.EnrollmentPeriod;
import org.example.Entity.Student;
import org.example.Entity.Subject;
import org.example.Exceptions.UniException;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class EnrollmentPeriodDaoSQL extends AbstractDAO<EnrollmentPeriod> implements EnrollmentPeriodDAO {
    /**
     * Constructor for AbstractDao that sets connection name and calls specific method to create connection
     *
     * @param tableName String
     */
    public EnrollmentPeriodDaoSQL(String tableName) {
        super(tableName);
    }

    @Override
    public EnrollmentPeriod row2object(ResultSet rs) throws UniException, SQLException {
        try{
            EnrollmentPeriod enrollmentPeriod = new EnrollmentPeriod();
            enrollmentPeriod.setId(rs.getInt("enrollment_period_id"));
            enrollmentPeriod.setBegin_period(rs.getDate("begin_period"));
            enrollmentPeriod.setEnd_period(rs.getDate("end_period"));
            return enrollmentPeriod;
        }catch (SQLException e){
            throw new UniException(e.getMessage(), e);
        }
    }

    @Override
    public Map<String, Object> object2row(EnrollmentPeriod object) throws UniException {
        Map<String, Object> row = new TreeMap<>();
        row.put("enrollment_period_id", object.getId());
        row.put("begin_period", object.getBegin_period());
        row.put("end_period", object.getEnd_period());
        return row;
    }

    @Override
    public EnrollmentPeriod getEnrollmentPeriodById(int id) {
        return null;
    }

    @Override
    public void addEnrollmentPeriod(EnrollmentPeriod period) {

    }

    public boolean isOnlyOne() throws UniException {
        int count = 0;
        try {
            PreparedStatement stmt = this.getConnection().prepareStatement("SELECT COUNT(*) FROM EnrollmentPeriod;");
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                count = rs.getInt(1);
            }
            rs.close();
            return (count == 1);
        } catch (SQLException e) {
            throw new UniException(e.getMessage(), e);
        }
    }

    public EnrollmentPeriod getLastPeriodAdded() throws UniException {
        EnrollmentPeriodDaoSQL enrollmentPeriodDaoSQL = new EnrollmentPeriodDaoSQL("EnrollmentPeriod");
        EnrollmentPeriod enrollmentPeriod = new EnrollmentPeriod();
        try {
            PreparedStatement stmt = this.getConnection().prepareStatement("SELECT * FROM EnrollmentPeriod WHERE enrollment_period_id = ?");
            stmt.setInt(1,enrollmentPeriodDaoSQL.getMaxId());
            ResultSet rs = stmt.executeQuery();
            if(rs.next()) {
                enrollmentPeriod = row2object(rs);
                rs.close();
            }
        } catch (SQLException e) {
            throw new UniException(e.getMessage(), e);
        } catch (UniException e) {
            throw new RuntimeException(e);
        }
        return enrollmentPeriod;
    }
    public int getMaxId() throws UniException {
        int maxUserId = 1;
        try {
            PreparedStatement stmt = this.getConnection().prepareStatement("SELECT MAX(enrollment_period_id) AS max_id FROM EnrollmentPeriod");
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                maxUserId = rs.getInt("max_id");
            }
        } catch (SQLException e) {
            throw new UniException(e.getMessage(), e);
        }
        return maxUserId;
    }
    public void addEnrollmentPeriodId(EnrollmentPeriod period, Date begin, Date end) {
        EnrollmentPeriodDaoSQL enrollmentPeriodDaoSQL = new EnrollmentPeriodDaoSQL("EnrollmentPeriod");
        try{
            PreparedStatement stmt = this.getConnection().prepareStatement("INSERT INTO EnrollmentPeriod (enrollment_period_id, begin_period, end_period) VALUES (?,?,?)");
            stmt.setInt(1,enrollmentPeriodDaoSQL.getMaxId()+1);
            stmt.setDate(2, new java.sql.Date(begin.getTime()));
            stmt.setDate(3, new java.sql.Date(end.getTime()));

            int rs = stmt.executeUpdate();

        }catch (SQLException | UniException e){
            throw new RuntimeException(e);
        }
    }

    @Override
    public void updateEnrollmentPeriod(EnrollmentPeriod period) {

    }
    public void deleteAll() {
        try{
            PreparedStatement stmt = this.getConnection().prepareStatement("DELETE EnrollmentPeriod from EnrollmentPeriod where enrollment_period_id > 0");
            int rs = stmt.executeUpdate();

        }catch (SQLException  e){
            throw new RuntimeException(e);
        }


    }
    @Override
    public void deleteEnrollmentPeriod(EnrollmentPeriod period) {

    }
}


