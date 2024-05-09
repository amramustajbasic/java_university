package org.example.Dao;
import org.example.Entity.*;
import org.example.Exceptions.UniException;

public interface StudentDAO {
    Student getStudentById(int studentId);
    void addStudent(Student student) throws UniException;
    void updateStudent(Student student);
    void deleteStudent(Student student);
    // Other methods related to Student entity
}