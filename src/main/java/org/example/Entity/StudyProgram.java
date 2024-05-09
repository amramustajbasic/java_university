package org.example.Entity;

public class StudyProgram implements IDType{
    private int programId;
    private int subjectId;
    private String subjectName;
    private String teachers;
    private int responsibleTeacherId;


    @Override
    public void setId(int id) {
        this.programId = id;
    }

    public void setSubjectId(int subjectId) {
        this.subjectId = subjectId;
    }

    public void setResponsibleTeacherId(int responsibleTeacherId) {
        this.responsibleTeacherId = responsibleTeacherId;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    public void setTeachers(String teachers) {
        this.teachers = teachers;
    }


    @Override
    public int getId() {
        return this.programId;
    }

    public int getSubjectId() {
        return subjectId;
    }

    public int getResponsibleTeacherId() {
        return responsibleTeacherId;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public String getTeachers() {
        return teachers;
    }

}
