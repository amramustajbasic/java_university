package org.example.Dao;

import com.sun.source.tree.BreakTree;
import org.example.Entity.*;
import org.example.Exceptions.UniException;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class StudentDaoSQL extends AbstractDAO<Student> implements StudentDAO {
    /**
     * Constructor for AbstractDao that sets connection name and calls specific method to create connection
     *
     * @param tableName String
     */
    public StudentDaoSQL(String tableName) {
        super(tableName);
    }

    @Override
    public Student row2object(ResultSet rs) throws UniException, SQLException {
        try{
            Student student = new Student();
            student.setId(rs.getInt("student_id"));
            student.setUserId(rs.getInt("user_id"));
            student.setYearOfStudy(rs.getInt("year_of_study"));
            student.setStatus(rs.getString("status"));
            student.setName(rs.getString("name"));
            student.setLastName(rs.getString("last_name"));
            return student;
        } catch (SQLException e){
            throw new UniException(e.getMessage(),e);
        }
    }

    @Override
    public Map<String, Object> object2row(Student object) throws UniException {
        Map<String, Object> row = new TreeMap<>();
        row.put("student_id", object.getId());
        row.put("user_id", object.getUserId());
        row.put("year_of_study", object.getYearOfStudy());
        row.put("status", object.getStatus());
        row.put("name", object.getStatus());
        row.put("last_name", object.getLastName());
        //row.put("request_id",object.getRequest_id());

        return row;
    }

    public List<Student> getStudentsByName(String name) {
        List<Student> students = new LinkedList<>();

        try{
            PreparedStatement stmt = this.getConnection().prepareStatement("Select * FROM Student WHERE name = ?;");
            stmt.setString(1,name);
            ResultSet rs = stmt.executeQuery();
            while(rs.next()) {
                Student student = row2object(rs);
                students.add(student);
            }
            rs.close();
        } catch (SQLException | UniException e) {
            throw new RuntimeException(e);
        }

        return students;
    }
    public List<Student> getStudentsByNameLastName(String name, String last_name) {
        List<Student> students = new LinkedList<>();

        try{
            PreparedStatement stmt = this.getConnection().prepareStatement("Select * FROM Student WHERE name = ? AND last_name = ?");
            stmt.setString(1,name);
            stmt.setString(2,last_name);
            ResultSet rs = stmt.executeQuery();
            while(rs.next()) {
                Student student = row2object(rs);
                students.add(student);
            }
            rs.close();
        } catch (SQLException | UniException e) {
            throw new RuntimeException(e);
        }

        return students;
    }

    public Student getStudentByNameLastName(String name, String last_name) {
      Student student = new Student();

        try{
            PreparedStatement stmt = this.getConnection().prepareStatement("Select * FROM Student WHERE name = ? AND last_name = ?");
            stmt.setString(1,name);
            stmt.setString(2,last_name);
            ResultSet rs = stmt.executeQuery();
            if(rs.next()) {
                student = row2object(rs);
                rs.close();
            }
        } catch (SQLException | UniException e) {
            throw new RuntimeException(e);
        }

        return student;
    }
    public List<Student> getAllEnrolled(int subject_id) throws UniException{
        String upit = "SELECT * from Student st JOIN Student_Subject SS on st.student_id = SS.student_id where SS.subject_id ="  + subject_id + ";";
        List<Student> results = new LinkedList<>();

        try{
            PreparedStatement stmt = getConnection().prepareStatement(upit);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()){
                Student object = row2object(rs);
                results.add(object);
            }
            rs.close();
            return results;

        }catch (SQLException | UniException e){
            throw new UniException(e.getMessage(), e);
        }
    }

    public List<Student> getAllEnrolledOnSubject(int subject_id) throws UniException{
        String upit = "SELECT st.* from Student st JOIN Enrollment e on st.student_id = e.student_id where e.subject_id ="  + subject_id + ";";
        List<Student> results = new LinkedList<>();

        try{
            PreparedStatement stmt = getConnection().prepareStatement(upit);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()){
                Student object = row2object(rs);
                results.add(object);
            }
            rs.close();
            return results;

        }catch (SQLException | UniException e){
            throw new UniException(e.getMessage(), e);
        }
    }

    public List<Student> getAllEnrolled() throws UniException{
        String upit = "SELECT st.* from Student st JOIN Student_Subject SS on st.student_id = SS.student_id ;";
        List<Student> results = new LinkedList<>();

        try{
            PreparedStatement stmt = getConnection().prepareStatement(upit);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()){
                Student object = row2object(rs);
                results.add(object);
            }
            rs.close();
            return results;

        }catch (SQLException | UniException e){
            throw new UniException(e.getMessage(), e);
        }
    }

    public List<Student> getAllEnrolledNextYear() throws UniException{
        String query = "SELECT s.* FROM Student s INNER JOIN (SELECT student_id, COUNT(*) AS enrollmentCount FROM Enrollment" +
                "    GROUP BY student_id HAVING COUNT(*) = 10) e ON s.student_id = e.student_id;";
        List<Student> results = new LinkedList<>();
        try{
            PreparedStatement stmt = getConnection().prepareStatement(query);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()){
                Student object = row2object(rs);
                results.add(object);
            }
            rs.close();
            return results;
        }catch (SQLException | UniException e){
            throw new UniException(e.getMessage(), e);
        }
    }


    public List<Student> getAllStudentsForNextYear( Teacher teacher, Subject subject) throws UniException{
        String query = "SELECT stud.* from Student stud JOIN Enrollment E on stud.student_id = E.student_id JOIN Subject S on S.subject_id = E.subject_id " +
                "JOIN Teacher T on S.responsible_teacher_id = T.teacher_id where T.teacher_id = ? and S.subject_id = ?;";
        List<Student> results = new LinkedList<>();

        try{
            PreparedStatement stmt = getConnection().prepareStatement(query);
            stmt.setInt(1,teacher.getId());
            stmt.setInt(2,subject.getId());
            ResultSet rs = stmt.executeQuery();
            while (rs.next()){
                Student object = row2object(rs);
                results.add(object);
            }
            rs.close();
            return results;

        }catch (SQLException | UniException e){
            throw new UniException(e.getMessage(), e);
        }
    }
    public boolean isEnrolled(int subject_id, int student_id) throws UniException{
        String upit = "SELECT COUNT(*) from Student st JOIN Student_Subject SS " +
                "on st.student_id = SS.student_id where SS.subject_id = " + subject_id + " AND SS.student_id = " + student_id;
        List<Student> results = new LinkedList<>();

        try{
            PreparedStatement stmt = getConnection().prepareStatement(upit);
            ResultSet rs = stmt.executeQuery();
            if(rs.next()) {
                int count = rs.getInt(1);
                rs.close();
                return count>0;
            }
            return !results.isEmpty();

        }catch (SQLException e){
            throw new UniException(e.getMessage(), e);
        }
    }

    public Student getStudentByUserId(User user){
        Student student = new Student();
        try{
            PreparedStatement stmt = this.getConnection().prepareStatement("SELECT * FROM Student WHERE user_id = ?");
            stmt.setInt(1, user.getId());
            ResultSet rs = stmt.executeQuery();
            if(rs.next()) {
                student = row2object(rs);
                rs.close();
            }
            return student;
        } catch (SQLException | UniException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Student> getStudentLike(String name){
        List<Student> students = new LinkedList<>();
        try{
            PreparedStatement stmt = this.getConnection().prepareStatement("SELECT * FROM Student WHERE name LIKE ? OR last_name LIKE ?;"  );
            stmt.setString(1, name + "%");
            stmt.setString(2, name + "%");
            ResultSet rs = stmt.executeQuery();
            while(rs.next()) {
                Student student = row2object(rs);
                students.add(student);
            }
            rs.close();
            return students;
        } catch (SQLException | UniException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Student getStudentById(int studentId) {
        Student student = new Student();
        try{
            PreparedStatement stmt = this.getConnection().prepareStatement("SELECT * FROM Student WHERE student_id = ?");
            stmt.setInt(1, studentId);
            ResultSet rs = stmt.executeQuery();
            if(rs.next()) {
                student = row2object(rs);
                rs.close();
            }
        } catch (SQLException | UniException e) {
            throw new RuntimeException(e);
        }
        return student;

    }
    public void automatskaRegistracijaPredmeta(Student student) throws UniException {
        int kljucevi_predmeta[] = {1,2,4,5,6,7,8,9,10,11};
        for(int kljuc : kljucevi_predmeta) {
            try {
                PreparedStatement stmt = getConnection().prepareStatement("insert into Student_Subject (student_id, subject_id,status)" +
                        "VALUES (?,?,?)");
                stmt.setInt(1, student.getId());
                stmt.setInt(2, kljuc);
                stmt.setString(3, "Prvi put");
                stmt.executeUpdate();

            } catch (SQLException e) {
                throw new UniException(e.getMessage(), e);
            }
        }
    }
    public String getStatusNaPredmetu(Student student){
        String ret = new String();
        try{
            PreparedStatement stmt = this.getConnection().prepareStatement("SELECT Status FROM Student_Subject WHERE student_id = ?");
            stmt.setInt(1, student.getId());
            ResultSet rs = stmt.executeQuery();
            if(rs.next()) {
                ret = rs.getString(1);
                rs.close();

            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return ret;

    }
    public int getMaxStudentId() throws UniException {
        int maxStudentId = 0;
        try {
            PreparedStatement stmt = this.getConnection().prepareStatement("SELECT MAX(student_id) AS max_id FROM Student");
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                maxStudentId = rs.getInt("max_id");
            }
        } catch (SQLException e) {
            throw new UniException(e.getMessage(), e);
        }
        return maxStudentId;
    }

    @Override
    public void addStudent(Student student) throws UniException {
        try{
            PreparedStatement stmt = getConnection().prepareStatement("insert into Student (student_id, user_id, year_of_study, status, name, last_name)" +
                    "VALUES (?,?,?,?,?,?)");
            stmt.setInt(1,student.getId());
            stmt.setInt(2, student.getUserId());
            stmt.setInt(3,student.getYearOfStudy());
            stmt.setString(4,student.getStatus());
            stmt.setString(5,student.getName());
            stmt.setString(6,student.getLastName());
            stmt.executeUpdate();

        }catch (SQLException  e){
            throw new UniException(e.getMessage(), e);
        }
    }

    public List<Student> getAllStudentsRequestForViceDean() throws UniException{
        String upit = "SELECT * FROM Student s JOIN Request r ON s.student_id = r.student_id WHERE r.status = 'Upit_zamjena' OR r.status = 'upit_zamjena';";
        List<Student> results = new LinkedList<>();

        try{
            PreparedStatement stmt = getConnection().prepareStatement(upit);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()){
                Student object = row2object(rs);
                results.add(object);
            }
            rs.close();
            return results;

        }catch (SQLException | UniException e){
            throw new UniException(e.getMessage(), e);
        }
    }
    public List<Student> getAllStudentsRequest() throws UniException{
        String upit = "SELECT * FROM Student s JOIN Request r ON s.student_id = r.student_id WHERE r.status LIKE 'Upit%' OR r.status LIKE 'upit%';";
        List<Student> results = new LinkedList<>();

        try{
            PreparedStatement stmt = getConnection().prepareStatement(upit);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()){
                Student object = row2object(rs);
                results.add(object);
            }
            rs.close();
            return results;

        }catch (SQLException | UniException e){
            throw new UniException(e.getMessage(), e);
        }
    }

    @Override
    public void updateStudent(Student student) {

    }

    public void updateStudentStatus(Student student, String status) {
        try{
            PreparedStatement stmt = this.getConnection().prepareStatement("UPDATE Student SET status = ? WHERE student_id = ? ");
            stmt.setString(1,status);
            stmt.setInt(2,student.getId());
            int rs = stmt.executeUpdate();

        }catch (SQLException  e){
            throw new RuntimeException(e);
        }
    }

    @Override
    public void deleteStudent(Student student) {

    }

    @Override
    public List<Student> getAll() throws UniException {
        return super.getAll();
    }
}
