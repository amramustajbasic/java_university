package org.example.Frontend;

import org.example.Dao.StudentDaoSQL;
import org.example.Dao.UserDaoSQL;
import org.example.Entity.Student;
import org.example.Entity.User;
import org.example.Exceptions.UniException;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class unesi_studenta {
    private JTextField textField1;
    private JTextField textField2;
    private JButton nazadButton;
    private JButton potvrdiButton;
    public JPanel unesi_studenta;
    private JTextField textField3;
    private JRadioButton iRadioButton;
    private JRadioButton IIRadioButton;
    private JRadioButton IIIRadioButton;
    private JRadioButton IVRadioButton;

    public unesi_studenta(JFrame oldFrame) {
    ButtonGroup godina = new ButtonGroup();
    godina.add(IIRadioButton);
    godina.add(iRadioButton);
    godina.add(IIIRadioButton);
    godina.add(IVRadioButton);
    nazadButton.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent actionEvent) {
        WindowCreator.create_window_admin(oldFrame);
        }
    });
    potvrdiButton.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            if (!valid_input()) {
                JOptionPane.showMessageDialog(oldFrame, "Molimo ispunite prazna polja", "Upozorenje", JOptionPane.WARNING_MESSAGE);
                return;
            }
            UserDaoSQL userDaoSQL = new UserDaoSQL("User");
            StudentDaoSQL studentDaoSQL = new StudentDaoSQL("Student");
            int userId = getNextUserId(userDaoSQL);
            int indeks = getNextStudentId(studentDaoSQL);



            if(student_exists(studentDaoSQL)){
                JOptionPane.showMessageDialog(oldFrame, "Student vec postoji u bazi!", "Greska", JOptionPane.ERROR_MESSAGE);
                return;
            }
            User student_korisnik = create_user_from_input(userId,indeks);
            Student student = create_student_from_input(userId,indeks);

            try {
                userDaoSQL.addUser(student_korisnik);
                studentDaoSQL.addStudent(student);
                //ovjde se obavi automatska registracija gdje se uveze student sa subject_student tabelom
                //na taj nacin imamo predmete koje student trenutno slusa u prvoj godini automatski registrovane
                if(iRadioButton.isSelected()) {
                    studentDaoSQL.automatskaRegistracijaPredmeta(student);
                }
            } catch (UniException e) {
                throw new RuntimeException(e);
            }

            success_message(oldFrame, student);
            WindowCreator.create_window_unesi_studenta(oldFrame);
        }
    });
}
private boolean valid_input(){
    if(textField1.getText().isEmpty()|| textField2.getText().isEmpty() ){
        return false;
    }
    if(!(iRadioButton.isSelected() || IIRadioButton.isSelected() || IIIRadioButton.isSelected() || IVRadioButton.isSelected())){
        return false;
    }
    return true;
}

    private int getNextUserId(UserDaoSQL userDaoSQL) {
        try {
            return userDaoSQL.getMaxUserId() + 1;
        } catch (UniException e) {
            throw new RuntimeException(e);
        }
    }
private String generate_password(String ime, String prezime, int indeks){
    return ime.toUpperCase() + indeks + prezime.toUpperCase();
}
    private int getNextStudentId(StudentDaoSQL studentDaoSQL) {
        try {
            return studentDaoSQL.getMaxStudentId() + 1;
        } catch (UniException e) {
            throw new RuntimeException(e);
        }
    }

    private boolean student_exists(StudentDaoSQL studentDaoSQL){
        Student student = new Student();
        student = studentDaoSQL.getStudentByNameLastName(textField1.getText(),textField2.getText());
        if(student.getId()!=0){
            return true;
        }
        return false;
    }
    private User create_user_from_input(int user_id, int teacher_id){
        User student_korisnik = new User();
        String ime = textField1.getText();
        String prezime = textField2.getText();
        String username = ime.toLowerCase() + "." + prezime.toLowerCase();
        String password = generate_password(ime,prezime,teacher_id);
        student_korisnik.setUsername(username);
        student_korisnik.setId(user_id);
        student_korisnik.setRole(2);
        student_korisnik.setPassword(password);
        return student_korisnik;
    }
    private Student create_student_from_input(int userId, int indeks){
        Student student = new Student();
        String ime = textField1.getText();
        String prezime = textField2.getText();
        String status = "Prvi put";

        int godina = getGodinaInput();


        student.setId(indeks);
        student.setUserId(userId);
        student.setName(ime);
        student.setLastName(prezime);
        student.setStatus(status);
        student.setYearOfStudy(godina);
        return student;
    }
    private void success_message(JFrame oldFrame,Student student){
        String potvrda = student.toRezultat();
        JOptionPane.showMessageDialog(oldFrame,"Student: \n" + potvrda + "uspjesno dodan!","Uspjeh",JOptionPane.INFORMATION_MESSAGE);
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
}
