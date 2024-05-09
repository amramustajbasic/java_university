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
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedList;
import java.util.List;

public class tekucaSpisakStudenata {
    public JPanel panel_studenti_naredna;
    private JList list1;
    private JButton nazadButton;
    private JTable table1;
    private JButton ucitajPredmeteButton;

    public tekucaSpisakStudenata(JFrame oldFrame, User user){
        TeacherDaoSQL pretraga_nastavnika = new TeacherDaoSQL("Teacher");
        Teacher nastavnik;
        try {
            nastavnik = pretraga_nastavnika.getTeacherById(user.getId());

        } catch (UniException e) {
            throw new RuntimeException(e);
        }

        nazadButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                WindowCreator.create_window_nastavnik(oldFrame,user);
            }
        });
        list1.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent listSelectionEvent) {
                DefaultTableModel tableModel = new DefaultTableModel(
                        new String[]{"", "","", "", ""}, 0);
                tableModel.addRow(new Object[]{"Student", "Godina", "Status studenta", "Status na predmetu", "Predispitne aktivnosti"});
                if(list1.isSelectionEmpty()){
                    JOptionPane.showMessageDialog(oldFrame,"Predmet nije odabran!", "Greska", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                StudentDaoSQL studentDaoSQL = new StudentDaoSQL("Student");
                List<Student> lista_studenata;

                Subject predmet = (Subject) list1.getSelectedValue();
                GradeDaoSQL gradeDaoSQL = new GradeDaoSQL("Grade");
                try {
                    lista_studenata = studentDaoSQL.getAllStudentsForNextYear(nastavnik, (Subject) list1.getSelectedValue());
                } catch (UniException e) {
                    throw new RuntimeException(e);
                }
                for (int i = 0; i < lista_studenata.size(); i++){
                    tableModel.addRow(new String[]{lista_studenata.get(i).getName() + " " +lista_studenata.get(i).getLastName(),
                            String.valueOf(lista_studenata.get(i).getYearOfStudy()),
                            lista_studenata.get(i).getStatus(), "Prenos",
                            String.valueOf(gradeDaoSQL.getBodovi(lista_studenata.get(i).getId(), predmet))});
                }
                table1.setModel(tableModel);

            }
        });
        ucitajPredmeteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                DefaultComboBoxModel<Subject> model = new DefaultComboBoxModel<>();
                List<Subject> lista = new LinkedList<>();
                DefaultListModel<Subject> lista_model = new DefaultListModel<>();
                SubjectDaoSQL subjectDaoSQL = new SubjectDaoSQL("Subject");

                try {
                    lista = subjectDaoSQL.getAllProfesorsSubjects(nastavnik.getId());
                } catch (UniException e) {
                    throw new RuntimeException(e);
                }
                for (int i = 0; i < lista.size(); i++) {

                    lista_model.addElement(lista.get(i));
                }
                list1.setModel(lista_model);
            }
        });
    }



}
