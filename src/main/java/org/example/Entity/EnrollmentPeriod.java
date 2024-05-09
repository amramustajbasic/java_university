package org.example.Entity;

import java.util.Calendar;
import java.util.Date;

public class EnrollmentPeriod implements IDType {
    private int enrollment_period_id;
    private Date begin_period;
    private Date end_period;

    public Date getBegin_period() {
        return begin_period;
    }

    public Date getEnd_period() {
        return end_period;
    }

    @Override
    public int getId() {
        return enrollment_period_id;
    }

    @Override
    public void setId(int id) {
        this.enrollment_period_id = id;
    }

    public void setBegin_period(Date begin_period) {
        this.begin_period = begin_period;
    }

    public void setEnd_period(Date end_period) {
        this.end_period = end_period;
    }

    @Override
    public String toString() {
        return begin_period + " - " + end_period;
    }
}
