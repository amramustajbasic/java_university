package org.example.Dao;
import org.example.Entity.*;
import org.example.Exceptions.UniException;

public interface TeacherDAO {
    Teacher getTeacherByNameLastName(String name,String last_name);
    void addTeacher(Teacher teacher) throws UniException;
    void updateTeacher_zvanje(Teacher teacher, String title, int id);
    void deleteTeacher(Teacher teacher);
    // Other methods related to Teacher entity
}
