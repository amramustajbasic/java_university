package org.example.Dao;
import org.example.Entity.Enrollment;
import org.example.Exceptions.UniException;

public interface EnrollmentDAO {
    Enrollment getEnrollmentById(int enrollmentId);
    void addEnrollment(Enrollment enrollment) throws UniException;
    void updateEnrollment(Enrollment enrollment);
    void deleteEnrollment(Enrollment enrollment);
    // Other methods related to Enrollment entity
}
