package org.example.Dao;

import org.example.Entity.EnrollmentPeriod;
import org.example.Entity.Request;

public interface EnrollmentPeriodDAO {

    EnrollmentPeriod getEnrollmentPeriodById(int id);
    void addEnrollmentPeriod(EnrollmentPeriod period);
    void updateEnrollmentPeriod(EnrollmentPeriod period);
    void deleteEnrollmentPeriod(EnrollmentPeriod period);

}
