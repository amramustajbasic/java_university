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

public class RequestDaoSQL extends AbstractDAO<Request> implements RequestDAO {
    /**
     * Constructor for AbstractDao that sets connection name and calls specific method to create connection
     *
     * @param tableName String
     */
    public RequestDaoSQL(String tableName) {
        super(tableName);
    }

    @Override
    public Request row2object(ResultSet rs) throws UniException, SQLException {
        try{
            Request request = new Request();
            request.setId(rs.getInt("request_id"));
            request.setStudent_id(rs.getInt("student_id"));
            request.setSubject_id(rs.getInt("subject_id"));
            request.setMessage(rs.getString("message"));
            request.setStatus(rs.getString("status"));
            request.setVice_dean_id(rs.getInt("vice_dean_id"));
            request.setTeacher_id(rs.getInt("teacher_id"));
            return request;
        } catch (SQLException e){
            throw new UniException(e.getMessage(),e);
        }
    }

    @Override
    public Map<String, Object> object2row(Request object) throws UniException {
        Map<String, Object> row = new TreeMap<>();
        row.put("request_id", object.getId());
        row.put("student_id", object.getStudent_id());
        row.put("subject_id", object.getSubject_id());
        row.put("message", object.getMessage());
        row.put("status" ,object.getStatus());
        row.put("vice_dean_id", object.getVice_dean_id());
        row.put("teacher_id",object.getTeacher_id());

        return row;
    }

    public void insertChangeSubjects(int pred1, int pred2,int studentId){
        try{
            PreparedStatement stmt = this.getConnection().prepareStatement("INSERT INTO ZamjenaPredmeta(predmet1,predmet2,student_id) VALUES (?,?,?)");
            stmt.setInt(1, pred1);
            stmt.setInt(2,pred2);
            stmt.setInt(3,studentId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean studentSentRequestViceDean(int student_id) {
        boolean result = false;
        try {
            PreparedStatement stmt = this.getConnection().prepareStatement("SELECT CASE WHEN " +
                    "           EXISTS (SELECT 1 FROM Request WHERE student_id = ? AND (status = 'upit_zamjena' OR status = 'Upit_zamjena'))" +
                    " THEN 'true' ELSE 'false' END AS result;");
            stmt.setInt(1, student_id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                result = rs.getBoolean(1);
            }
            rs.close();
            return result;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

//    public Request getRequestByStudentId(int student_id){
//        Request request = new Request();
//        try{
//            //System.out.println(studentId);
//            PreparedStatement stmt = this.getConnection().prepareStatement("SELECT * FROM Request WHERE student_id = ? AND (status = 'Upit_zamjena' OR status = 'upit_zamjena'); ");
//            stmt.setInt(1, student_id );
//            ResultSet rs = stmt.executeQuery();
//            if(rs.next()) {
//                request =row2object(rs;
//            }
//            rs.close();
//            return request;
//        } catch (SQLException e) {
//            throw new RuntimeException(e);
//        }
//    }
    public int getFirstSubID(Student student, int predmet2){
        int predmet1 = 0;
        try{
            //System.out.println(studentId);
            PreparedStatement stmt = this.getConnection().prepareStatement("SELECT predmet1 FROM ZamjenaPredmeta WHERE predmet2 = ? AND student_id = ?");
            stmt.setInt(1, predmet2);
            stmt.setInt(2,student.getId());
            ResultSet rs = stmt.executeQuery();
            if(rs.next()) {
                predmet1 = rs.getInt("predmet1");
            }
            rs.close();
            return predmet1;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public List<Request> getAllRequestsByStudent(int studentId) throws UniException{
        List<Request> list = new LinkedList<>();

        try{
            PreparedStatement stmt = this.getConnection().prepareStatement("SELECT * FROM Request WHERE student_id = ? AND (status LIKE 'Upit%' OR status LIKE 'upit%')");
            stmt.setInt(1, studentId);

            ResultSet rs = stmt.executeQuery();
            while(rs.next()) {
                Request object = row2object(rs);
                list.add(object);
            }
            rs.close();
            return list;
        } catch (SQLException | UniException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Request> getAllAnswersByStudent(int studentId) throws UniException{
        List<Request> list = new LinkedList<>();

        try{
            PreparedStatement stmt = this.getConnection().prepareStatement("SELECT * FROM Request WHERE student_id = ? AND (status LIKE 'Odgovor%' OR status LIKE 'odgovor%')");
            stmt.setInt(1, studentId);

            ResultSet rs = stmt.executeQuery();
            while(rs.next()) {
                Request object = row2object(rs);
                list.add(object);
            }
            rs.close();
            return list;
        } catch (SQLException | UniException e) {
            throw new RuntimeException(e);
        }
    }


    public List<Request> getAllByStudent(int studentId) throws UniException{
        List<Request> list = new LinkedList<>();

        try{
            //System.out.println(studentId);
            PreparedStatement stmt = this.getConnection().prepareStatement("SELECT * FROM Request WHERE student_id = ?");
            stmt.setInt(1, studentId);
            ResultSet rs = stmt.executeQuery();
            while(rs.next()) {
                Request object = row2object(rs);
                list.add(object);
                //System.out.println(object.toString());
            }
            rs.close();
            return list;
        } catch (SQLException | UniException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Request> getAllBySubjectID(Subject subject) throws UniException{
        List<Request> list = new LinkedList<>();

        try{
            PreparedStatement stmt = this.getConnection().prepareStatement("SELECT * FROM Request WHERE subject_id = ?");
            stmt.setInt(1, subject.getId());
            ResultSet rs = stmt.executeQuery();
            if(rs.next()) {
                Request object = row2object(rs);
                list.add(object);
                rs.close();
            }
            return list;
        } catch (SQLException | UniException e) {
            throw new RuntimeException(e);
        }
    }
    public List<Request> getAllBySubjectIDRequests_Bodovi(Subject subject) throws UniException{
        List<Request> list = new LinkedList<>();

        try{
            PreparedStatement stmt = this.getConnection().prepareStatement("SELECT * FROM Request WHERE subject_id = ? AND status = 'Upit_prenos'");
            stmt.setInt(1, subject.getId());
            ResultSet rs = stmt.executeQuery();
            while(rs.next()) {
                Request object = row2object(rs);
                list.add(object);
            }
            rs.close();
            return list;
        } catch (SQLException | UniException e) {
            throw new RuntimeException(e);
        }
    }
    public List<Request> getAllBySubjectIDRequests_Slusanje(Subject subject) throws UniException{
        List<Request> list = new LinkedList<>();

        try{
            PreparedStatement stmt = this.getConnection().prepareStatement("SELECT * FROM Request WHERE subject_id = ? AND status = 'Upit_slusanje'");
            stmt.setInt(1, subject.getId());
            ResultSet rs = stmt.executeQuery();
            while(rs.next()) {
                Request object = row2object(rs);
                list.add(object);
            }
            rs.close();
            return list;
        } catch (SQLException | UniException e) {
            throw new RuntimeException(e);
        }
    }
    public List<Request> getAllBySubjectIDReply(Subject subject) throws UniException{
        List<Request> list = new LinkedList<>();

        try{
            PreparedStatement stmt = this.getConnection().prepareStatement("SELECT * FROM Request WHERE subject_id = ? AND status = 'Odgovor'");
            stmt.setInt(1, subject.getId());
            ResultSet rs = stmt.executeQuery();
            if(rs.next()) {
                Request object = row2object(rs);
                list.add(object);
                rs.close();
            }
            return list;
        } catch (SQLException | UniException e) {
            throw new RuntimeException(e);
        }
    }
    @Override
    public Request getRequestById(int requestId) {
        return null;
    }

    @Override
    public void addRequest(Request request,int new_id) throws UniException {
        try{
            PreparedStatement stmt = getConnection().prepareStatement("insert into Request (request_id, student_id, subject_id, message, status, vice_dean_id, teacher_id ) VALUES (?,?,?,?,?,?,?)");
            stmt.setInt(1,new_id);
            stmt.setInt(2,request.getStudent_id());
            stmt.setInt(3,request.getSubject_id());
            stmt.setString(4, request.getMessage());
            stmt.setString(5, request.getStatus());
            stmt.setInt(6,request.getVice_dean_id());
            stmt.setInt(7,request.getTeacher_id());
            stmt.executeUpdate();

        }catch (SQLException  e){
            throw new UniException(e.getMessage(), e);
        }
    }


    public int getMaxRequestId() throws UniException {
        int id = 1;
        try {
            PreparedStatement stmt = this.getConnection().prepareStatement("SELECT MAX(request_id) AS max_id FROM Request");
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                id = rs.getInt("max_id");
            }
        } catch (SQLException e) {
            throw new UniException(e.getMessage(), e);
        }
        return id;
    }
    public void addRequestStatus(Request request, int stud_id, int sub_id, String text, String status, int teacherId, int viceDeanId) throws UniException {
        RequestDaoSQL requestDaoSQL = new RequestDaoSQL("Request");
        try{
            PreparedStatement stmt = getConnection().prepareStatement("insert into Request (request_id, student_id, subject_id, message, status, vice_dean_id, teacher_id ) VALUES (?,?,?,?,?,?,?)");
            stmt.setInt(1,requestDaoSQL.getMaxRequestId()+1);
            stmt.setInt(2,stud_id);
            stmt.setInt(3,sub_id);
            stmt.setString(4, text);
            stmt.setString(5,status);
            if(viceDeanId == 0) {
                stmt.setInt(6, 0);
            }else{
                stmt.setInt(6, viceDeanId);
            }
            if(teacherId == 0) {
                stmt.setInt(7, 0);
            }else{
                stmt.setInt(7, teacherId);
            }
            stmt.executeUpdate();

        }catch (SQLException  e){
            throw new UniException(e.getMessage(), e);
        }
    }

    @Override
    public void updateRequest(Request request) {

    }

    @Override
    public void deleteRequest(Request request) {
        try{
            PreparedStatement stmt = this.getConnection().prepareStatement("DELETE FROM Request WHERE student_id = ? AND request_id = ?");
            stmt.setInt(1,request.getStudent_id());
            stmt.setInt(2,request.getId());
            int rs = stmt.executeUpdate();

        }catch (SQLException  e){
            throw new RuntimeException(e);
        }
    }
}
