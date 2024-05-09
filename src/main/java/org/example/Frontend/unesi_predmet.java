package org.example.Frontend;

import org.example.Dao.SubjectDaoSQL;
import org.example.Dao.TeacherDaoSQL;
import org.example.Entity.Subject;
import org.example.Entity.Teacher;
import org.example.Exceptions.UniException;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedList;
import java.util.List;

public class unesi_predmet {
    public JPanel panel_unesi_predmet;
    private JTextField textField1;
    private JTextField textField2;
    private JButton potvrdiButton;
    private JButton nazadButton;
    private JTextField textField3;
    private JTextField textField4;
    private JTextField textField5;
    private JTextField textField6;
    private JRadioButton ljetniRadioButton;
    private JRadioButton zimskiRadioButton;
    private JList list1;
    private JRadioButton iRadioButton;
    private JRadioButton IIRadioButton;
    private JRadioButton IIIRadioButton;
    private JRadioButton IVRadioButton;
    private JList list2;
    private JList list3;
    private JButton dodajButton;

    public unesi_predmet(JFrame oldFrame) {
        List<Subject> lista_preduslova = new LinkedList<>();
        DefaultListModel<Subject> lista_preduslova_za_ispis = new DefaultListModel<>();
        ButtonGroup godine= new ButtonGroup();
        godine.add(iRadioButton);
        godine.add(IIIRadioButton);
        godine.add(IIIRadioButton);
        godine.add(IVRadioButton);
        ButtonGroup semstar = new ButtonGroup();
        semstar.add(ljetniRadioButton);
        semstar.add(zimskiRadioButton);
        nazadButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                WindowCreator.create_window_admin(oldFrame);

            }
        });
        potvrdiButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                try {
                    TeacherDaoSQL teacherDaoSQL = new TeacherDaoSQL("Teacher");
                    SubjectDaoSQL subjectDaoSQL = new SubjectDaoSQL("Subject");
                    if(!(zimskiRadioButton.isSelected() || ljetniRadioButton.isSelected())){
                        JOptionPane.showMessageDialog(oldFrame, "Ispunite sva polja!", "Greska", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    if(!(iRadioButton.isSelected() || IIIRadioButton.isSelected() || IIIRadioButton.isSelected() || IVRadioButton.isSelected())) {
                        JOptionPane.showMessageDialog(oldFrame, "Ispunite sva polja!", "Greska", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    if(!inputValid()){

                    }
                    Teacher teacher = (Teacher)list1.getSelectedValue();
                    if (teacher == null){
                        JOptionPane.showMessageDialog(oldFrame, "Ispunite sva polja!", "Greska", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    Subject subject = createSubjectFromInput(subjectDaoSQL, teacher, lista_preduslova);
                    if(subject_exists(subjectDaoSQL)){
                        JOptionPane.showMessageDialog(oldFrame, "Predmet vec postoji u bazi!","Greska",JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    subjectDaoSQL.addSubject(subject);
                    String ispis = "Predmet: \n"+ subject.getResult(teacher.toString()) + "\n" + "Uspjesno dodan!" ;
                    JOptionPane.showMessageDialog(oldFrame, ispis, "Uspjeh", JOptionPane.INFORMATION_MESSAGE);
                } catch (UniException e) {
                    throw new RuntimeException(e);
                }
                WindowCreator.create_window_unesi_predmet(oldFrame);
            }
        });
        textField3.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                String unos = textField3.getText();
                String[] ime_prezime = unos.split(" ");
                List<Teacher> lista_pregraga = new LinkedList<>();
                TeacherDaoSQL teacherDaoSQL = new TeacherDaoSQL("Teacher");
                DefaultListModel<Teacher> lista_nastavnika = new DefaultListModel<>();

                lista_pregraga = teacherDaoSQL.getTeacherLike(unos);
                if(ime_prezime.length >1){
                    lista_pregraga.add(teacherDaoSQL.getTeacherByNameLastName(ime_prezime[0], ime_prezime[1]));
                }
                for(int i = 0 ; i < lista_pregraga.size(); i++){
                    lista_nastavnika.addElement(lista_pregraga.get(i));
                }
                list1.setModel(lista_nastavnika);
            }
        });
        textField2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                SubjectDaoSQL subjectDaoSQL = new SubjectDaoSQL("Subject");
                DefaultListModel<Subject> lista_predmeta = new DefaultListModel<>();
                String unos = textField2.getText();
                List<Subject> lista_pretraga;
                try {
                    lista_pretraga = subjectDaoSQL.getSubjectLike(unos);
                } catch (UniException e) {
                    throw new RuntimeException(e);
                }
                for (int i = 0; i < lista_pretraga.size(); i++){
                    lista_predmeta.addElement(lista_pretraga.get(i));
                }
                list2.setModel(lista_predmeta);
            }
        });
        list3.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent listSelectionEvent) {

            Subject odabrani_predmet_za_brisanje = (Subject) list3.getSelectedValue();
            lista_preduslova_za_ispis.removeElement(odabrani_predmet_za_brisanje);
            lista_preduslova.remove(odabrani_predmet_za_brisanje);
            list3.setModel(lista_preduslova_za_ispis);



            }
        });
        dodajButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                Subject subject = (Subject) list2.getSelectedValue();
                if(subject == null){
                    return;
                }

                if(lista_preduslova_za_ispis.contains(subject)){
                    JOptionPane.showMessageDialog(oldFrame, "Predmet je vec dodan", "Greska", JOptionPane.ERROR_MESSAGE);
                    return;
                };
                lista_preduslova.add(subject);
                lista_preduslova_za_ispis.addElement(subject);
                list3.setModel(lista_preduslova_za_ispis);
            }
        });
    }

    private Subject createSubjectFromInput(SubjectDaoSQL subjectDaoSQL, Teacher teacher, List<Subject> lista_preduslova) {
        Subject subject = new Subject();
        try {
            int id = subjectDaoSQL.getMaxSubjectId();
            subject.setId(++id);
        } catch (RuntimeException | UniException e) {
            throw new RuntimeException(e);
        }
        StringBuilder priprema = new StringBuilder();
        if(lista_preduslova.size()>0) {
            priprema.append(lista_preduslova.get(0).getName());
            for (int i = 1; i < lista_preduslova.size(); i++) {
                priprema.append(", ");
                priprema.append(lista_preduslova.get(i).getName());
            }
        }
        String preduslovi = "";
        if(priprema.length()>0) {
            preduslovi = priprema.toString();
        }
        int godina = getGodinaInput();
        String semestar = getSemestarInput();
        subject.setSemester(semestar);
        subject.setPrerequisite_subject(preduslovi);
        subject.setGodina(godina);
        subject.setEcts(Integer.valueOf(textField4.getText()));
        subject.setName(textField1.getText());
        subject.setResponsibleTeacher(teacher.getId());

        return subject;
    }
    private boolean subject_exists( SubjectDaoSQL subjectDaoSQL){
        Subject subject = new Subject();
        subject = subjectDaoSQL.getSubjectByName(textField1.getText());
        if(subject.getName() == null){
            return false;
        }
        return true;
    }
    private int getGodinaInput(){
        int godina = 0;
        if(iRadioButton.isSelected()){
            godina = 1;
        }
        if(IIRadioButton.isSelected()){
            godina=2;
        }
        if(IIIRadioButton.isSelected()){
            godina = 3;
        }
        if(IVRadioButton.isSelected()){
            godina = 4;
        }
        return godina;
    }
    private String getSemestarInput(){
        String sem = null;
        if(ljetniRadioButton.isSelected()){
            sem = "Ljetni";
        }
        if(zimskiRadioButton.isSelected()){
            sem = "Zimski";
        }

        return sem;

    }
    private boolean inputValid(){
        if(list1.isSelectionEmpty()){
            return false;
        }
        if(textField1.getText().equals("")){
            return false;
        }
        if(textField4.getText().equals("")){
            return false;
        }
        return false;
    }
}
