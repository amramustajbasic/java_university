package org.example.Entity;

public class Grade implements IDType {
    private int gradeId;
    private int student_id;
    private int subject_id;



    private int grade;
    @Override
    public void setId(int id) {
        this.gradeId = id;
    }

    public int getGradeId() {
        return gradeId;
    }


    public void setGrade(int grade) {
        this.grade = grade;
    }

    @Override
    public int getId() {
        return gradeId;
    }

    public void setStudentID(int student_id) {
        this.student_id = student_id;
    }

    public void setSubjectID(int subject_id) {
        this.subject_id = subject_id;
    }

    public int getGrade() {
        return grade;
    }


}
