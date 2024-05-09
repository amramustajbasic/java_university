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
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class plan_nastave {
    public JPanel panel_plan_nastave;
    private JList list1;
    private JComboBox comboBox1;
    private JComboBox comboBox2;
    private JButton potvrdiButton;
    private JButton nazadButton;
    private JTextField textField1;
    private JButton dodajPredavacaButton;
    private JButton ucitajPredmeteButton;
    private JButton izbaciButton;
    private JList list2;
    private JTextField textField2;

    private String predavaci = new String("");
    private List<Teacher> teachers = new LinkedList<>();
    private Subject oldChoice = new Subject();
    private  SubjectDaoSQL subjectDaoSQL = new SubjectDaoSQL("Subject");
    private  TeacherDaoSQL teacherDaoSQL = new TeacherDaoSQL("Teacher");
    private StudyProgramDaoSQL studyProgramDaoSQL = new StudyProgramDaoSQL("StudyProgram");
    private StudentDaoSQL studentDaoSQL = new StudentDaoSQL("Student");

    public plan_nastave(JFrame oldFrame, User user) {

        nazadButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                WindowCreator.create_window_prodekan(oldFrame,user);
            }
        });

        textField1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                Subject subject = new Subject();
                String predmetText = textField1.getText();

                DefaultListModel<Subject> list = new DefaultListModel<>();
                List<Subject> listaPredmeta = subjectDaoSQL.getSubjectsBySearch(predmetText);

                if(listaPredmeta.size()>0){
                    for(int i = 0; i<listaPredmeta.size(); i++){
                        list.addElement(listaPredmeta.get(i));
                    }
                }
                if(listaPredmeta.size() == 0){
                    JOptionPane.showMessageDialog(oldFrame,"Ne postoji u sistemu predmet sa unesenim nazivom");
                }

                list1.setModel(list);
            }
        });

        comboBox1.addPopupMenuListener(new PopupMenuListener() {
            @Override
            public void popupMenuWillBecomeVisible(PopupMenuEvent popupMenuEvent) {
                DefaultComboBoxModel<Teacher> model = new DefaultComboBoxModel<>();
                List<Teacher> lista = new LinkedList<>();
                //TeacherDaoSQL teacherDaoSQL = new TeacherDaoSQL("Teacher");
                    try {
                    lista = teacherDaoSQL.getAll();
                        model.addAll(lista);
                } catch (UniException e) {
                    throw new RuntimeException(e);
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
                DefaultComboBoxModel<Teacher> model = new DefaultComboBoxModel<>();
                List<Teacher> lista = new LinkedList<>();

                try {
                    lista = teacherDaoSQL.getAll();
                    model.addAll(lista);
                } catch (UniException e) {
                    throw new RuntimeException(e);
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

        potvrdiButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {


                Subject predmet = new Subject();
                predmet = (Subject) list1.getSelectedValue();

                Teacher nosilac = new Teacher();
                nosilac = (Teacher) comboBox2.getSelectedItem();

                StudyProgram studyProgram = new StudyProgram();
                List<StudyProgram> lista = new LinkedList<>();


                if(predmet == null && nosilac == null && predavaci.isEmpty()){
                    JOptionPane.showMessageDialog(oldFrame, "Niste odabrali trazene podatke za azuriranje", "Upozorenje", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                if(nosilac == null && predavaci.isEmpty()){
                    JOptionPane.showMessageDialog(oldFrame, "Niste odabrali trazene podatke za azuriranje predmeta " + predmet.getName() , "Upozorenje", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                if(predmet == null && nosilac == null){
                    JOptionPane.showMessageDialog(oldFrame, "Dodali ste predavaca/ce "+ predavaci + ", ali nemate predmet, kao ni odgovornog nsatavnika", "Upozorenje", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                if(predmet == null && predavaci.isEmpty()){
                    JOptionPane.showMessageDialog(oldFrame, "Odgovoran nastavnik je "+ nosilac.getName() + " " + nosilac.getLastName()+", ali morate odabrati predmet i njegove predavace", "Upozorenje", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                if(predmet == null){
                    JOptionPane.showMessageDialog(oldFrame, "Niste odabrali predmet", "Upozorenje", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                Teacher stariNosilac = new Teacher();
                try {
                    stariNosilac = teacherDaoSQL.getTeacherByTeacherId(predmet.getResponsibleTeacher());
                } catch (UniException e) {
                    throw new RuntimeException(e);
                }

                if(nosilac == null && stariNosilac == null){
                    JOptionPane.showMessageDialog(oldFrame, "Predmet mora imati odgovornog nastavnika", "Upozorenje", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                if(nosilac == null && stariNosilac != null){
                    JOptionPane.showMessageDialog(oldFrame, "Predmet ima odgovornog nastavnika, ali Vi za azuriranje plana nastave morate odabrati istog ili novog odgovornog nastavnika. ", "Upozorenje", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                if(predavaci.isEmpty() && nosilac == null){
                    JOptionPane.showMessageDialog(oldFrame, "Predmet mora imati svoje predavace", "Upozorenje", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                if(predavaci.isEmpty() && nosilac!=null){
                    JOptionPane.showMessageDialog(oldFrame,"Niste odabrali predavace, ali imate nosioca, koji je takodjer i predavac");
                }

                for(int i = 0;i<teachers.size();i++) {
                    if (nosilac.getId() == teachers.get(i).getId()) {
                        JOptionPane.showMessageDialog(oldFrame,"Vas odabrani nosilac ste odabrali za predavaca.\nPri dodavanju nosioca,on/a automatski postaje i predavac.\n" +
                                "Morate predavaca " + nosilac.getName() + " " + nosilac.getLastName() + " izbaciti iz liste");
                        return;
                    }
                }

                teachers.add(nosilac);
                predavaci += nosilac.getName() + " " + nosilac.getLastName();

                try {
                    if(studyProgramDaoSQL.StudyProgramExists(predmet.getId())){
                        studyProgramDaoSQL.updatePlanNastave(predmet,predavaci,nosilac);
                    }else{
                        studyProgramDaoSQL.insertPlanNastave(predmet,predavaci,nosilac);
                    }
                } catch (UniException e) {
                    throw new RuntimeException(e);
                }


                JOptionPane.showMessageDialog(oldFrame, "Uspjesno ste azurirali plan realizacije nastave za predmet " + predmet.getName()
                + "\n Predavac/i su: " + predavaci + ", a odgovoran nastavnik je " + nosilac.getName() + " " + nosilac.getLastName(), "Uspjeh", JOptionPane.INFORMATION_MESSAGE);

                textField2.setText(nosilac.getName() + " " + nosilac.getLastName());
            }
        });



        dodajPredavacaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {

                predavaci = "";

                Subject subject = new Subject();
                subject = (Subject) list1.getSelectedValue();

                if(subject == null){
                    JOptionPane.showMessageDialog(oldFrame,"Niste odabrali predmet");
                    return;
                }

                if(oldChoice != null){
                    if(!(oldChoice.getId() == subject.getId())){
                        if(teachers.size()>0) {
                            teachers.clear();
                            predavaci = "";
                        }
                    }
                }
                oldChoice = subject;

                DefaultListModel<Teacher> listaModel = new DefaultListModel<>();


                Teacher predavacDodat = new Teacher();
                predavacDodat = (Teacher) comboBox1.getSelectedItem();


//                String predavaci_str = new String("");
//                String[] predavaciArray = null;
//                String name = "";
//                String lastname = "";
//
//                try {
//                    predavaci_str = subjectDaoSQL.getTeachers(subject.getId());
//                } catch (UniException e) {
//                    throw new RuntimeException(e);
//                }
//                predavaciArray = predavaci_str.split(", ");
//
//
//                if(!(predavaci_str.isEmpty())) {
//
//                    for (int i = 0; i < predavaciArray.length; i++) {
//                        String[] both = predavaciArray[i].split(" ");
//                        name = both[0];
//                        lastname = both[1];
//                        teachers.add(teacherDaoSQL.getTeacherByNameLastName(name, lastname));
//                    }
//                }


                if(teachers.size()>0){
                    for (int i = 0; i<teachers.size();i++){
                        if(predavacDodat.getId() == teachers.get(i).getId()){
                            JOptionPane.showMessageDialog(oldFrame,"Predavac "+ predavacDodat.getName() +
                                    " " + predavacDodat.getLastName()+ " je vec na predmetu " + subject.getName());
                        return;
                        }
                    }
                }


//                if(!(predavaci.isEmpty())) {
//                    String[] predavaciArray = predavaci.split(", ");
//
//                    String name = null;
//                    String lastName = null;
//
//                    if (predavaciArray.length > 0) {
//                        for (int i = 0; i < predavaciArray.length; i++) {
//                            String[] both = predavaciArray[i].split(" ");
//                            name = both[0];
//                            lastName = both[1];
//                            teachers.add(teacherDaoSQL.getTeacherByNameLastName(name, lastName));
//                        }
//                    }
//                }


                teachers.add(predavacDodat);

                for(int i = 0; i<teachers.size();i++){
                        predavaci += teachers.get(i).getName() + " " + teachers.get(i).getLastName() + ", ";
                }
                System.out.println(predavaci);

                for (int i = 0; i< teachers.size(); i++){
                    listaModel.addElement(teachers.get(i));
                }

                list2.setModel(listaModel);





            }
        });
        ucitajPredmeteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                List<Subject> lista = new LinkedList<>();
                try {
                    lista = subjectDaoSQL.getAll();
                } catch (UniException e) {
                    throw new RuntimeException(e);
                }
                DefaultListModel<Subject> lista_model = new DefaultListModel<>();
                for(int i = 0 ; i < lista.size(); i++) {
                    lista_model.addElement(lista.get(i));
                }
                list1.setModel(lista_model);
            }

        });
        list1.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent listSelectionEvent) {
                DefaultListModel<Teacher> model = new DefaultListModel<>();
                Subject subject = (Subject) list1.getSelectedValue();

                if(teachers.size()>0) {
                    teachers.clear();
                    predavaci = "";
                }

                String predavaci_str = new String("");
                String[] predavaciArray = null;
                String name = "";
                String lastname = "";

                try {
                    predavaci_str = subjectDaoSQL.getTeachers(subject.getId());
                } catch (UniException e) {
                    throw new RuntimeException(e);
                }
                predavaciArray = predavaci_str.split(", ");


                if(!(predavaci_str.isEmpty())) {

                    for (int i = 0; i < predavaciArray.length; i++) {
                        String[] both = predavaciArray[i].split(" ");
                        name = both[0];
                        lastname = both[1];
                        teachers.add(teacherDaoSQL.getTeacherByNameLastName(name, lastname));
                    }
                }
                System.out.println(teachers);

                for (int i = 0; i<teachers.size(); i++) {
                    model.addElement(teachers.get(i));
                }
                Teacher teacher = new Teacher();
                //TeacherDaoSQL teacherDaoSQL1 = new TeacherDaoSQL("Teacher");
                try {
                    teacher = teacherDaoSQL.getTeacherByTeacherId(subject.getResponsibleTeacher());
                } catch (UniException e) {
                    throw new RuntimeException(e);
                }
                textField2.setText(teacher.getName() + " " + teacher.getLastName());
                list2.removeAll();
                list2.setModel(model);
            }
        });
        izbaciButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                    DefaultListModel<Teacher> model = new DefaultListModel<>();
                    Teacher teacher = new Teacher();
                    teacher = (Teacher) list2.getSelectedValue();

                    if(teacher == null){
                        JOptionPane.showMessageDialog(oldFrame,"Odaberite predavaca za izbacivanje");
                        return;
                    }

                    for(int i = 0; i<teachers.size();i++){
                        if(teacher.getId() == teachers.get(i).getId()){
                            teachers.remove(i);
                        }
                    }
                    for(int i = 0; i<teachers.size();i++){
                        predavaci += teachers.get(i).getName() + " " + teachers.get(i).getLastName() + ", ";
                    }
                    for(int i = 0; i< teachers.size();i++){
                        model.addElement(teachers.get(i));
                    }
                    list2.setModel(model);
            }
        });
    }
}

