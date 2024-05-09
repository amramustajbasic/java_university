package org.example.Entity;

import org.example.Dao.StudentDaoSQL;

public class Request implements IDType {
    private int requestId;
    private int student_id;
    private int subject_id;
    private String message;
    private String status;

    private int vice_dean_id;

    private int teacher_id;

    @Override
    public void setId(int id) {
        this.requestId = id;
    }

    public void setStudent_id(int student_id) {
        this.student_id = student_id;
    }

    public void setSubject_id(int subject_id) {
        this.subject_id = subject_id;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setTeacher_id(int teacher_id) {
        this.teacher_id = teacher_id;
    }

    public void setVice_dean_id(int vice_dean_id) {
        this.vice_dean_id = vice_dean_id;
    }

    @Override
    public int getId() {
        return requestId;
    }

    public int getStudent_id() {
        return student_id;
    }

    public int getSubject_id() {
        return subject_id;
    }

    public String getMessage() {
        return message;
    }

    public String getStatus() {
        return status;
    }

    public int getTeacher_id() {
        return teacher_id;
    }

    public int getVice_dean_id() {
        return vice_dean_id;
    }
    public String toResult(){
        String ret;
        StudentDaoSQL studentDaoSQL = new StudentDaoSQL("Student");
        Student student = studentDaoSQL.getStudentById(student_id);
        ret = student.getName() + " "  + student.getLastName()  + ": " + "'" + message +"'" ;
        return ret;
    }

    @Override
    public String toString() {
        return "Zahtjev ID: "+ requestId;
    }
}
