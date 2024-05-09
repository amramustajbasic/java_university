package org.example.Frontend;

import org.example.Dao.RequestDaoSQL;
import org.example.Dao.StudentDaoSQL;
import org.example.Dao.SubjectDaoSQL;
import org.example.Dao.TeacherDaoSQL;
import org.example.Entity.*;
import org.example.Exceptions.UniException;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedList;
import java.util.List;

public class PregledZahtjeva {
    public JPanel panel_zahtjevi;
    private JList list1;
    private JButton ispisiOdgovoreButton;
    private JList list2;
    private JButton ispisiZahtjeveButton;
    private JTextArea textArea1;
    private JTextArea textArea2;
    private JButton obrazlozenjeButton;
    private JButton obrazlozenjeButton1;
    private JButton nazadButton;

    public PregledZahtjeva(JFrame oldFrame, User user) {

        ispisiOdgovoreButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {

                textArea1.setText("");
                textArea2.setText("");

                Student student = new Student();
                StudentDaoSQL studentDaoSQL = new StudentDaoSQL("Student");
                student = studentDaoSQL.getStudentByUserId(user);

                List<Request> odgovori = new LinkedList<>();
                RequestDaoSQL requestDaoSQL = new RequestDaoSQL("Request");

                DefaultListModel<Request> lista = new DefaultListModel<>();


                try {
                    odgovori = requestDaoSQL.getAllAnswersByStudent(student.getId());
                } catch (UniException e) {
                    throw new RuntimeException(e);
                }

                for(int i = 0; i<odgovori.size(); i++){
                    lista.addElement(odgovori.get(i));
                }
                list1.setModel(lista);
            }
        });
        ispisiZahtjeveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                textArea1.setText("");
                textArea2.setText("");
                Student student = new Student();
                StudentDaoSQL studentDaoSQL = new StudentDaoSQL("Student");
                student = studentDaoSQL.getStudentByUserId(user);

                RequestDaoSQL requestDaoSQL = new RequestDaoSQL("Request");

                List<Request> upiti = new LinkedList<>();

                try {
                    upiti = requestDaoSQL.getAllRequestsByStudent(student.getId());
                } catch (UniException e) {
                    throw new RuntimeException(e);
                }

                DefaultListModel<Request> lista = new DefaultListModel<>();

                for(int i = 0; i<upiti.size(); i++){
                    lista.addElement(upiti.get(i));
                }
                list2.setModel(lista);

            }
        });
        obrazlozenjeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {

                textArea2.setEditable(false);
                textArea1.setEditable(false);
                Request request = new Request();
                request = (Request) list1.getSelectedValue();
                if(request == null){
                    JOptionPane.showMessageDialog(oldFrame,"Niste odabrali zahtjev");
                    return;
                }
                TeacherDaoSQL teacherDaoSQL = new TeacherDaoSQL("Teacher");
                Teacher teacher = new Teacher();
                SubjectDaoSQL subjectDaoSQL = new SubjectDaoSQL("Subject");
                Subject subject = new Subject();
                subject = subjectDaoSQL.getSubjectById(request.getSubject_id());
                //teacher = teacherDaoSQL.getTeacherByUserId(user);
                String imePrezime = new String("");
                try {
                    if(request.getStatus().equals("Odgovor_zamjena") || request.getStatus().equals("odgovor_zamjena")) {
                        teacher = teacherDaoSQL.getTeacherById(request.getVice_dean_id());
                        imePrezime = teacher.getName() + " " + teacher.getLastName() + " - " + "Prodekan";
                    }else{
                        teacher = teacherDaoSQL.getTeacherByTeacherId(request.getTeacher_id());
                        imePrezime = teacher.getName() + " " + teacher.getLastName();
                    }
                } catch (UniException e) {
                    throw new RuntimeException(e);
                }

                String predmet = subject.getName();
                textArea1.setText(imePrezime + "\n" + predmet + "\n"+ "Obrazlozenje:\n" + request.getMessage());
            }
        });
        obrazlozenjeButton1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                textArea2.setEditable(false);
                textArea1.setEditable(false);
                Request request = new Request();
                request = (Request) list2.getSelectedValue();
                if(request == null){
                    JOptionPane.showMessageDialog(oldFrame,"Niste odabrali zahtjev");
                    return;
                }
                TeacherDaoSQL teacherDaoSQL = new TeacherDaoSQL("Teacher");
                Teacher teacher = new Teacher();
                SubjectDaoSQL subjectDaoSQL = new SubjectDaoSQL("Subject");
                Subject subject = new Subject();
                subject = subjectDaoSQL.getSubjectById(request.getSubject_id());
                try {
                    teacher = teacherDaoSQL.getTeacherByTeacherId(request.getTeacher_id());
                } catch (UniException e) {
                    throw new RuntimeException(e);
                }
                String imePrezime = teacher.getName() + " " + teacher.getLastName();
                String predmet = subject.getName();
                textArea2.setText(predmet + ": " + request.getMessage());
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
