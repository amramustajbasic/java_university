package org.example.Frontend;

import org.example.Dao.GradeDaoSQL;
import org.example.Dao.StudentDaoSQL;
import org.example.Dao.SubjectDaoSQL;
import org.example.Dao.TeacherDaoSQL;
import org.example.Entity.*;
import org.example.Exceptions.UniException;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.LinkedList;
import java.util.List;

public class tekucaSpisakPredmeta {
    public JPanel panel_tekuca_spisak;
    private JButton ucitajPredmeteButton;
    private JList list1;
    private JList list2;
    private JButton ucitajStudenteButton;
    private JTextField ovdjeOcjenaTextField;
    private JButton potvrdiButton;
    private JButton odustaniButton;
    private JLabel ime_stud;
    private JLabel ocjena_studenta;
    private JLabel student_polje;
    private JLabel ocjena_polje;
    private JLabel bodovi_polje;
    private JLabel bodovi;

    public tekucaSpisakPredmeta(JFrame oldFrame, User user) {
    odustaniButton.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent actionEvent) {
        WindowCreator.create_window_nastavnik(oldFrame,user);
        }
    });
    potvrdiButton.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            int nova_ocj = Integer.valueOf(ovdjeOcjenaTextField.getText());
            Grade ocjena = new Grade();
            ocjena.setGrade(nova_ocj);
            Subject predmet_studenta = (Subject)list1.getSelectedValue();
            Student odabrani_stud = (Student)list2.getSelectedValue();
            GradeDaoSQL gradeDaoSQL = new GradeDaoSQL("Grade");
            if(ocjena_studenta.getText().equals("Nije ocjenjen!")){
                gradeDaoSQL.addGrade(ocjena,odabrani_stud,predmet_studenta);
                JOptionPane.showMessageDialog(oldFrame,"Uspjesno dodana ocjena", "Usjpeh", JOptionPane.INFORMATION_MESSAGE);
            }else {
                gradeDaoSQL.updateGrade(ocjena,odabrani_stud,predmet_studenta);
                JOptionPane.showMessageDialog(oldFrame,"Uspjesno azurirana ocjena", "Usjpeh", JOptionPane.INFORMATION_MESSAGE);
            }
        }
    });
    ucitajPredmeteButton.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>();
            list1.setModel(model);
            List<Subject> lista = new LinkedList<>();
            SubjectDaoSQL subjectDaoSQL = new SubjectDaoSQL("Subject");
            TeacherDaoSQL teacherDaoSQL = new TeacherDaoSQL("Teacher");
            Teacher nastavnik = new Teacher();
            try {
               nastavnik = teacherDaoSQL.getTeacherById(user.getId());
            } catch (UniException e) {
                throw new RuntimeException(e);
            }
            try {
                lista = subjectDaoSQL.getAllProfesorsSubjects(nastavnik.getId());
            } catch (UniException e) {
                throw new RuntimeException(e);
            }
            DefaultListModel<Subject> lista_model = new DefaultListModel<>();
            for (int i = 0; i < lista.size(); i++) {
                lista_model.addElement(lista.get(i));
            }
            list1.setModel(lista_model);
        }


    });
    ucitajStudenteButton.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            list1.removeAll();
            Subject predmet = (Subject) list1.getSelectedValue();
            List<Student> lista = new LinkedList<>();
            StudentDaoSQL studentDaoSQL = new StudentDaoSQL("Student");
            if(list1.isSelectionEmpty()){
                JOptionPane.showMessageDialog(oldFrame,"Predmet nije odabran!", "Greska", JOptionPane.WARNING_MESSAGE);
                return;
            }
            try {

                lista = studentDaoSQL.getAllEnrolled(predmet.getId());
            } catch (UniException e) {
                throw new RuntimeException(e);
            }
            DefaultListModel<Student> lista_model = new DefaultListModel<>();
            for(int i = 0 ; i < lista.size(); i++) {
                lista_model.addElement(lista.get(i));
            }
            list2.setModel(lista_model);
        }

    });

        list2.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent listSelectionEvent) {
                Grade ocjena_stud = new Grade();
                Subject predmet_studenta = (Subject)list1.getSelectedValue();
                Student odabrani_stud = (Student)list2.getSelectedValue();
                GradeDaoSQL gradeDaoSQL = new GradeDaoSQL("Grade");
                String student = "";
                String ocjena_str = "";
                String bodovi_str = "";
                if(!list2.isSelectionEmpty()){
                    student = odabrani_stud.toString();
                    ocjena_stud = gradeDaoSQL.getGradeById(odabrani_stud.getId(),predmet_studenta.getId());
                }
                if(ocjena_stud.getId()!=0) {
                    ocjena_str = String.valueOf(ocjena_stud.getGrade());
                    bodovi_str = String.valueOf(gradeDaoSQL.getBodovi(odabrani_stud.getId(),predmet_studenta));

                }else{
                    ocjena_str = "Nije ocjenjen!";
                }
                ime_stud.setText(student);
                ocjena_studenta.setText(ocjena_str);
                bodovi.setText(bodovi_str);
                ocjena_polje.setText("Ocjena:");
                student_polje.setText("Student:");
                bodovi_polje.setText("Bodovi");
            }
        });
    }
}
