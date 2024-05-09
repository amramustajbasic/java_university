package org.example.Frontend;


import org.example.Dao.StudentDaoSQL;
import org.example.Entity.Student;
import org.example.Entity.User;

import org.example.Dao.UserDaoSQL;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class login {
    private JTextField textField1;
    public JPanel panel_login;
    private JButton prijaviSeButton;
    private JPasswordField passwordField1;

    //public int loggedId;




    public JPasswordField getPasswordField1() {
        return passwordField1;
    }

    public JTextField getTextField1() {
        return textField1;
    }

    public login(JFrame oldFrame) {

    prijaviSeButton.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent actionEvent) {
           handleLogin(oldFrame);

        }
    });
    passwordField1.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            handleLogin(oldFrame);
        }
    });

}

    public void handleLogin(JFrame oldFrame) {
        String username = textField1.getText();
        String password = new String(passwordField1.getPassword());
        UserDaoSQL korisnikQuery = new UserDaoSQL("User");
        User korisnik = new User();
        korisnik = korisnikQuery.getUserByUsernameAndPass(username, password);
        Object[] options = {"Prodekan", "Nastavnik"};


        if(username.isEmpty() || password.isEmpty()){
            JOptionPane.showMessageDialog(null, "Empty field", "Login Error", JOptionPane.WARNING_MESSAGE);
            return;
        }else if (korisnik.getUsername() == null || korisnik.getPassword() == null){
            JOptionPane.showMessageDialog(null, "User not found", "Login Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        switch (korisnik.getRole()) {
            case 0: // admin
                WindowCreator.create_window_admin(oldFrame);
                break;
            case 1: {// prodekan}
                int selectedOption = JOptionPane.showOptionDialog(oldFrame, "Odaberite profil", "Popup Window",
                        JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, options, options[0]);
                    String selectedProfile = (String) options[selectedOption];
                    if (selectedProfile.equals("Prodekan")) {
                        WindowCreator.create_window_prodekan(oldFrame, korisnik);
                    } else if (selectedProfile.equals("Nastavnik")) {
                        WindowCreator.create_window_nastavnik(oldFrame, korisnik);
                    } else {
                        JOptionPane.showMessageDialog(oldFrame, "Greska", "Greska", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    break;
                }
            case 2: // student
                WindowCreator.create_window_student(oldFrame,korisnik);
                break;
            case 3: // nastavnik
                WindowCreator.create_window_nastavnik(oldFrame, korisnik);
                break;
        }
    }

}
