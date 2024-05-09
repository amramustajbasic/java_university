package org.example.Dao;
import org.example.Entity.*;
public interface GradeDAO {
    Grade getGradeById( int studentId, int subjectId);
    void addGrade(Grade grade, Student student, Subject subject);
    void updateGrade(Grade grade,Student student, Subject subject);
    void deleteGrade(Grade grade);
    // Other methods related to Grade entity
}
