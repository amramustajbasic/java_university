package org.example.Dao;

import org.example.Entity.Enrollment;
import org.example.Entity.Student;
import org.example.Entity.Subject;
import org.example.Entity.Teacher;
import org.example.Exceptions.UniException;

import java.sql.*;
import java.util.*;

public class SubjectDaoSQL extends AbstractDAO<Subject> implements SubjectDAO {
    /**
     * Constructor for AbstractDao that sets connection name and calls specific method to create connection
     *
     * @param tableName String
     */
    public SubjectDaoSQL(String tableName) {
        super(tableName);
    }

    @Override
    public Subject row2object(ResultSet rs) throws UniException {
        try{
            Subject subject = new Subject();
            subject.setId(rs.getInt("subject_id"));
            subject.setName(rs.getString("name"));
            subject.setSemester(rs.getString("semester"));
            subject.setResponsibleTeacher(rs.getInt("responsible_teacher_id"));
            subject.setPrerequisite_subject(rs.getString("prerequisite_subject"));
            subject.setEcts(rs.getInt("ects"));
            subject.setGodina(rs.getInt("godina"));
            return subject;
        }catch (SQLException e){
            throw new UniException(e.getMessage(), e);
        }

    }

    @Override
    public Map<String, Object> object2row(Subject object) throws UniException {
        Map<String, Object> row = new TreeMap<>();
        row.put("subject_id", object.getId());
        row.put("name", object.getName());
        row.put("semester", object.getSemester());
        row.put("responsible_teacher_id", object.getResponsibleTeacher());
        row.put("prerequisite_subject", object.getPrerequisite_subject());
        row.put("ects", object.getEcts());
        row.put("godina",object.getGodina());
        return row;
    }

    @Override
    public Subject getSubjectById(int subjectId) {
        Subject subject = new Subject();
        try{
            PreparedStatement stmt = this.getConnection().prepareStatement("Select * FROM Subject WHERE subject_id = ? ");
            stmt.setInt(1,subjectId);
            ResultSet rs = stmt.executeQuery();
            if(rs.next()) {
                subject = row2object(rs);
                rs.close();
            }
        } catch (SQLException | UniException e) {
            throw new RuntimeException(e);
        }

        return subject;
    }

    public List<Subject> getAllUnpassed(Student student) throws UniException {
        String upit = "SELECT s.* FROM Subject s  JOIN UnpassedSubjects us ON s.subject_id = us.subject_id WHERE us.student_id = " + student.getId() + ";";
        List<Subject> results = new LinkedList<>();

        try{
            PreparedStatement stmt = getConnection().prepareStatement(upit);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()){
                Subject object = row2object(rs);
                results.add(object);
            }
            rs.close();
            return results;

        }catch (SQLException | UniException e){
            throw new UniException(e.getMessage(), e);
        }

    }

    public String getTeachers(int subject_id) throws UniException {
        String ispis = "";
        String upit = "SELECT sp.teachers FROM StudyProgram sp  JOIN Subject s ON sp.subject_id = s.subject_id WHERE s.subject_id = " + subject_id + ";";
        //List<Subject> results = new LinkedList<>();

        try{
            PreparedStatement stmt = getConnection().prepareStatement(upit);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()){
                ispis = rs.getString(1);
            }
            rs.close();
            return ispis;

        }catch (SQLException e){
            throw new UniException(e.getMessage(), e);
        }
    }

    public List<Subject> getAllProfesorsSubjects(int teacher_id) throws UniException{
        String upit = "SELECT * FROM Subject s  JOIN Teacher t ON s.responsible_teacher_id = t.teacher_id WHERE t.teacher_id = " + teacher_id + ";";
        List<Subject> results = new LinkedList<>();

        try{
            PreparedStatement stmt = getConnection().prepareStatement(upit);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()){
                Subject object = row2object(rs);
                results.add(object);
            }
            rs.close();
            return results;

        }catch (SQLException | UniException e){
            throw new UniException(e.getMessage(), e);
        }
    }

    public List<Subject> getSubjectsBySearch(String text){
        List<Subject> subjects = new LinkedList<>();
        String query = "SELECT * FROM Subject WHERE name LIKE '"+text+"%' OR name LIKE '%" + text+ "' OR name LIKE '%" + text + "%';";
        try{
            PreparedStatement stmt = this.getConnection().prepareStatement(query);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Subject subject = row2object(rs);
                subjects.add(subject);
            }
            rs.close();
            return subjects;
        } catch (SQLException | UniException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Subject> getAllWithPrerequisiteSubjects(Subject subject, String prereq) throws UniException {
        List<Subject> list = new LinkedList<>();
        try{
            PreparedStatement stmt = getConnection().prepareStatement("SELECT * FROM Subject where prerequisite_subject = ? AND subject_id = ?;");
            stmt.setString(1,prereq);
            stmt.setInt(2, subject.getId());
            ResultSet rs = stmt.executeQuery();

            while (rs.next()){
                Subject object = row2object(rs);
                list.add(object);
            }
            rs.close();
            return list;

        }catch (SQLException | UniException e){
            throw new UniException(e.getMessage(), e);
        }

    }
    public boolean isPassed(Student student,Subject subject) throws UniException {
            String upit = "SELECT COUNT(*) FROM Subject s JOIN Grade g ON s.subject_id = g.subject_id WHERE g.student_id = " + student.getId() + " AND g.grade > 5 AND g.subject_id = " + subject.getId() + ";";
            int count = 0;
            try{
                PreparedStatement stmt = getConnection().prepareStatement(upit);
                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    count = rs.getInt(1);
                }
                rs.close();
                return (count == 1);

            }catch (SQLException e){
                throw new UniException(e.getMessage(), e);
            }

    }

    public List<Subject> getAllUnpassedByYear(Student student,int year) throws UniException{
        String upit = "SELECT s.* FROM Subject s JOIN Grade g ON s.subject_id = g.subject_id WHERE g.student_id = " + student.getId() + " AND g.grade < 6  AND s.godina = " + year + ";";
        List<Subject> results = new LinkedList<>();
        try{
            PreparedStatement stmt = getConnection().prepareStatement(upit);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()){
                Subject object = row2object(rs);
                results.add(object);
            }
            rs.close();
            return results;

        }catch (SQLException | UniException e){
            throw new UniException(e.getMessage(), e);
        }
    }

    public List<Subject> getAllPassed(Student student) throws UniException{
        String upit = "SELECT s.* FROM Subject s JOIN Grade g ON s.subject_id = g.subject_id WHERE g.student_id = " + student.getId() + " AND g.grade > 5;";
        List<Subject> results = new LinkedList<>();
        try{
            PreparedStatement stmt = getConnection().prepareStatement(upit);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()){
                Subject object = row2object(rs);
                results.add(object);
            }
            rs.close();
            return results;

        }catch (SQLException | UniException e){
            throw new UniException(e.getMessage(), e);
        }
    }
    public List<Subject> getSubjectLike(String unos) throws UniException{
        List<Subject> results = new LinkedList<>();
        try{
            PreparedStatement stmt = this.getConnection().prepareStatement("SELECT * FROM Subject WHERE name LIKE ?;");
            stmt.setString(1,  unos  + "%" );
            ResultSet rs = stmt.executeQuery();
            while (rs.next()){
                Subject object = row2object(rs);
                results.add(object);
            }
            rs.close();
            return results;

        }catch (SQLException | UniException e){
            throw new UniException(e.getMessage(), e);
        }
    }
    public Subject getSubjectByName(String name){
       Subject subject = new Subject();
        try{
            PreparedStatement stmt = this.getConnection().prepareStatement("Select * FROM Subject WHERE name = ? ");
            stmt.setString(1,name);
            ResultSet rs = stmt.executeQuery();
            if(rs.next()) {
                subject = row2object(rs);
                rs.close();
            }
        } catch (SQLException | UniException e) {
            throw new RuntimeException(e);
        }

        return subject;
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

    @Override
    public void addSubject(Subject subject) throws UniException {

        try{
            PreparedStatement stmt = getConnection().prepareStatement("insert into Subject (subject_id, name, semester, responsible_teacher_id, prerequisite_subject, ects,godina) " +
                    "VALUES (?,?,?,?,?,?,?)");
            stmt.setInt(1,subject.getId());
            stmt.setString(2,subject.getName());
            stmt.setString(3,subject.getSemester());
            stmt.setInt(4,subject.getResponsibleTeacher());
            stmt.setString(5,subject.getPrerequisite_subject());
            stmt.setInt(6,subject.getEcts());
            stmt.setInt(7,subject.getGodina());
            stmt.executeUpdate();

        }catch (SQLException  e){
            throw new UniException(e.getMessage(), e);
        }
    }
    public int getMaxSubjectId() throws UniException {
        int maxSubjectId = 0;
        try {
             PreparedStatement stmt = this.getConnection().prepareStatement("SELECT MAX(subject_id) AS max_id FROM Subject");
             ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                maxSubjectId = rs.getInt("max_id");
            }
        } catch (SQLException e) {
            throw new UniException(e.getMessage(), e);
        }
        return maxSubjectId;
    }

    @Override
    public void updateSubject(Subject subject) {

    }


    public void updateSubject_preduslov(Subject subject, String prereq_subject, int id) {
        try{
            PreparedStatement stmt = this.getConnection().prepareStatement("UPDATE Subject SET prerequisite_subject = ? WHERE subject_id = ? ");
            stmt.setString(1,prereq_subject);
            stmt.setInt(2,id);
            int rs = stmt.executeUpdate();

        }catch (SQLException  e){
            throw new RuntimeException(e);
        }

    }

    public void updateSubjectPlanNastave(Subject subject, String[] predavaci, String nosilac){
//        try{
//            PreparedStatement stmt = this.getConnection().prepareStatement("UPDATE Subject SET prerequisite_subject = ? WHERE subject_id = ? ");
//            stmt.setString(1,prereq_subject);
//            stmt.setInt(2,id);
//            int rs = stmt.executeUpdate();
//
//        }catch (SQLException  e){
//            throw new RuntimeException(e);
//        }
    }

    @Override
    public void deleteSubject(Subject subject) {

    }

    public List<Subject> getAllByYearOfStudy(int godina) throws UniException, SQLException {

        PreparedStatement stmt = this.getConnection().prepareStatement("SELECT * FROM Subject WHERE godina = ?");
        stmt.setInt(1,godina);
        List<Subject> results = new LinkedList<>();

        try{
            ResultSet rs = stmt.executeQuery();
            while (rs.next()){
                Subject object = row2object(rs);
                results.add(object);
            }
            rs.close();
            return results;

        }catch (SQLException | UniException e){
            throw new UniException(e.getMessage(), e);
        }
    }
    public List<Subject> getAllBySemester(Subject subject) throws UniException, SQLException {

        PreparedStatement stmt = this.getConnection().prepareStatement("SELECT * FROM Subject WHERE semester = ?");
        stmt.setString(1,subject.getSemester());
        List<Subject> results = new LinkedList<>();

        try{
            ResultSet rs = stmt.executeQuery();
            while (rs.next()){
                Subject object = row2object(rs);
                results.add(object);
            }
            rs.close();
            return results;

        }catch (SQLException | UniException e){
            throw new UniException(e.getMessage(), e);
        }
    }

    public List<Subject> getAllEnrolled(Student student) throws UniException{
        String upit = "SELECT s.* FROM Subject s JOIN Enrollment e ON s.subject_id = e.subject_id WHERE e.student_id = " + student.getId() + ";";
        List<Subject> results = new LinkedList<>();
        try{
            PreparedStatement stmt = getConnection().prepareStatement(upit);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()){
                Subject object = row2object(rs);
                results.add(object);
            }
            rs.close();
            return results;

        }catch (SQLException | UniException e){
            throw new UniException(e.getMessage(), e);
        }
    }

}
