package org.example.Entity;

public class Locked implements IDType{
    private int lockedID;
    private int student_id;
    private boolean locked;
    @Override
    public void setId(int id) {
        this.lockedID = id;
    }

    public void setLocked(boolean locked) {
        this.locked = locked;
    }

    public void setStudent_id(int student_id) {
        this.student_id = student_id;
    }

    @Override
    public int getId() {
        return lockedID;
    }

    public int getStudent_id() {
        return student_id;
    }

    public int getLockedID() {
        return lockedID;
    }
}
