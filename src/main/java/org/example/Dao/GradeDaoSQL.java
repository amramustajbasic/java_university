package org.example.Dao;

import org.example.Entity.Grade;
import org.example.Entity.Student;
import org.example.Entity.Subject;
import org.example.Exceptions.UniException;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

public class GradeDaoSQL extends AbstractDAO<Grade> implements GradeDAO {

    /**
     * Constructor for AbstractDao that sets connection name and calls specific method to create connection
     *
     * @param tableName String
     */
    public GradeDaoSQL(String tableName) {
        super(tableName);
    }

    @Override
    public Grade row2object(ResultSet rs) throws UniException, SQLException {
        try{
            Grade ocjena = new Grade();
            ocjena.setId(rs.getInt("grade_id"));
            ocjena.setStudentID(rs.getInt("student_id"));
            ocjena.setSubjectID(rs.getInt("subject_id"));
            ocjena.setGrade(rs.getInt("grade"));
            return ocjena;
        }catch (SQLException e){
            throw new UniException(e.getMessage(), e);
        }
    }

    @Override
    public Map<String, Object> object2row(Grade object) throws UniException {
        return null;
    }

    @Override
    public Grade getGradeById(int studentId, int subjectId) {
       Grade ocjena = new Grade();

        try{
            PreparedStatement stmt = this.getConnection().prepareStatement("SELECT g.* FROM Grade g " +
                    "JOIN Student s ON g.student_id = s.student_id" +
                    " JOIN Subject sub ON g.subject_id = sub.subject_id " +
                    "WHERE s.student_id = ? AND sub.subject_id = ?;");
            stmt.setInt(1,studentId);
            stmt.setInt(2,subjectId);
            ResultSet rs = stmt.executeQuery();
            if(rs.next()) {
                ocjena = row2object(rs);
                rs.close();
            }
        } catch (SQLException | UniException e) {
            throw new RuntimeException(e);
        }
        return ocjena;

    }

    @Override
    public void addGrade(Grade grade, Student student, Subject subject) {
        try{
            PreparedStatement stmt = this.getConnection().prepareStatement("INSERT INTO Grade (student_id, subject_id, grade)" +
                    "VALUES (?, ?, ?);");
            stmt.setInt(1,student.getId());
            stmt.setInt(2,subject.getId());
            stmt.setInt(3,grade.getGrade());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    public int getBodovi(int student_id, Subject predmet){
        int bodovi = 0;
        try{
            PreparedStatement stmt = this.getConnection().prepareStatement("SELECT g.bodovi FROM Grade g " +
                    "JOIN Student s ON g.student_id = s.student_id" +
                    " JOIN Subject sub ON g.subject_id = sub.subject_id " +
                    "WHERE s.student_id = ? AND sub.subject_id = ?;");
            stmt.setInt(1,student_id);
            stmt.setInt(2,predmet.getId());
            ResultSet rs = stmt.executeQuery();
            if(rs.next()) {
               bodovi = rs.getInt(1);
                rs.close();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return bodovi;
    }

    @Override
    public void updateGrade(Grade grade, Student student, Subject subject) {

        try {
            PreparedStatement stmt = this.getConnection().prepareStatement(" UPDATE Grade" +
                    "        SET grade = ?" +
                    "                WHERE student_id = ?" +
                    "                AND subject_id = ?;");
            stmt.setInt(1,grade.getGrade());
            stmt.setInt(2,student.getId());
            stmt.setInt(3,subject.getId());
            stmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public void deleteGrade(Grade grade) {

    }
}
