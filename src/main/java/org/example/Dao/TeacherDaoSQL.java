package org.example.Dao;

import org.example.Entity.Student;
import org.example.Entity.Teacher;
import org.example.Entity.User;
import org.example.Exceptions.UniException;

import java.lang.reflect.Type;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class TeacherDaoSQL extends AbstractDAO<Teacher> implements TeacherDAO{
    /**
     * Constructor for AbstractDao that sets connection name and calls specific method to create connection
     *
     * @param tableName String
     */
    public TeacherDaoSQL(String tableName) {
        super(tableName);
    }

    @Override
    public Teacher row2object(ResultSet rs) throws UniException {
        try{
            Teacher teacher = new Teacher();
            teacher.setId(rs.getInt("teacher_id"));
            teacher.setUserId(rs.getInt("user_id"));
            teacher.setName(rs.getString("name"));
            teacher.setLastName(rs.getString("last_name"));
            teacher.setTitle(rs.getString("title"));
            return teacher;
        } catch (SQLException e){
            throw new UniException(e.getMessage(),e);
        }
    }

    @Override
    public Map<String, Object> object2row(Teacher object) throws UniException {
        Map<String, Object> row = new TreeMap<>();
        row.put("teacher_id", object.getId());
        row.put("title", object.getTitle());
        row.put("name", object.getName());
        row.put("last_name", object.getLastName());
        return row;
    }



    public List<Teacher> getTeacherByNameAndLastName(String name,String lastName){
        List<Teacher> teachers = new LinkedList<>();
        try{
            PreparedStatement stmt = this.getConnection().prepareStatement("Select * FROM Teacher WHERE name = ? AND last_name = ?"  );
            stmt.setString(1,name);
            stmt.setString(2,lastName);
            ResultSet rs = stmt.executeQuery();
            while(rs.next()) {
                Teacher teacher = row2object(rs);
                teachers.add(teacher);
            }
            rs.close();
            return teachers;
        } catch (SQLException | UniException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Teacher> getTeacherByName(String name){
        List<Teacher> teachers = new LinkedList<>();
        try{
            PreparedStatement stmt = this.getConnection().prepareStatement("Select * FROM Teacher WHERE name = ?"  );
            stmt.setString(1,name);
            ResultSet rs = stmt.executeQuery();
            while(rs.next()) {
                Teacher teacher = row2object(rs);
                teachers.add(teacher);
            }
            rs.close();
            return teachers;
        } catch (SQLException | UniException e) {
            throw new RuntimeException(e);
        }
    }
    @Override
    public Teacher getTeacherByNameLastName(String name, String last_name) {
        Teacher teacher = new Teacher();

        try{
            PreparedStatement stmt = this.getConnection().prepareStatement("Select * FROM Teacher WHERE name = ? AND last_name = ?");
            stmt.setString(1,name);
            stmt.setString(2,last_name);
            ResultSet rs = stmt.executeQuery();
            if(rs.next()) {
                teacher = row2object(rs);
                rs.close();
            }
        } catch (SQLException | UniException e) {
            throw new RuntimeException(e);
        }
        
        return teacher;
    }
    public List<Teacher> getTeacherLike(String name){
        List<Teacher> teachers = new LinkedList<>();
        try{
            PreparedStatement stmt = this.getConnection().prepareStatement("SELECT * FROM Teacher WHERE name LIKE ? OR last_name LIKE ?;"  );
            stmt.setString(1, name + "%");
            stmt.setString(2, name + "%");
            ResultSet rs = stmt.executeQuery();
            while(rs.next()) {
                Teacher teacher = row2object(rs);
                teachers.add(teacher);
            }
            rs.close();
            return teachers;
        } catch (SQLException | UniException e) {
            throw new RuntimeException(e);
        }
    }

    public Teacher getTeacherBySubject(int subject_id) {
        Teacher teacher = new Teacher();
        try{
            PreparedStatement stmt = this.getConnection().prepareStatement("Select t.* FROM Teacher t JOIN Subject s ON t.teacher_id = s.responsible_teacher_id WHERE subject_id = ?; ");
            stmt.setInt(1,subject_id);
            ResultSet rs = stmt.executeQuery();
            if(rs.next()) {
                teacher = row2object(rs);
                rs.close();
            }
        } catch (SQLException | UniException e) {
            throw new RuntimeException(e);
        }
        return teacher;
    }
    @Override
    public void addTeacher(Teacher teacher) throws UniException {
        try{
            PreparedStatement stmt = this.getConnection().prepareStatement("insert into Teacher (teacher_id, user_id, title, name, last_name)" +
                    "values (?, ?, ?, ?, ?)");
            stmt.setInt(1,teacher.getId());
            stmt.setInt(2, teacher.getUserId());
            stmt.setString(3,teacher.getTitle());
            stmt.setString(4,teacher.getName());
            stmt.setString(5,teacher.getLastName());
            stmt.executeUpdate();
        }
        catch (SQLException e){
            throw new UniException(e.getMessage(), e);

        }

    }
    public int getMaxTeacherId() throws UniException {
        int maxTeacherId = 0;
        try {
            PreparedStatement stmt = this.getConnection().prepareStatement("SELECT MAX(teacher_id) AS max_id FROM Teacher");
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                maxTeacherId = rs.getInt("max_id");
            }
        } catch (SQLException e) {
            throw new UniException(e.getMessage(), e);
        }
        return maxTeacherId;
    }

    public void updateTeacher_zvanje(Teacher teacher, String title, int id) {
        try{
            PreparedStatement stmt = this.getConnection().prepareStatement("UPDATE Teacher SET title = ? WHERE teacher_id = ? ");
            stmt.setString(1,title);
            stmt.setInt(2,id);
            int rs = stmt.executeUpdate();

        }catch (SQLException  e){
            throw new RuntimeException(e);
        }

    }
    public Teacher getTeacherByTeacherId(int id) throws UniException{
        String query = "SELECT * FROM Teacher WHERE teacher_id = ?";
        try {
            PreparedStatement stmt = this.getConnection().prepareStatement(query);
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                Teacher result = row2object(rs);
                rs.close();
                return result;

            } else {
                throw new RuntimeException("Object not found");
            }

        } catch (SQLException | UniException e) {
            throw new UniException(e.getMessage(), e);
        }
    }
    public Teacher getTeacherById(int id) throws UniException{
        String query = "SELECT * FROM Teacher WHERE user_id = ?";
        try {
            PreparedStatement stmt = this.getConnection().prepareStatement(query);
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                Teacher result = row2object(rs);
                rs.close();
                return result;

            } else {
                throw new RuntimeException("Object not found");
            }

        } catch (SQLException | UniException e) {
            throw new UniException(e.getMessage(), e);
        }
    }

    @Override
    public void deleteTeacher(Teacher teacher) {

    }

    @Override
    public List<Teacher> getAll() throws UniException {
        return super.getAll();
    }
}
