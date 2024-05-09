package org.example.Dao;

import org.example.Entity.Student;
import org.example.Entity.StudyProgram;
import org.example.Entity.Subject;
import org.example.Entity.Teacher;
import org.example.Exceptions.UniException;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;
import java.util.TreeMap;

public class StudyProgramDaoSQL extends AbstractDAO<StudyProgram> implements StudyProgramDAO{
    /**
     * Constructor for AbstractDao that sets connection name and calls specific method to create connection
     *
     * @param tableName String
     */
    public StudyProgramDaoSQL(String tableName) {
        super(tableName);
    }

    @Override
    public StudyProgram row2object(ResultSet rs) throws UniException, SQLException {
        try{
            StudyProgram studyProgram = new StudyProgram();
            studyProgram.setId(rs.getInt("program_id"));
            studyProgram.setSubjectId(rs.getInt("subject_id"));
            studyProgram.setSubjectName(rs.getString("subject_name"));
            studyProgram.setTeachers(rs.getString("teachers"));
            studyProgram.setResponsibleTeacherId(rs.getInt("responsible_teacher_id"));
            return studyProgram;
        } catch (SQLException e){
            throw new UniException(e.getMessage(),e);
        }
    }

    @Override
    public Map<String, Object> object2row(StudyProgram object) throws UniException {
        Map<String, Object> row = new TreeMap<>();
        row.put("program_id", object.getId());
        row.put("subject_id", object.getSubjectId());
        row.put("subject_name", object.getSubjectName());
        row.put("teachers", object.getTeachers());
        row.put("responsible_teacher_id", object.getResponsibleTeacherId());
        return row;
    }

    @Override
    public StudyProgram getStudyProgramById(int programId) {
        return null;
    }

    @Override
    public void addStudyProgram(StudyProgram studyProgram) {

    }

    public StudyProgram getStudyProgramBySubjectId(int subject_id){
       StudyProgram studyProgram = new StudyProgram();
        try{
            PreparedStatement stmt = this.getConnection().prepareStatement("SELECT * FROM StudyProgram WHERE subject_id = ?");
            stmt.setInt(1, subject_id);
            ResultSet rs = stmt.executeQuery();
            if(rs.next()) {
                studyProgram = row2object(rs);
                rs.close();
            }
            return studyProgram;
        } catch (SQLException | UniException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void updateStudyProgram(StudyProgram studyProgram) {

    }

    public void updateSubjectTeacher(Subject predmet, Teacher nosilac){
        try{
            PreparedStatement stmt = this.getConnection().prepareStatement("UPDATE Subject SET responsible_teacher_id = ?  WHERE subject_id = ?  ");
            stmt.setInt(1,nosilac.getId());
            stmt.setInt(2,predmet.getId());


            int rs = stmt.executeUpdate();

        }catch (SQLException  e){
            throw new RuntimeException(e);
        }
    }

    public int getMaxStudyProgramId() throws UniException {
        int max = 0;
        try {
            PreparedStatement stmt = this.getConnection().prepareStatement("SELECT MAX(program_id) AS max_id FROM StudyProgram");
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                max = rs.getInt("max_id");
            }
        } catch (SQLException e) {
            throw new UniException(e.getMessage(), e);
        }
        return max;
    }

    public boolean StudyProgramExists(int subject_id) throws UniException {
        int count = 0;
        try {
            PreparedStatement stmt = this.getConnection().prepareStatement("SELECT COUNT(*) FROM StudyProgram where subject_id = " + subject_id+ ";");
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                count = rs.getInt(1);
            }
            rs.close();
            return (count==1);
        } catch (SQLException e) {
            throw new UniException(e.getMessage(), e);
        }
    }

    public void insertPlanNastave(Subject predmet, String teachers, Teacher nosilac ){
        StudyProgramDaoSQL studyProgramDaoSQL = new StudyProgramDaoSQL("StudyProgram");
        try{
            PreparedStatement stmt = this.getConnection().prepareStatement("INSERT INTO StudyProgram (subject_id, subject_name, teachers, responsible_teacher_id) VALUES (?,?,?,?)");
            stmt.setInt(1,predmet.getId());
            stmt.setString(2,predmet.getName());
            stmt.setString(3,teachers);
            stmt.setInt(4,nosilac.getId());

            int rs = stmt.executeUpdate();

        }catch (SQLException  e){
            throw new RuntimeException(e);
        }

    }

    public void updatePlanNastave(Subject predmet, String teachers, Teacher nosilac ){
        try{
            PreparedStatement stmt = this.getConnection().prepareStatement("UPDATE StudyProgram SET subject_name = ? , teachers = ? , responsible_teacher_id = ?  WHERE subject_id = ?  ");
            stmt.setString(1,predmet.getName());
            stmt.setString(2,teachers);
            stmt.setInt(3,nosilac.getId());
            stmt.setInt(4,predmet.getId());

            int rs = stmt.executeUpdate();

        }catch (SQLException  e){
            throw new RuntimeException(e);
        }

    }

    @Override
    public void deleteStudyProgram(StudyProgram studyProgram) {

    }
}
