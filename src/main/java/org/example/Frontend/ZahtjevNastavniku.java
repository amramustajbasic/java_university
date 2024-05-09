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

public class ZahtjevNastavniku extends JFrame {
    public JPanel panelZahtjevNastavniku;
    private JButton pošaljiZahtjevButton;
    private JTextArea textArea1;
    private JComboBox comboBox1;
    private JComboBox comboBox2;
    private JButton odaberiButton;
    private JButton nazadButton;

    private SubjectDaoSQL subjectDaoSQL = new SubjectDaoSQL("Subject");
    private EnrollmentDaoSQL enrollmentDaoSQL = new EnrollmentDaoSQL("Enrollment");
    private RequestDaoSQL requestDaoSQL = new RequestDaoSQL("Request");

    public ZahtjevNastavniku(JFrame oldFrame, User user){

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

                StudentDaoSQL studentDaoSQL = new StudentDaoSQL("Student");
                Student student = studentDaoSQL.getStudentByUserId(user);

                DefaultComboBoxModel<Subject> model = new DefaultComboBoxModel<>();

                Teacher teacher = (Teacher) comboBox2.getSelectedItem();
                List<Subject> subjects = new LinkedList<>();
                List<Subject> teacherSubject = new LinkedList<>();
                if(teacher == null){
                    try {
                        subjects = subjectDaoSQL.getAllUnpassed(student);
                    } catch (UniException e) {
                        throw new RuntimeException(e);
                    }
                    for (int i = 0; i < subjects.size(); i++) {
                        model.insertElementAt(subjects.get(i), i);
                    }
                }if(teacher!=null) {
                    try {
                        teacherSubject = subjectDaoSQL.getAllProfesorsSubjects(teacher.getId());
                    } catch (UniException e) {
                        throw new RuntimeException(e);
                    }
                    for (int i = 0; i < teacherSubject.size(); i++) {
                        model.insertElementAt(teacherSubject.get(i), i);
                    }
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
        comboBox2.addPopupMenuListener(new PopupMenuListener() {
            @Override
            public void popupMenuWillBecomeVisible(PopupMenuEvent popupMenuEvent) {
                Teacher teacher = new Teacher();
                TeacherDaoSQL teacherDaoSQL = new TeacherDaoSQL("Teacher");

                DefaultComboBoxModel<Teacher> model = new DefaultComboBoxModel<>();

                Subject subject = (Subject)comboBox1.getSelectedItem();
                List<Teacher> teachers = new LinkedList<>();


                if(subject == null){
                    try {
                        teachers = teacherDaoSQL.getAll();
                    } catch (UniException e) {
                        throw new RuntimeException(e);
                    }
                    for (int i = 0; i < teachers.size(); i++) {
                        model.insertElementAt(teachers.get(i),i);
                    }
                }
                if(subject != null){
                    teacher = teacherDaoSQL.getTeacherBySubject(subject.getId());
                    teachers.add(teacher);
                    model.insertElementAt(teacher,0);
                }

                comboBox2.setModel(model);
            }

            @Override
            public void popupMenuWillBecomeInvisible(PopupMenuEvent popupMenuEvent) {

            }

            @Override
            public void popupMenuCanceled(PopupMenuEvent popupMenuEvent) {

            }
        });


        odaberiButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                Subject subject = (Subject) comboBox1.getSelectedItem();
                Teacher teacher = (Teacher) comboBox2.getSelectedItem();

                if(teacher == null || subject == null) {
                    JOptionPane.showMessageDialog(oldFrame,"Morate izabrati predmet i nastavnika iz padajuceg menija", "Upozorenje", JOptionPane.WARNING_MESSAGE);
                }else{
                    if (subject.getResponsibleTeacher() == teacher.getId()) {
                        JOptionPane.showMessageDialog(oldFrame, "Za " + subject.getName() + " je odgovoran/na " + teacher.getName() + " " + teacher.getLastName(), "Informativno", JOptionPane.INFORMATION_MESSAGE);
                        return;
                    } else {
                        JOptionPane.showMessageDialog(oldFrame, "Morate odabrati nastavnika koji je odgovoran za predmet koji zelite", "Upozorenje", JOptionPane.WARNING_MESSAGE);
                        return;
                    }
                }
            }
        });
        pošaljiZahtjevButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {



                String poruka = textArea1.getText();
                Subject subject = (Subject) comboBox1.getSelectedItem();
                Teacher teacher = (Teacher) comboBox2.getSelectedItem();
                StudentDaoSQL studentDaoSQL = new StudentDaoSQL("Student");
                Student student = studentDaoSQL.getStudentByUserId(user);



                List<Subject> list = new LinkedList<>();
                List<Request> zahtjevi = new LinkedList<>();
                try {
                    list = subjectDaoSQL.getAllEnrolled(student);
                    zahtjevi = requestDaoSQL.getAllByStudent(student.getId());
                } catch (UniException e) {
                    throw new RuntimeException(e);
                }
                int brojRegistrovanihPredmeta = list.size();
                int brojZahtjeva = zahtjevi.size();

                if((brojRegistrovanihPredmeta + brojZahtjeva) > 10){
                    JOptionPane.showMessageDialog(oldFrame,"Ne mozete vise zahtjeva poslati");
                    return;
                }


                if(teacher == null || subject == null) {
                    JOptionPane.showMessageDialog(oldFrame, "Morate izabrati predmet i nastavnika iz padajuceg menija", "Upozorenje", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                if(poruka.length()==0){
                    JOptionPane.showMessageDialog(oldFrame, "Ne mozete poslati upit nastavniku bez obrazlozenja", "Upozorenje", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                if (subject.getResponsibleTeacher() == teacher.getId()) {
                        RequestDaoSQL requestDaoSQL = new RequestDaoSQL("Request");
                        Request request = new Request();

                        UserDaoSQL userDaoSQL = new UserDaoSQL("User");
                        User prodekanUser = new User();
                        prodekanUser = userDaoSQL.getOldProdekan();

                        try {
                            requestDaoSQL.addRequestStatus(request, student.getId(), subject.getId(), poruka, "Upit_slusanje", subject.getResponsibleTeacher(), prodekanUser.getId());
                        } catch (UniException e) {
                            throw new RuntimeException(e);
                        }
                        JOptionPane.showMessageDialog(oldFrame, "Zahtjev uspjesno poslan", "Uspjeh", JOptionPane.INFORMATION_MESSAGE);
                    }
                textArea1.setText("");
                WindowCreator.create_window_zahtjev_nastavniku(oldFrame,user);
            }



        });
    }
}
