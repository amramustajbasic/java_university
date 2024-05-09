package org.example.Dao;

import org.example.Entity.*;
import org.example.Exceptions.UniException;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class EnrollmentDaoSQL extends AbstractDAO<Enrollment> implements EnrollmentDAO {
    /**
     * Constructor for AbstractDao that sets connection name and calls specific method to create connection
     *
     * @param tableName String
     */
    public EnrollmentDaoSQL(String tableName) {
        super(tableName);
    }

    @Override
    public Enrollment row2object(ResultSet rs) throws UniException, SQLException {
        try{
           Enrollment enrollment = new Enrollment();
            enrollment.setId(rs.getInt("enrollment_id"));
            enrollment.setStudentId(rs.getInt("student_id"));
            enrollment.setSubjectId(rs.getInt("subject_id"));
            enrollment.setStatus(rs.getString("status"));
            return enrollment;
        }catch (SQLException e){
            throw new UniException(e.getMessage(), e);
        }
    }

    @Override
    public Map<String, Object> object2row(Enrollment object) throws UniException {
        Map<String, Object> row = new TreeMap<>();
        row.put("enrollment_id", object.getId());
        row.put("student_id", object.getStudentId());
        row.put("subject_id", object.getSubjectId());
        row.put("status",object.getStatus());
        return row;
    }

    @Override
    public Enrollment getEnrollmentById(int enrollmentId) {
        return null;
    }

    public void enrollStudent (Request request){
        try {
            PreparedStatement stmt = getConnection().prepareStatement("INSERT INTO Enrollment(student_id, subject_id) VALUES (?,?)");
            stmt.setInt(1, request.getStudent_id());
            stmt.setInt(2,request.getSubject_id());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }
    public List<Enrollment> getAllByStudent(Student student) throws UniException{
        String query = "SELECT * FROM Enrollment WHERE student_id = ?";
        List<Enrollment> results = new LinkedList<>();

        try{
            PreparedStatement stmt = getConnection().prepareStatement(query);
            stmt.setInt(1,student.getId());
            ResultSet rs = stmt.executeQuery();
            while (rs.next()){
                Enrollment object = row2object(rs);
                results.add(object);
            }
            rs.close();
            return results;

        }catch (SQLException | UniException e){
            throw new UniException(e.getMessage(), e);
        }
    }


    public List<Enrollment> get10LastEnrollments(int student_id) throws UniException {
        List<Enrollment> enrollments = new LinkedList<>();
        try{
            PreparedStatement stmt = getConnection().prepareStatement("SELECT * from Enrollment where student_id = ?");
            stmt.setInt(1, student_id);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()){
                Enrollment object = row2object(rs);
                enrollments.add(object);
            }
            rs.close();
            return enrollments;


        }catch (SQLException e){
            throw new UniException(e.getMessage(), e);
        }
    }

    public void deleteAllByStudent(int student_id) throws UniException{
        String query = "DELETE FROM Enrollment WHERE student_id = ?";
        //List<Enrollment> results = new LinkedList<>();
        try{
            PreparedStatement stmt = getConnection().prepareStatement(query);
            stmt.setInt(1,student_id);
            int rs = stmt.executeUpdate();
//            while (rs.next()){
//                Enrollment object = row2object(rs);
//                results.add(object);
//            }
            //return results;

        }catch (SQLException e){
            throw new UniException(e.getMessage(), e);
        }
    }

    @Override
    public void addEnrollment(Enrollment enrollment) throws UniException {
        try{
            PreparedStatement stmt = getConnection().prepareStatement("INSERT INTO Enrollment(student_id, subject_id) VALUES (?,?)");
            stmt.setInt(1, enrollment.getStudentId());
            stmt.setInt(2,enrollment.getSubjectId());

            stmt.executeUpdate();

        }catch (SQLException  e){
            throw new UniException(e.getMessage(), e);
        }
    }

    public void addLocked(Student student, int locked) throws UniException {
        try{
            PreparedStatement stmt = getConnection().prepareStatement("INSERT INTO Locked(student_id, locked) VALUES (?,?)");
            stmt.setInt(1, student.getId());
            stmt.setInt(2,locked);

            stmt.executeUpdate();

        }catch (SQLException  e){
            throw new UniException(e.getMessage(), e);
        }

    }

    public boolean isLocked(Student student) throws UniException {
        int count = 0;
        try {
            PreparedStatement stmt = this.getConnection().prepareStatement("SELECT COUNT(*) FROM Locked where student_id = ? AND locked = 1;");
            stmt.setInt(1, student.getId());
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



    public List<Enrollment> getAllLocked(Student student, boolean locked) throws UniException {
        List<Enrollment> enrollments = new LinkedList<>();
        try{

            PreparedStatement stmt = getConnection().prepareStatement("SELECT * from Locked where student_id = ? AND locked = ?");
            stmt.setInt(1, student.getId());
            stmt.setBoolean(2,locked);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()){
                Enrollment object = row2object(rs);
                enrollments.add(object);
            }
            rs.close();
            return enrollments;


        }catch (SQLException e){
            throw new UniException(e.getMessage(), e);
        }
    }

    @Override
    public void updateEnrollment(Enrollment enrollment) {

    }

    @Override
    public void deleteEnrollment(Enrollment enrollment) {

    }

    public void updateEnrollmentChangeSubject(Student student,int firstId, int secondId){
        try{
            PreparedStatement stmt = this.getConnection().prepareStatement("UPDATE Enrollment SET subject_id = ? WHERE subject_id = ? AND student_id = ? ");
            stmt.setInt(1,secondId);
            stmt.setInt(2,firstId);
            stmt.setInt(3,student.getId());
            int rs = stmt.executeUpdate();

        }catch (SQLException  e){
            throw new RuntimeException(e);
        }
    }

    public void updateEnrollmentStatus(Student student, int subject_id, String status){
        try{
            PreparedStatement stmt = this.getConnection().prepareStatement("UPDATE Enrollment SET status = ? WHERE subject_id = ? AND student_id = ? ");
            stmt.setString(1,status);
            stmt.setInt(2,subject_id);
            stmt.setInt(3,student.getId());
            int rs = stmt.executeUpdate();

        }catch (SQLException  e){
            throw new RuntimeException(e);
        }
    }


    public void insertUnpassed(Student student,int subject_id){
        try{
            PreparedStatement stmt = this.getConnection().prepareStatement("INSERT INTO UnpassedSubjects(student_id, subject_id) VALUES (?,?) ");
            stmt.setInt(1,student.getId());
            stmt.setInt(2,subject_id);
            int rs = stmt.executeUpdate();

        }catch (SQLException  e){
            throw new RuntimeException(e);
        }
    }
    public Enrollment getByStudent(Student student, int subject_id) throws UniException {
        Enrollment object = new Enrollment();
        try{
            PreparedStatement stmt = getConnection().prepareStatement("SELECT * from Enrollment where student_id = ? AND subject_id = ?");
            stmt.setInt(1, student.getId());
            stmt.setInt(2,subject_id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()){
                object = row2object(rs);
            }
            rs.close();
            return object;


        }catch (SQLException e){
            throw new UniException(e.getMessage(), e);
        } catch (UniException e) {
            throw new RuntimeException(e);
        }
    }

//    public Enrollment getEnrollmentBySubjectId(){
//        Enrollment enrollment = new Enrollment();
//        try{
//            PreparedStatement stmt = this.getConnection().prepareStatement("SELECT * FROM Enrollment WHERE student_id = ? AND ");
//            stmt.setInt(1, studentId);
//            ResultSet rs = stmt.executeQuery();
//            if(rs.next()) {
//                student = row2object(rs);
//                rs.close();
//            }
//        } catch (SQLException | UniException e) {
//            throw new RuntimeException(e);
//        }
//        return student;
//    }

    public int getMaxEnrollmentId() throws UniException {
        int id = 1;
        try {
            PreparedStatement stmt = this.getConnection().prepareStatement("SELECT MAX(enrollment_id) AS max_id FROM Enrollment");
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                id = rs.getInt("max_id");
            }
        } catch (SQLException e) {
            throw new UniException(e.getMessage(), e);
        }
        return id;
    }

}
