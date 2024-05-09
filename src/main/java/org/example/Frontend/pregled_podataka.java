package org.example.Frontend;

import javax.swing.*;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedList;
import java.util.List;
import org.example.Dao.SubjectDaoSQL;
import org.example.Dao.StudentDaoSQL;
import org.example.Dao.TeacherDaoSQL;
import org.example.Entity.*;
import org.example.Exceptions.UniException;

public class pregled_podataka {
    private JComboBox comboBox1;
    private JList list1;
    private JButton nazadButton;
    public JPanel pregled_podataka;
    private JTextField textField1;

    public pregled_podataka(JFrame oldFrame) {
    comboBox1.addPopupMenuListener(new PopupMenuListener() {
        @Override
        public void popupMenuWillBecomeVisible(PopupMenuEvent popupMenuEvent) {
            DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>();
            model.addElement("Studenti");
            model.addElement("Nastavnici");
            model.addElement("Predmeti");
            comboBox1.setModel(model);
        }

        @Override
        public void popupMenuWillBecomeInvisible(PopupMenuEvent popupMenuEvent) {
            if(comboBox1.getSelectedItem().equals("Studenti")) {
                DefaultListModel<Student> studenti = new DefaultListModel<>();
                StudentDaoSQL studentDaoSQL = new StudentDaoSQL("Student");
                List<Student> lista_studenata;
                try {
                    lista_studenata = studentDaoSQL.getAll();
                } catch (UniException e) {
                    throw new RuntimeException(e);
                }
                for (int i = 0; i < lista_studenata.size(); i++) {
                    studenti.addElement(lista_studenata.get(i));
                }
                list1.setModel(studenti);
            }
            if(comboBox1.getSelectedItem().equals("Nastavnici")) {
                DefaultListModel<Teacher> nastavnici = new DefaultListModel<>();
                TeacherDaoSQL teacherDaoSQL = new TeacherDaoSQL("Teacher");
                List<Teacher> lista_nastavnika;
                try {
                    lista_nastavnika = teacherDaoSQL.getAll();
                } catch (UniException e) {
                    throw new RuntimeException(e);
                }
                for (int i = 0; i < lista_nastavnika.size(); i++) {
                    nastavnici.addElement(lista_nastavnika.get(i));
                }
                list1.setModel(nastavnici);
            }
            if(comboBox1.getSelectedItem().equals("Predmeti")) {
                DefaultListModel<Subject> predmeti = new DefaultListModel<>();
                SubjectDaoSQL studentDaoSQL = new SubjectDaoSQL("Subject");
                List<Subject> lista_predmeta;
                try {
                    lista_predmeta = studentDaoSQL.getAll();
                } catch (UniException e) {
                    throw new RuntimeException(e);
                }
                for (int i = 0; i < lista_predmeta.size(); i++) {
                    predmeti.addElement(lista_predmeta.get(i));
                }
                list1.setModel(predmeti);
            }
        }

        @Override
        public void popupMenuCanceled(PopupMenuEvent popupMenuEvent) {

        }
    });
        nazadButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                WindowCreator.create_window_admin(oldFrame);
            }
        });
        textField1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if(comboBox1.getSelectedItem().equals("Nastavnici")) {
                    DefaultListModel<Teacher> nastavnici = new DefaultListModel<>();
                    TeacherDaoSQL teacherDaoSQL = new TeacherDaoSQL("Teacher");
                    List<Teacher> lista_nastavnika = new LinkedList<>();
                    lista_nastavnika = teacherDaoSQL.getTeacherLike(textField1.getText());
                    for (int i = 0; i < lista_nastavnika.size(); i++) {
                        nastavnici.addElement(lista_nastavnika.get(i));
                        list1.setModel(nastavnici);
                    }
                } else if(comboBox1.getSelectedItem().equals("Studenti")) {
                    DefaultListModel<Student> studenti = new DefaultListModel<>();
                    StudentDaoSQL studentDaoSQL = new StudentDaoSQL("Student");
                    List<Student> lista_studenata = new LinkedList<>();
                    lista_studenata = studentDaoSQL.getStudentLike(textField1.getText());
                    for (int i = 0; i < lista_studenata.size(); i++) {
                        studenti.addElement(lista_studenata.get(i));
                        list1.setModel(studenti);
                    }
                }
                else if(comboBox1.getSelectedItem().equals("Predmeti")) {
                    DefaultListModel<Subject> predmeti = new DefaultListModel<>();
                    SubjectDaoSQL subjectDaoSQL = new SubjectDaoSQL("Subject");
                    List<Subject> lista_predmeta = new LinkedList<>();
                    try {
                        lista_predmeta = subjectDaoSQL.getSubjectLike(textField1.getText());
                    } catch (UniException e) {
                        throw new RuntimeException(e);
                    }
                    for (int i = 0; i < lista_predmeta.size(); i++) {
                        predmeti.addElement(lista_predmeta.get(i));
                        list1.setModel(predmeti);
                    }
                }
            }
        });
    }
}
