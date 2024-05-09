package org.example.Dao;

import org.example.Entity.StudyProgram;

public interface StudyProgramDAO {
    StudyProgram getStudyProgramById(int programId);
    void addStudyProgram(StudyProgram studyProgram);
    void updateStudyProgram(StudyProgram studyProgram);
    void deleteStudyProgram(StudyProgram studyProgram);
    // Other methods related to StudyProgram entity
}