package org.example.Frontend;

import org.example.Dao.*;
import org.example.Entity.*;
import org.example.Exceptions.UniException;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedList;
import java.util.List;

public class predmetInfo {
    public JPanel panel_predmet_info;
    private JTextField textField1;
    private JButton ispisiButton;
    private JTextField textField3;
    private JTextField textField4;
    private JTextField textField5;
    private JTextField textField6;
    private JTextField textField7;
    private JButton nazadButton;
    private JList list1;
    private JButton ucitajPredmeteButton;
    private JTextField textField2;
    private JTextField textField8;
    private JList list2;
    private JTextField TFpreduslovi;

    public predmetInfo(JFrame oldFrame, User user) {
    ispisiButton.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            String predmetText = textField1.getText();

//            if(predmetText.length() == 0){
//                JOptionPane.showMessageDialog(null, "Niste unijeli naziv predmeta", "Upozorenje", JOptionPane.WARNING_MESSAGE);
//                return;
//            }
            if(list1.getSelectedValue() == null){
                JOptionPane.showMessageDialog(oldFrame, "Niste odabrali zeljeni predmet", "Upozorenje", JOptionPane.WARNING_MESSAGE);
                return;
            }


            Student student = new Student();
            StudentDaoSQL studentDaoSQL = new StudentDaoSQL("Student");
            student = studentDaoSQL.getStudentByUserId(user);

            Subject subject = new Subject();
            SubjectDaoSQL subjectDaoSQL = new SubjectDaoSQL("Subject");
            subject = (Subject) list1.getSelectedValue();

            StudyProgram studyProgram = new StudyProgram();
            StudyProgramDaoSQL studyProgramDaoSQL = new StudyProgramDaoSQL("StudyProgram");

            Enrollment enrollment = new Enrollment();
            EnrollmentDaoSQL enrollmentDaoSQL = new EnrollmentDaoSQL("Enrollment");
            try {
                enrollment = enrollmentDaoSQL.getByStudent(student,subject.getId());
            } catch (UniException e) {
                throw new RuntimeException(e);
            }

            studyProgram = studyProgramDaoSQL.getStudyProgramBySubjectId(subject.getId());

            Teacher teacher = new Teacher();
            TeacherDaoSQL teacherDaoSQL = new TeacherDaoSQL("Teacher");
            try {
                teacher = teacherDaoSQL.getTeacherByTeacherId(subject.getResponsibleTeacher());
            } catch (UniException e) {
                throw new RuntimeException(e);
            }

            GradeDaoSQL gradeDaoSQL = new GradeDaoSQL("Grade");
            Grade grade = gradeDaoSQL.getGradeById(student.getId(), subject.getId());



            textField3.setText(subject.getSemester());
            textField4.setText(String.valueOf(grade.getGrade()));
            textField5.setText(teacher.getName() + " " + teacher.getLastName());
            textField6.setText(String.valueOf(subject.getEcts()));
            textField7.setText(subject.getPrerequisite_subject());
            textField2.setText(String.valueOf(subject.getGodina()));
            textField8.setText("Prvi put");


            textField3.setEditable(false);
            textField4.setEditable(false);
            textField5.setEditable(false);
            textField6.setEditable(false);
            textField7.setEditable(false);
            textField2.setEditable(false);
            textField8.setEditable(false);

        }
    });
    nazadButton.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            WindowCreator.create_window_student(oldFrame,user);
        }
    });
        textField1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                String predmet = textField1.getText();
                SubjectDaoSQL subjectDaoSQL = new SubjectDaoSQL("Subject");
                Subject subject = new Subject();
                subject = subjectDaoSQL.getSubjectByName(predmet);
                DefaultListModel<Subject> lista = new DefaultListModel<>();
                lista.addElement(subject);
                list1.setModel(lista);
            }
        });
        ucitajPredmeteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                List<Subject> lista = new LinkedList<>();
                SubjectDaoSQL subjectDaoSQL = new SubjectDaoSQL("Subject");

                Student student = new Student();
                StudentDaoSQL studentDaoSQL = new StudentDaoSQL("Student");
                student = studentDaoSQL.getStudentByUserId(user);

                try {
                    lista = subjectDaoSQL.getAll();
                } catch (UniException e) {
                    throw new RuntimeException(e);
                }

                DefaultListModel<Subject> lista_model = new DefaultListModel<>();
                for (int i = 0; i < lista.size(); i++) {
                    try {
                        if(studentDaoSQL.isEnrolled(lista.get(i).getId(),student.getId())){
                            lista_model.addElement(lista.get(i));
                        }
                    } catch (UniException e) {
                        throw new RuntimeException(e);
                    }

                }
                List<Subject> listaReg = new LinkedList<>();
                try {
                    listaReg = subjectDaoSQL.getAllEnrolled(student);
                } catch (UniException e) {
                    throw new RuntimeException(e);
                }
                DefaultListModel<Subject> modelRegistrovani = new DefaultListModel<>();

                for(int i = 0; i<listaReg.size(); i++){
                    modelRegistrovani.addElement(listaReg.get(i));
                }

                list2.setModel(modelRegistrovani);
                list1.setModel(lista_model);
            }
        });
    }
}
