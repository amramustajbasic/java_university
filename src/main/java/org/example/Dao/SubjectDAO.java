package org.example.Dao;
import org.example.Entity.*;
import org.example.Exceptions.UniException;

public interface SubjectDAO {
    Subject getSubjectById(int subjectId);
    void addSubject(Subject subject) throws UniException;
    void updateSubject(Subject subject);
    void deleteSubject(Subject subject);
    // Other methods related to Subject entity
}