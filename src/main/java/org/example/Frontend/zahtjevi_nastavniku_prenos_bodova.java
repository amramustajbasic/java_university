package org.example.Frontend;

import org.example.Dao.*;
import org.example.Entity.*;
import org.example.Exceptions.UniException;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedList;
import java.util.List;

public class zahtjevi_nastavniku_prenos_bodova {
    private JComboBox comboBox1;
    private JList list1;
    private JButton odbijButton;
    private JButton prihvatiButton;
    private JButton nazadButton;
    public JPanel panel_zah_bod_nas;
    private JTextField textField1;
    private JList list2;

    public zahtjevi_nastavniku_prenos_bodova(JFrame oldFrame, User user) {
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
                    lista = requestDaoSQL.getAllBySubjectIDRequests_Bodovi(subject);
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
        odbijButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                Request zahtjev = (Request) list1.getSelectedValue();
                TeacherDaoSQL teacherDaoSQL = new TeacherDaoSQL("Teacher");
                Teacher teacher;
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
        prihvatiButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                Request zahtjev = (Request) list1.getSelectedValue();
                TeacherDaoSQL teacherDaoSQL = new TeacherDaoSQL("Teacher");
                EnrollmentDaoSQL enrollmentDaoSQL = new EnrollmentDaoSQL("Enrollment");

                Teacher teacher;
                try {
                    teacher = teacherDaoSQL.getTeacherById(user.getId());
                } catch (UniException e) {
                    throw new RuntimeException(e);
                }
                String poruka_nastavnika = textField1.getText();
                RequestDaoSQL requestDaoSQL = new RequestDaoSQL("Request");
                Request odgovor_profesora = new Request();
                odgovor_profesora.setMessage(poruka_nastavnika);
                odgovor_profesora.setStatus("Odgovor_prenos");
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
                   // enrollmentDaoSQL.enrollStudent(zahtjev);
                    requestDaoSQL.addRequest(odgovor_profesora,new_id);
                    requestDaoSQL.deleteRequest(zahtjev);


                } catch (UniException e) {
                    throw new RuntimeException(e);
                }
                JOptionPane.showMessageDialog(oldFrame,"Odgovor poslan", "Uspjeh", JOptionPane.INFORMATION_MESSAGE);
            }

        });
        list1.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent listSelectionEvent) {
                Subject subject = (Subject) comboBox1.getSelectedItem();
                List<Request> lista;
                RequestDaoSQL requestDaoSQL = new RequestDaoSQL("Request");
                Request zahtjev = (Request) list1.getSelectedValue();
                if(zahtjev == null){
                    JOptionPane.showMessageDialog(oldFrame, "Zahtjev nije izabran", "Upozorenje", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                DefaultListModel<String> lista_model = new DefaultListModel<>();
                lista_model.addElement(zahtjev.toResult());
                list2.setModel(lista_model);

            }
        });
    }
}
