package org.example.Frontend;

import org.example.Dao.*;
import org.example.Entity.*;
import org.example.Exceptions.UniException;

import javax.swing.*;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.MouseAdapter;
import java.beans.PropertyChangeListener;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class zahtjevi {
    public JPanel panel_zahtjevi;
    private JButton prihvatiButton;
    private JButton odbijButton;
    private JTextArea textArea1;
    private JComboBox comboBox1;
    private JButton obrazlozenjeZahtjevaButton;
    private JTextField textField1;
    private JButton nazadButton;
    private JTextField textField2;
    private JList list1;
    private JButton zahtjeviButton;


    public zahtjevi(JFrame oldFrame,User user) {

        comboBox1.addPopupMenuListener(new PopupMenuListener() {
            @Override
            public void popupMenuWillBecomeVisible(PopupMenuEvent popupMenuEvent) {

                //list1.removeAll();

                DefaultComboBoxModel<Student> model = new DefaultComboBoxModel<>();

                StudentDaoSQL studentDaoSQL = new StudentDaoSQL("Student");
                Student student = new Student();
                //List<Student> students = new LinkedList<>();
                List<Student> studentsRequests = new LinkedList<>();

                try {
                    studentsRequests = studentDaoSQL.getAllStudentsRequestForViceDean();
                } catch (UniException e) {
                    throw new RuntimeException(e);
                }

                List<Student> distinctList = new LinkedList<>();

                if(studentsRequests.size()>0) {
                    distinctList.add(studentsRequests.get(0));
                    for (int i = 1; i < studentsRequests.size(); i++) {
                        if (!(studentsRequests.get(i).equals(studentsRequests.get(i - 1)))) {
                            distinctList.add(studentsRequests.get(i));
                        }
                    }

                    for (int i = 0; i < distinctList.size(); i++) {
                        model.insertElementAt(distinctList.get(i), i);
                    }

                    comboBox1.setModel(model);
                }else {
                    JOptionPane.showMessageDialog(oldFrame,"Ne postoje studenati sa zahtjevom");
                    return;
                }

            }



            @Override
            public void popupMenuWillBecomeInvisible(PopupMenuEvent popupMenuEvent) {

            }

            @Override
            public void popupMenuCanceled(PopupMenuEvent popupMenuEvent) {

            }
        });

        obrazlozenjeZahtjevaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {


                Request request = new Request();
                request = (Request) list1.getSelectedValue();
//                String message = request.getMessage();
                Student student = new Student();

                student = (Student) comboBox1.getSelectedItem();

                RequestDaoSQL requestDaoSQL = new RequestDaoSQL("Request");


                if (student == null || request == null){
                    JOptionPane.showMessageDialog(null, "Niste odabrali podatke", "Upozorenje", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                if(student.getId() != request.getStudent_id()){
                    JOptionPane.showMessageDialog(null, "Pogresan odabir", "Upozorenje", JOptionPane.WARNING_MESSAGE);
                }
                    textArea1.setText("");
                    textArea1.append(request.getMessage());
                    Subject subject = new Subject();
                    SubjectDaoSQL subjectDaoSQL = new SubjectDaoSQL("Subject");
                    subject = subjectDaoSQL.getSubjectById(request.getSubject_id());


                //textField2.setText(subject.getName());
                    int subjectChanged = subject.getId();
                    int subjectFirst = requestDaoSQL.getFirstSubID(student,subjectChanged);
                    //Predmet koji je bio prije
                    Subject subject1 = subjectDaoSQL.getSubjectById(subjectFirst);



                textField2.setText(subject1.getName() + "<->" + subject.getName());

            }

        });
        prihvatiButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {

                Student student = (Student) comboBox1.getSelectedItem();

                Request request = new Request();
                RequestDaoSQL requestDaoSQL = new RequestDaoSQL("Request");
                request = (Request)list1.getSelectedValue();
                if(request == null){
                    JOptionPane.showMessageDialog(oldFrame, "Morate odabrati zahtjev ukoliko postoje");
                    return;
                }

                Enrollment enrollment = new Enrollment();
                EnrollmentDaoSQL enrollmentDaoSQL = new EnrollmentDaoSQL("Enrollment");

                String message =  textField1.getText();

                if(message.length() < 10){
                    JOptionPane.showMessageDialog(oldFrame, "Vase obrazlozenje mora imati min 10 karaktera");
                    return;
                }

                User prodekan = new User();
                UserDaoSQL userDaoSQL = new UserDaoSQL("User");
                prodekan = userDaoSQL.getOldProdekan();

                try {
                    requestDaoSQL.addRequestStatus(request,student.getId(),request.getSubject_id(),message,"Odgovor_zamjena",request.getTeacher_id(),prodekan.getId());
                    requestDaoSQL.deleteRequest(request);
                } catch (UniException e) {
                    throw new RuntimeException(e);
                }

                textArea1.setText("");

                student = (Student) comboBox1.getSelectedItem();
                int studentId = student.getId();
                List<Request> lista = new LinkedList<>();


                try {
                    lista = requestDaoSQL.getAllByStudent(studentId);
                } catch (UniException e) {
                    throw new RuntimeException(e);
                }
                List<Request> listaFilter = new LinkedList<>();
                for (int i = 0; i<lista.size(); i++){
                    if(lista.get(i).getStatus().equals("Upit_zamjena") || lista.get(i).getStatus().equals("upit_zamjena"))
                        listaFilter.add(lista.get(i));
                }

                DefaultListModel<Request> lista_model = new DefaultListModel<>();

                for (int i = 0; i < listaFilter.size(); i++) {
                    lista_model.addElement(listaFilter.get(i));
                }
                list1.setModel(lista_model);

                Subject subject = new Subject();
                SubjectDaoSQL subjectDaoSQL = new SubjectDaoSQL("Subject");
                subject = subjectDaoSQL.getSubjectById(request.getSubject_id());


                //textField2.setText(subject.getName());
                int subjectChanged = subject.getId();
                int subjectFirst = requestDaoSQL.getFirstSubID(student,subjectChanged);

                enrollmentDaoSQL.updateEnrollmentChangeSubject(student, subjectFirst, subjectChanged);
                textField1.setText("");
                textArea1.setText("");
                textField2.setText("");
                WindowCreator.create_zahtjevi(oldFrame,user);
            }
        });

        odbijButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {

                Student student = (Student) comboBox1.getSelectedItem();

                String answerText = textField1.getText();

                Request request = new Request();
                RequestDaoSQL requestDaoSQL = new RequestDaoSQL("Request");
                request = (Request) list1.getSelectedValue();

                if(request == null){
                    JOptionPane.showMessageDialog(oldFrame, "Morate odabrati zahtjev ukoliko postoje");
                    return;
                }

                UserDaoSQL userDaoSQL = new UserDaoSQL("User");
                User prodekanUser = new User();
                prodekanUser = userDaoSQL.getOldProdekan();

                Subject subject = new Subject();
                SubjectDaoSQL subjectDaoSQL = new SubjectDaoSQL("Subject");

                int index;
                index = list1.getSelectedIndex();

                if(answerText.length()<10){
                    JOptionPane.showMessageDialog(oldFrame, "Ne mozete odbiti zahtjev bez unosa tekstualnog obrazlozenja od min 10 karaktera", "Upozorenje", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                if(student != null && request != null) {
                    try {

                        subject = subjectDaoSQL.getSubjectById(request.getSubject_id());
                        requestDaoSQL.addRequestStatus(request, student.getId(), request.getSubject_id(), answerText, "Odgovor_zamjena", subject.getResponsibleTeacher(), prodekanUser.getId());
                        requestDaoSQL.deleteRequest(request);

                        list1.remove(index);

                    } catch (UniException e) {
                        throw new RuntimeException(e);
                    }

                }
                textArea1.setText("");

                //Student student = new Student();
                student = (Student) comboBox1.getSelectedItem();
                int studentId = student.getId();
                List<Request> lista = new LinkedList<>();
                //RequestDaoSQL requestDaoSQL = new RequestDaoSQL("Request");

                try {
                    lista = requestDaoSQL.getAllByStudent(studentId);
                } catch (UniException e) {
                    throw new RuntimeException(e);
                }
                List<Request> listaFilter = new LinkedList<>();
                for (int i = 0; i<lista.size(); i++){
                    if(lista.get(i).getStatus().equals("Upit_zamjena") || lista.get(i).getStatus().equals("upit_zamjena"))
                        listaFilter.add(lista.get(i));
                }

                DefaultListModel<Request> lista_model = new DefaultListModel<>();

                for (int i = 0; i < listaFilter.size(); i++) {
                    lista_model.addElement(listaFilter.get(i));
                }
                list1.setModel(lista_model);
                textField1.setText("");
                textArea1.setText("");
                textField2.setText("");
                WindowCreator.create_zahtjevi(oldFrame,user);

            }
        });

        nazadButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                WindowCreator.create_window_prodekan(oldFrame,user);
            }
        });

        zahtjeviButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {



                textArea1.setText("");
                textField2.setText("");

                Student student = new Student();
                student = (Student) comboBox1.getSelectedItem();

                if (student == null) {
                    JOptionPane.showMessageDialog(oldFrame, "Morate odabrati studenta", "Upozorenje", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                int studentId = student.getId();
                List<Request> lista = new LinkedList<>();
                RequestDaoSQL requestDaoSQL = new RequestDaoSQL("Request");

                try {
                    lista = requestDaoSQL.getAllByStudent(studentId);
                } catch (UniException e) {
                    throw new RuntimeException(e);
                }
                List<Request> listaFilter = new LinkedList<>();
                for (int i = 0; i<lista.size(); i++){
                    if(lista.get(i).getStatus().equals("Upit_zamjena") || lista.get(i).getStatus().equals("upit_zamjena")) {

                        listaFilter.add(lista.get(i));
                    }
                }

                DefaultListModel<Request> lista_model = new DefaultListModel<>();

                for (int i = 0; i < listaFilter.size(); i++) {
                    lista_model.addElement(listaFilter.get(i));
                }
                list1.setModel(lista_model);

            }
        });
    }


}
