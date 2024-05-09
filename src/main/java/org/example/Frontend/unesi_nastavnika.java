package org.example.Frontend;

import org.example.Dao.TeacherDaoSQL;
import org.example.Dao.UserDaoSQL;
import org.example.Entity.Teacher;
import org.example.Entity.User;
import org.example.Exceptions.UniException;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class unesi_nastavnika {
    private JTextField textField1;
    private JTextField textField2;
    private JRadioButton redovanRadioButton;
    private JRadioButton vanredanRadioButton;
    private JRadioButton docentRadioButton;
    private JButton nazadButton;
    private JButton potvrdiButton;
    public JPanel unesi_nastavnika;
public unesi_nastavnika(JFrame oldFrame) {
    ButtonGroup buttonGroup = new ButtonGroup();
    buttonGroup.add(redovanRadioButton);
    buttonGroup.add(vanredanRadioButton);
    buttonGroup.add(docentRadioButton);
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

            TeacherDaoSQL teacherDaoSQL = new TeacherDaoSQL("Teacher");
            UserDaoSQL userDaoSQL = new UserDaoSQL("User");
            int user_id = get_next_user_id(userDaoSQL);
            int teacher_id = get_next_teacher_id(teacherDaoSQL);
             if(teacher_exists(teacherDaoSQL)){
                   JOptionPane.showMessageDialog(oldFrame, "Nastavnik vec postoji u bazi!", "Greska", JOptionPane.ERROR_MESSAGE);
                   return;
                }
             User nastavnik_korisnik = create_user_from_input(user_id,teacher_id);
             Teacher nastavnik = create_teacher_from_input(user_id,teacher_id);
             try {
                userDaoSQL.addUser(nastavnik_korisnik);
                teacherDaoSQL.addTeacher(nastavnik);
             } catch (UniException e) {
                 throw new RuntimeException(e);
            }

          success_message(oldFrame, nastavnik);
             WindowCreator.create_window_unesi_nastavnika(oldFrame);
        }
    });
}
private boolean teacher_exists(TeacherDaoSQL teacherDaoSQL){
    Teacher nastavnik = new Teacher();
    nastavnik = teacherDaoSQL.getTeacherByNameLastName(textField1.getText(),textField2.getText());
    if(nastavnik.getId()!=0){
       return true;
    }
    return false;
}
private String generate_password(String ime, String prezime, int teacher_id){
    return ime.toUpperCase() + teacher_id + prezime.toUpperCase();
}
private void success_message(JFrame oldFrame, Teacher nastavnik){
    String potvrda = nastavnik.toResult();
    JOptionPane.showMessageDialog(oldFrame,"Nastavnik: \n" + potvrda + "uspjesno dodan!","Uspjeh",JOptionPane.INFORMATION_MESSAGE);
}
private User create_user_from_input(int user_id, int teacher_id){
    User nastavnik_korisnik = new User();
    String ime = textField1.getText();
    String prezime = textField2.getText();
    String username = ime.toLowerCase() + "." + prezime.toLowerCase();
    String password = generate_password(ime,prezime,teacher_id);
    nastavnik_korisnik.setUsername(username);
    nastavnik_korisnik.setId(user_id);
    nastavnik_korisnik.setRole(3);
    nastavnik_korisnik.setPassword(password);
    return nastavnik_korisnik;
}
private int get_next_user_id(UserDaoSQL userDaoSQL){
    int user_id = 0;
    try {
        user_id = userDaoSQL.getMaxUserId();
    } catch (UniException e) {
        throw new RuntimeException(e);
    }
    ++user_id;
    return user_id;
}
private int get_next_teacher_id(TeacherDaoSQL teacherDaoSQL){
    int teacher_id = 0;
    try {
        teacher_id = teacherDaoSQL.getMaxTeacherId();
    } catch (UniException e) {
        throw new RuntimeException(e);
    }
    ++teacher_id;
    return teacher_id;
}
private Teacher create_teacher_from_input(int user_id, int teacher_id){
    String ime = textField1.getText();
    String prezime = textField2.getText();
    String status = "null";
    if(redovanRadioButton.isSelected()){
        status = "Redovan";
    }
    else if(vanredanRadioButton.isSelected()){
        status = "Vanredan";
    }
    else if(docentRadioButton.isSelected()) {
        status = "Docent";
    }
    Teacher nastavnik = new Teacher();
    nastavnik.setName(ime);
    nastavnik.setLastName(prezime);
    nastavnik.setTitle(status);
    nastavnik.setId(teacher_id);
    nastavnik.setUserId(user_id);
    return nastavnik;
}
    private boolean valid_input(){
        if(textField1.getText().isEmpty()|| textField2.getText().isEmpty()){
            return false;
        }
        if(!redovanRadioButton.isSelected() && !vanredanRadioButton.isSelected() && !docentRadioButton.isSelected()){
            return false;
        }
        return true;
    }
}
