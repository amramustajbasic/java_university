package org.example.Frontend;

import org.example.Dao.*;
import org.example.Entity.*;
import org.example.Exceptions.UniException;

import javax.swing.*;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedList;
import java.util.List;

public class PrenosBodova {
    public JPanel panel_prenos_bodova;
    private JComboBox comboBox1;
    private JButton posaljiZahtjevButton;
    private JButton nazadButton;

    public PrenosBodova(JFrame oldFrame, User user) {
    posaljiZahtjevButton.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent actionEvent) {

        }
    });
    nazadButton.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            WindowCreator.create_window_student(oldFrame,user);
        }
    });
    comboBox1.addPopupMenuListener(new PopupMenuListener() {
        @Override
        public void popupMenuWillBecomeVisible(PopupMenuEvent popupMenuEvent) {
            Subject subject = new Subject();
            SubjectDaoSQL subjectDaoSQL = new SubjectDaoSQL("Subject");

            DefaultComboBoxModel<Subject> model = new DefaultComboBoxModel<>();

            Student student = new Student();
            StudentDaoSQL studentDaoSQL = new StudentDaoSQL("Student");
            student = studentDaoSQL.getStudentByUserId(user);

            List<Subject> subjects = new LinkedList<>();
            List<Subject> filtrirano = new LinkedList<>();
            try {
                subjects = subjectDaoSQL.getAll();
            } catch (UniException e) {
                throw new RuntimeException(e);
            }

            for(int i = 0; i < subjects.size(); i++) {
                try {
                    if (!(subjectDaoSQL.isPassed(student,subjects.get(i))) && studentDaoSQL.isEnrolled(subjects.get(i).getId(), student.getId()) ){
                        filtrirano.add(subjects.get(i));
                    }
                } catch (UniException e) {
                    throw new RuntimeException(e);
                }
            }

            for (int i = 0; i < filtrirano.size(); i++) {
                model.insertElementAt(filtrirano.get(i),i);
            }

            comboBox1.setModel(model);
        }

        @Override
        public void popupMenuWillBecomeInvisible(PopupMenuEvent popupMenuEvent) {

        }

        @Override
        public void popupMenuCanceled(PopupMenuEvent popupMenuEvent) {

        }
    });
    posaljiZahtjevButton.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent actionEvent) {

            Subject subject = new Subject();
            subject = (Subject) comboBox1.getSelectedItem();

            Teacher teacher = new Teacher();
            TeacherDaoSQL teacherDaoSQL = new TeacherDaoSQL("Teacher");

            Student student = new Student();
            StudentDaoSQL studentDaoSQL = new StudentDaoSQL("Student");
            student = studentDaoSQL.getStudentByUserId(user);

            String message = subject.zelimPrenijeti();
            String status = "Upit_prenos";

            User prodekan = new User();
            UserDaoSQL userDaoSQL = new UserDaoSQL("User");
            prodekan = userDaoSQL.getOldProdekan();

            try {
                teacher = teacherDaoSQL.getTeacherByTeacherId(subject.getResponsibleTeacher());
            } catch (UniException e) {
                throw new RuntimeException(e);
            }

            Request request = new Request();
            RequestDaoSQL requestDaoSQL = new RequestDaoSQL("Request");

            try {
                requestDaoSQL.addRequestStatus(request,student.getId(), subject.getId(),message,status, teacher.getId(), prodekan.getId());
            } catch (UniException e) {
                throw new RuntimeException(e);
            }

            int ans = JOptionPane.showConfirmDialog(oldFrame, "Uspjesno poslan zahtjev za prenos bodova iz predmeta "+subject.getName() +"\n Zelite li nazad?");
            if(ans == JOptionPane.YES_OPTION){
                WindowCreator.create_window_student(oldFrame,user);
            }
            if(ans == JOptionPane.NO_OPTION){
                JOptionPane.showMessageDialog(oldFrame, "Nastavite sa radom ;)" , "Informativno", JOptionPane.INFORMATION_MESSAGE);
            }
        }
    });
}
}
