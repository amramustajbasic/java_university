package org.example.Dao;

import org.example.Entity.Enrollment;
import org.example.Entity.Locked;
import org.example.Exceptions.UniException;

public interface LockedDAO {

    Enrollment getLockedById(int lockedID);
    void addLocked(Locked locked) throws UniException;
    void updateLocked(Locked locked);
    void deleteLocked(Locked locked);

}
