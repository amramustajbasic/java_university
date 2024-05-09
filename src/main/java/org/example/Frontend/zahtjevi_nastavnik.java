package org.example.Frontend;

import org.example.Dao.*;
import org.example.Entity.*;
import org.example.Exceptions.UniException;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedList;
import java.util.List;

public class zahtjevi_nastavnik {
    private JComboBox comboBox1;
    private JList list1;
    private JTextField textField1;
    private JButton privhatiButton;
    private JButton odbijButton;
    private JButton nazadButton;
    public JPanel panel_zahtj_nastavniku;
    private JTable table1;
    private JLabel ime_studenta;
    private JList list2;
    private JButton ucitajPodatkeButton;
    private JPanel donja_tabela;

    public zahtjevi_nastavnik(JFrame oldFrame, User user) {
    nazadButton.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent actionEvent) {
        WindowCreator.create_window_nastavnik(oldFrame,user);
        }
    });


        comboBox1.addPopupMenuListener(new PopupMenuListener() {
            @Override
            public void popupMenuWillBecomeVisible(PopupMenuEvent popupMenuEvent) {
                DefaultComboBoxModel<Subject> model = new DefaultComboBoxModel<>();
                comboBox1.setModel(model);
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
                for (int i = 0; i < lista.size(); i++) {
                    model.insertElementAt(lista.get(i), i);
                }
                comboBox1.setModel(model);
            }

            @Override
            public void popupMenuWillBecomeInvisible(PopupMenuEvent popupMenuEvent) {
                Subject subject = (Subject) comboBox1.getSelectedItem();
                List<Request> lista;
                RequestDaoSQL requestDaoSQL = new RequestDaoSQL("Request");
                if(subject == null){
                    JOptionPane.showMessageDialog(oldFrame, "Predmet nije izabran", "Upozorenje", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                try {
                    lista = requestDaoSQL.getAllBySubjectIDRequests_Slusanje(subject);
                    System.out.println(lista.size());
                } catch (UniException e) {
                    throw new RuntimeException(e);
                }
                DefaultListModel<Request> lista_model = new DefaultListModel<>();

                for (int i = 0; i < lista.size(); i++) {
                    lista_model.addElement(lista.get(i));
                }
                list1.setModel(lista_model);
            }

            @Override
            public void popupMenuCanceled(PopupMenuEvent popupMenuEvent) {

            }
        });
        list1.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent listSelectionEvent) {


            }
        });
        ucitajPodatkeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                //za donju listu
                DefaultTableModel tableModel = new DefaultTableModel(
                        new Object[]{"Ime i prezime","Predmet", "Odlusao", "Polozio", "Ocjena"}, 0);

                list2.removeAll();

                Request odabrani_zahtjev = (Request) list1.getSelectedValue();
                if(odabrani_zahtjev == null){
                    JOptionPane.showMessageDialog(oldFrame,"Nema zahtjeva!", "Info", JOptionPane.INFORMATION_MESSAGE);
                    return;
                }
                StudentDaoSQL studentDaoSQL = new StudentDaoSQL("Student");
                Student student_koji_salje = studentDaoSQL.getStudentById(odabrani_zahtjev.getStudent_id());
                //sad mi treba uspjeh iz svih preduslovnih predmeta
                SubjectDaoSQL subjectDaoSQL = new SubjectDaoSQL("Subject");
                List<Subject> lista;
                if(list1.isSelectionEmpty()){
                    JOptionPane.showMessageDialog(oldFrame,"Zahtjev nije odabran!", "Greska", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                try {
                    lista = subjectDaoSQL.getAllPassed(student_koji_salje);
                } catch (UniException e) {
                    throw new RuntimeException(e);
                }
                DefaultListModel<Subject> lista_model = new DefaultListModel<>();
                for(int i = 0 ; i < lista.size(); i++) {
                    lista_model.addElement(lista.get(i));
                }
                list2.setModel(lista_model);
                //za donju listu
                Subject predmet = (Subject) comboBox1.getSelectedItem();
                String preduslovi_str = predmet.getPrerequisite_subject();
                String[] preduslovi = preduslovi_str.split(", ");
                GradeDaoSQL gradeDaoSQL = new GradeDaoSQL("Grade");
                Grade ocjena;
                Subject predmet_za_ispis;
                for(int i = 0; i < preduslovi.length; i++){
                    predmet_za_ispis = subjectDaoSQL.getSubjectByName(preduslovi[i]);
                    ocjena = gradeDaoSQL.getGradeById(student_koji_salje.getId(), predmet_za_ispis.getId());
                    String polozio = "";
                    String odlusao = "";
                    String ocjena_str = "";
                    boolean odlusao_predmet;
                    int bodovi = gradeDaoSQL.getBodovi(student_koji_salje.getId(),predmet_za_ispis);
                    try {
                        odlusao_predmet = studentDaoSQL.isEnrolled(predmet_za_ispis.getId(), student_koji_salje.getId());
                        System.out.println(predmet.getId());
                        System.out.println(student_koji_salje.getId());
                    } catch (UniException e) {
                        throw new RuntimeException(e);
                    }
                    if(bodovi>0){
                        odlusao = "Da";
                    }

                    if(ocjena.getGrade()>=6){
                        polozio = "Da";
                        ocjena_str = String.valueOf(ocjena.getGrade());
                    }
                    tableModel.addRow(new Object[]{student_koji_salje.getName() + " " + student_koji_salje.getLastName(),predmet_za_ispis.getName(), odlusao, polozio, ocjena_str});
                }
                table1.setModel(tableModel);
            }
        });
        privhatiButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                Request zahtjev = (Request) list1.getSelectedValue();
                TeacherDaoSQL teacherDaoSQL = new TeacherDaoSQL("Teacher");
                EnrollmentDaoSQL enrollmentDaoSQL = new EnrollmentDaoSQL("Enrollment");
                Teacher teacher;
                if(list1.isSelectionEmpty()){
                    JOptionPane.showMessageDialog(oldFrame,"Zahtjev nije odabran!", "Greska", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                try {
                     teacher = teacherDaoSQL.getTeacherById(user.getId());
                } catch (UniException e) {
                    throw new RuntimeException(e);
                }
                //obrisat zahtjev i dodat studenta na predmet
                String poruka_nastavnika = textField1.getText();
                RequestDaoSQL requestDaoSQL = new RequestDaoSQL("Request");
                Request odgovor_profesora = new Request();
                odgovor_profesora.setMessage(poruka_nastavnika);
                odgovor_profesora.setStatus("Odgovor_slusanje");
                odgovor_profesora.setTeacher_id(teacher.getId());
                odgovor_profesora.setStudent_id(zahtjev.getStudent_id());
                odgovor_profesora.setSubject_id(zahtjev.getSubject_id());
                odgovor_profesora.setVice_dean_id(zahtjev.getVice_dean_id());
                int new_id;
                try {
                    new_id = requestDaoSQL.getMaxRequestId();
                } catch (UniException e) {
                    throw new RuntimeException(e);
                }
                ++new_id;
                try {
                    enrollmentDaoSQL.enrollStudent(zahtjev);
                    requestDaoSQL.addRequest(odgovor_profesora,new_id);
                    requestDaoSQL.deleteRequest(zahtjev);
                } catch (UniException e) {
                    throw new RuntimeException(e);
                }
                JOptionPane.showMessageDialog(oldFrame,"Odgovor poslan", "Uspjeh", JOptionPane.INFORMATION_MESSAGE);
            }
        });
        odbijButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                Request zahtjev = (Request) list1.getSelectedValue();
                TeacherDaoSQL teacherDaoSQL = new TeacherDaoSQL("Teacher");
                Teacher teacher;
                if(list1.isSelectionEmpty()){
                    JOptionPane.showMessageDialog(oldFrame,"Zahtjev nije odabran!", "Greska", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                try {
                    teacher = teacherDaoSQL.getTeacherById(user.getId());
                } catch (UniException e) {
                    throw new RuntimeException(e);
                }
                //obrisat zahtjev i dodat studenta na predmet
                String poruka_nastavnika = textField1.getText();
                RequestDaoSQL requestDaoSQL = new RequestDaoSQL("Request");
                Request odgovor_profesora = new Request();
                odgovor_profesora.setMessage(poruka_nastavnika);
                odgovor_profesora.setStatus("Odgovor_slusanje");
                odgovor_profesora.setTeacher_id(teacher.getId());
                odgovor_profesora.setStudent_id(zahtjev.getStudent_id());
                odgovor_profesora.setSubject_id(zahtjev.getSubject_id());
                odgovor_profesora.setVice_dean_id(zahtjev.getVice_dean_id());
                int new_id;
                try {
                    new_id = requestDaoSQL.getMaxRequestId();
                } catch (UniException e) {
                    throw new RuntimeException(e);
                }
                ++new_id;
                try {

                    requestDaoSQL.addRequest(odgovor_profesora,new_id);
                    requestDaoSQL.deleteRequest(zahtjev);
                } catch (UniException e) {
                    throw new RuntimeException(e);
                }
                JOptionPane.showMessageDialog(oldFrame,"Odgovor poslan", "Uspjeh", JOptionPane.INFORMATION_MESSAGE);

            }
        });
    }


}
