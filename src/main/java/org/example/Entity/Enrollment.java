package org.example.Entity;

public class Enrollment implements IDType {
    private int enrollmentId;
    private int studentId;
    private int subjectId;
    private String status;

    @Override
    public void setId(int id) {
        this.enrollmentId = id;
    }

    public void setSubjectId(int subjectId) {
        this.subjectId = subjectId;
    }

    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public int getId() {
        return enrollmentId;
    }

    public int getSubjectId() {
        return subjectId;
    }

    public int getStudentId() {
        return studentId;
    }

    public String getStatus() {
        return status;
    }

    @Override
    public String toString() {
        return "ID registracije: " + enrollmentId + ", ID studenta: " + studentId + ", ID predmeta: " + subjectId;
    }


}
