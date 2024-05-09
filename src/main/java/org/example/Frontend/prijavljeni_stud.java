package org.example.Frontend;

import org.example.Dao.*;
import org.example.Entity.*;
import org.example.Exceptions.UniException;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedList;
import java.util.List;

public class prijavljeni_stud {
    public JPanel panel_prijavljeni_stud;
    private JList list1;
    private JButton nazadButton1;
    private JButton osvjezi;
    private JList list2;
    private JCheckBox tekucaAkademskaGodinaCheckBox;
    private JCheckBox narednaAkademskaGodinaCheckBox;
    private JTextField textField1;
    private JList list3;
    private JTextField index;
    private JTextField imeiprezime;
    private JTextField ocjena;
    private JButton podaciOStudentuButton;
    private JButton sviPredmetiButton;
    private JTextField predmet;
    private JTextField textField2;
    private JTextField textField3;


    private SubjectDaoSQL subjectDaoSQL = new SubjectDaoSQL("Subject");
    private TeacherDaoSQL teacherDaoSQL = new TeacherDaoSQL("Teacher");
    private StudentDaoSQL studentDaoSQL = new StudentDaoSQL("Student");

    public prijavljeni_stud(JFrame oldFrame, User user) {

        if(!(narednaAkademskaGodinaCheckBox.isSelected())){
            list2.removeAll();
        }
        if(!(tekucaAkademskaGodinaCheckBox.isSelected())){
            list1.removeAll();
        }


        osvjezi.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                List<Student> students = new LinkedList<>();
                List<Student> registrovani = new LinkedList<>();
                DefaultListModel<Student> model = new DefaultListModel<>();
                DefaultListModel<Student> model1 = new DefaultListModel<>();

                try {
                    students = studentDaoSQL.getAll();
                    registrovani = studentDaoSQL.getAllEnrolledNextYear();
                } catch (UniException e) {
                    throw new RuntimeException(e);
                }

                for(int i = 0; i<students.size();i++){
                    model.addElement(students.get(i));
                }
                for(int i = 0; i<registrovani.size();i++){
                    model1.addElement(registrovani.get(i));
                }
                list1.setModel(model);
                list2.setModel(model1);
                narednaAkademskaGodinaCheckBox.setSelected(false);
                tekucaAkademskaGodinaCheckBox.setSelected(false);

            }
            });
        nazadButton1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                WindowCreator.create_window_prodekan(oldFrame,user);
            }
        });
        textField1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                Subject subject = new Subject();
                //SubjectDaoSQL subjectDaoSQL = new SubjectDaoSQL("Subject");
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

                list3.setModel(list);
            }
        });
        tekucaAkademskaGodinaCheckBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                List<Student> studentiTekuca = new LinkedList<>();
                Subject subject = new Subject();
                subject = (Subject) list3.getSelectedValue();

                if(subject == null) {

                    try {
                        studentiTekuca = studentDaoSQL.getAllEnrolled();
                    } catch (UniException e) {
                        throw new RuntimeException(e);
                    }
                }
                if(subject!=null){
                    try {
                        studentiTekuca = studentDaoSQL.getAllEnrolled(subject.getId());
                    } catch (UniException e) {
                        throw new RuntimeException(e);
                    }
                }
                DefaultListModel<Student> lista_model = new DefaultListModel<>();
                if(studentiTekuca.size()==0){
                    list1.setModel(lista_model);
                }
                List<Student> distinctList = new LinkedList<>();

                if (studentiTekuca.size() > 0) {
                    distinctList.add(studentiTekuca.get(0));
                    for (int i = 1; i < studentiTekuca.size(); i++) {
                        if (!(studentiTekuca.get(i).equals(studentiTekuca.get(i - 1)))) {
                            distinctList.add(studentiTekuca.get(i));
                        }
                    }


                    for (int i = 0; i < distinctList.size(); i++) {
                        lista_model.addElement(distinctList.get(i));
                    }
                    list1.setModel(lista_model);
                }
            }
        });
        narednaAkademskaGodinaCheckBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                Subject subject = new Subject();
                subject = (Subject)list3.getSelectedValue();
                List<Student> registrovani = new LinkedList<>();
                try {
                    if(subject == null){
                        registrovani = studentDaoSQL.getAllEnrolledNextYear();
                    }
                    if(subject!=null){
                        registrovani = studentDaoSQL.getAllEnrolledOnSubject(subject.getId());
                        }
                    } catch (UniException ex) {
                    throw new RuntimeException(ex);
                }

                DefaultListModel<Student> lista_model = new DefaultListModel<>();
                if(registrovani.size() == 0){
                    list2.setModel(lista_model);
                }

                List<Student> distinctList = new LinkedList<>();

                if (registrovani.size() > 0) {
                    distinctList.add(registrovani.get(0));
                    for (int i = 1; i < registrovani.size(); i++) {
                        if (!(registrovani.get(i).equals(registrovani.get(i - 1)))) {
                            distinctList.add(registrovani.get(i));
                        }
                    }



                    for (int i = 0; i < distinctList.size(); i++) {
                        lista_model.addElement(distinctList.get(i));
                    }
                    list2.setModel(lista_model);
                }
            }
        });

        podaciOStudentuButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                Student studentTek = new Student();
                studentTek = (Student) list1.getSelectedValue();

                Student studentReg = (Student)list2.getSelectedValue();

                Subject subject = new Subject();
                subject = (Subject)list3.getSelectedValue();

                Grade grade = new Grade();
                GradeDaoSQL gradeDaoSQL = new GradeDaoSQL("Grade");


                if(subject == null && studentReg == null){
                    JOptionPane.showMessageDialog(oldFrame,"Niste odabrali sve za ispis podataka o prijavljenom studentu ");
                    return;
                }
                if(subject == null && studentTek == null){
                    JOptionPane.showMessageDialog(oldFrame,"Niste odabrali sve za ispis podataka o studentu tekuce akatemske godine ");
                    return;
                }

                if(studentTek != null && subject != null){
                    list2.clearSelection();
                    grade = gradeDaoSQL.getGradeById(studentTek.getId(),subject.getId());
                    index.setText(String.valueOf(studentTek.getId()));
                    imeiprezime.setText(studentTek.getName() + " " + studentTek.getLastName());
                    predmet.setText(subject.getName());
                    ocjena.setText(String.valueOf(grade.getGrade()));
                }
                if(studentReg != null && subject != null){
                    list1.clearSelection();
                    grade = gradeDaoSQL.getGradeById(studentReg.getId(),subject.getId());
                    index.setText(String.valueOf(studentReg.getId()));
                    imeiprezime.setText(studentReg.getName() + " " + studentReg.getLastName());
                    predmet.setText(subject.getName());
                    ocjena.setText(String.valueOf(grade.getGrade()));
                }
            }
        });
        sviPredmetiButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                List<Subject> subjects = new LinkedList<>();
                DefaultListModel<Subject> model = new DefaultListModel<>();
                try {
                    subjects = subjectDaoSQL.getAll();
                } catch (UniException e) {
                    throw new RuntimeException(e);
                }
                for(int i = 0; i< subjects.size(); i++){
                    model.addElement(subjects.get(i));
                }
                list3.setModel(model);
            }
        });
        textField2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {

                List<Student> studentiTekuca = new LinkedList<>();
                Subject subject = new Subject();
                subject = (Subject) list3.getSelectedValue();

                if(subject == null) {

                    try {
                        studentiTekuca = studentDaoSQL.getAllEnrolled();
                    } catch (UniException e) {
                        throw new RuntimeException(e);
                    }
                }
                if(subject!=null){
                    try {
                        studentiTekuca = studentDaoSQL.getAllEnrolled(subject.getId());
                    } catch (UniException e) {
                        throw new RuntimeException(e);
                    }
                }
                List<Student> filter = new LinkedList<>();
                //searched = studentDaoSQL.getStudentLike(textField2.getText());
                //searched = studentDaoSQL.getStudentByNameLastName(name,lastName);

                String imeIprezime = textField2.getText();
                String[] nameLastName = imeIprezime.split("\\s");
                String name,last_name;
                //int id;
                List<Student> searched = new LinkedList<>();
                DefaultListModel<Student> lista = new DefaultListModel<>();

                if(nameLastName.length == 2) {
                    name = nameLastName[0];
                    last_name = nameLastName[1];
                    searched = studentDaoSQL.getStudentsByNameLastName(name, last_name);

                }
                if(nameLastName.length ==1 ){
                    name = nameLastName[0];
                    searched = studentDaoSQL.getStudentsByName(name);
                }

                for (Student student1 : studentiTekuca) {
                    // Iterate over elements in list2
                    for (Student student2 : searched) {
                        // Compare the IDs of the students
                        if (student1.getId() == student2.getId()) {
                            filter.add(student1);
                            break;
                        }
                    }
                }

                DefaultListModel<Student> lista_model = new DefaultListModel<>();

                for (int i = 0; i < filter.size(); i++) {
                    lista_model.addElement(filter.get(i));
                }
                //System.out.println(searched);
                list1.setModel(lista_model);
            }
        });
        textField3.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                List<Student> studentiNaredna = new LinkedList<>();
                Subject subject = new Subject();
                subject = (Subject) list3.getSelectedValue();

                if(subject == null) {

                    try {
                        studentiNaredna = studentDaoSQL.getAllEnrolledNextYear();
                    } catch (UniException e) {
                        throw new RuntimeException(e);
                    }
                }
                if(subject!=null){
                    try {
                        studentiNaredna = studentDaoSQL.getAllEnrolledOnSubject(subject.getId());
                    } catch (UniException e) {
                        throw new RuntimeException(e);
                    }
                }
                List<Student> filter = new LinkedList<>();
                //searched = studentDaoSQL.getStudentLike(textField2.getText());
                //searched = studentDaoSQL.getStudentByNameLastName(name,lastName);

                String imeIprezime = textField3.getText();
                String[] nameLastName = imeIprezime.split("\\s");
                String name,last_name;
                //int id;
                List<Student> searched = new LinkedList<>();
                DefaultListModel<Student> lista = new DefaultListModel<>();

                if(nameLastName.length == 2) {
                    name = nameLastName[0];
                    last_name = nameLastName[1];
                    searched = studentDaoSQL.getStudentsByNameLastName(name, last_name);

                }
                if(nameLastName.length ==1 ){
                    name = nameLastName[0];
                    searched = studentDaoSQL.getStudentsByName(name);
                }

                for (Student student1 : studentiNaredna) {
                    // Iterate over elements in list2
                    for (Student student2 : searched) {
                        // Compare the IDs of the students
                        if (student1.getId() == student2.getId()) {
                            filter.add(student1);
                            break;
                        }
                    }
                }

                DefaultListModel<Student> lista_model = new DefaultListModel<>();

                for (int i = 0; i < filter.size(); i++) {
                    lista_model.addElement(filter.get(i));
                }
                //System.out.println(searched);
                list2.setModel(lista_model);
            }
        });
    }

}
