package org.example.Frontend;

import org.example.Dao.*;
import org.example.Entity.*;
import org.example.Exceptions.UniException;

import javax.swing.*;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

public class AzuriranjePredmeta {
    public JPanel panel_zahtjev_prodekanu;
    private JButton pošaljiZahtjevButton;
    private JComboBox comboBox1;
    private JComboBox comboBox2;
    private JButton zamijeniButton;
    private JTextArea textArea1;
    private JButton nazadButton;

    //public Subject prvipredmet;

    public AzuriranjePredmeta(JFrame oldFrame, User user) {
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

                Student student = new Student();
                StudentDaoSQL studentDaoSQL = new StudentDaoSQL("Student");
                student = studentDaoSQL.getStudentByUserId(user);

                DefaultComboBoxModel<Subject> model = new DefaultComboBoxModel<>();

                List<Subject> subjects = new LinkedList<>();

                try {
                    subjects = subjectDaoSQL.getAll();
                } catch (UniException e) {
                    throw new RuntimeException(e);
                }


                List<Subject> predmetiKojeImam = new LinkedList<>();
                try {
                    predmetiKojeImam = subjectDaoSQL.getAllEnrolled(student);
                } catch (UniException e) {
                    throw new RuntimeException(e);
                }


                for (int i = 0; i < predmetiKojeImam.size(); i++) {
                    model.insertElementAt(predmetiKojeImam.get(i),i);
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
                Subject subject = new Subject();
                SubjectDaoSQL subjectDaoSQL = new SubjectDaoSQL("Subject");

                DefaultComboBoxModel<Subject> model = new DefaultComboBoxModel<>();

                Student student = new Student();
                StudentDaoSQL studentDaoSQL = new StudentDaoSQL("Student");
                student = studentDaoSQL.getStudentByUserId(user);

                Subject predmetPreduslov = new Subject();
//
                List<Subject> subjects = new LinkedList<>();
                List<Subject> filterSubjects = new LinkedList<>();


                try {
                    subjects = subjectDaoSQL.getAll();
                } catch (UniException e) {
                    throw new RuntimeException(e);
                }
                String preduslovi_str = new String("");
                String[] predusloviArray = null;
                for (int i = 0; i < subjects.size(); i++) {
                    Subject predmet2 = subjects.get(i);
                    preduslovi_str = predmet2.getPrerequisite_subject();
                    predusloviArray = preduslovi_str.split(", ");
                    try {
                        if (!(studentDaoSQL.isEnrolled(predmet2.getId(), student.getId()))) {
                            if (preduslovi_str.length() > 5) {
                                for (int j = 0; j < predusloviArray.length; j++) {
                                    predmetPreduslov = subjectDaoSQL.getSubjectByName(predusloviArray[j]);
                                    if (predmetPreduslov.getGodina() == predmet2.getGodina()) {//OOP.2 == SP.2
                                        continue;
                                    }
                                    if (!(subjectDaoSQL.isPassed(student, predmetPreduslov))) {
                                        //System.out.println("Nepolozen preduslov za " + predmet2.getName() + " je " + predusloviArray[j]);
//                                        filterSubjects.add(predmet2);
                                        subjects.remove(i);
                                        break;
                                    }
                                }
                                filterSubjects.add(predmet2);
                            }
                        }
                    } catch (UniException e) {
                        throw new RuntimeException(e);
                    }
                }


                for (int i = 0; i < filterSubjects.size(); i++) {
                    model.insertElementAt(filterSubjects.get(i),i);
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
        zamijeniButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                Subject subject1 = (Subject) comboBox1.getSelectedItem();
                Subject subject2 = (Subject) comboBox2.getSelectedItem();
                if(subject1 == null || subject2 == null) {
                    JOptionPane.showMessageDialog(oldFrame, "Predmeti nisu odabrani pravilno","Upozorenje", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                if(subject1.getId() == subject2.getId()){
                    JOptionPane.showMessageDialog(oldFrame, "Ne mozete odabrati iste predmete", "Upozorenje", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                Student student = new Student();
                StudentDaoSQL studentDaoSQL = new StudentDaoSQL("Student");
                student = studentDaoSQL.getStudentByUserId(user);

                RequestDaoSQL requestDaoSQL = new RequestDaoSQL("Request");
                requestDaoSQL.insertChangeSubjects(subject1.getId(), subject2.getId(),student.getId());

            }
        });
        pošaljiZahtjevButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {

                Student student = new Student();
                StudentDaoSQL studentDaoSQL = new StudentDaoSQL("Student");
                student = studentDaoSQL.getStudentByUserId(user);

                Subject subject = (Subject) comboBox1.getSelectedItem();
                Subject subject1 = (Subject) comboBox2.getSelectedItem();

                RequestDaoSQL requestDaoSQL = new RequestDaoSQL("Request");

                if(requestDaoSQL.studentSentRequestViceDean(student.getId())){
                    JOptionPane.showMessageDialog(oldFrame,"Vi ste prodekanu poslali zahtjev. \n Ne mozete ponovo");
                    return;
                }

                String areaText = textArea1.getText();

                if(comboBox1.getSelectedItem() == null || comboBox2.getSelectedItem() == null){
                    JOptionPane.showMessageDialog(oldFrame, "Predmeti nisu odabrani pravilno","Upozorenje", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                if(areaText.length() < 10){
                    JOptionPane.showMessageDialog(oldFrame, "Vase obrazlozenje mora imati min 10 karaktera");
                    return;
                }

                String zahtjev = "Da li ja mogu predmet " + subject.getName() + " zamijeniti za " + subject1.getName()+"?" +"\nRazlog zamjene: " + areaText;

                Request request = new Request();

                UserDaoSQL userDaoSQL = new UserDaoSQL("User");
                User prodekanUser = new User();
                prodekanUser = userDaoSQL.getOldProdekan();


                try {
                    requestDaoSQL.addRequestStatus(request,student.getId(),subject1.getId(),zahtjev,"Upit_zamjena",subject1.getResponsibleTeacher(), prodekanUser.getId());
                } catch (UniException e) {
                    throw new RuntimeException(e);
                }

                textArea1.setText("");
                WindowCreator.create_window_zahtjev_prodekanu(oldFrame,user);


            }
        });
        nazadButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                WindowCreator.create_window_student(oldFrame,user);
            }
        });
    }
}
