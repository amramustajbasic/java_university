package org.example.Frontend;

import org.example.Dao.TeacherDaoSQL;
import org.example.Dao.UserDaoSQL;
import org.example.Entity.Student;
import org.example.Entity.Teacher;
import org.example.Entity.User;
import org.example.Exceptions.UniException;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class unesi_prodekana {
    public JPanel panel_unesi_prodekana;
    private JTextField textField1;
    private JTextField textField2;
    private JButton potvrdiButton;
    private JButton nazadButton;
    private JList list1;

    public unesi_prodekana(JFrame oldFrame) {
        TeacherDaoSQL teacherDaoSQL = new TeacherDaoSQL("Teacher");
        UserDaoSQL userDaoSQL = new UserDaoSQL("User");
        User stari_prodekan = userDaoSQL.getOldProdekan();
        Teacher stari_prodekan_nastavnik;
        try {
         stari_prodekan_nastavnik = teacherDaoSQL.getTeacherById(stari_prodekan.getId());
        } catch (UniException e) {
            throw new RuntimeException(e);
        }
        textField2.setText(stari_prodekan_nastavnik.getName() + " " + stari_prodekan_nastavnik.getLastName());
        nazadButton.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            WindowCreator.create_window_admin(oldFrame);
        }
    });
    potvrdiButton.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            Teacher odabrani_nastavnik = (Teacher) list1.getSelectedValue();
            User odabrani_korisnik = userDaoSQL.getUserById(odabrani_nastavnik.getUserId());

            if(odabrani_korisnik.getRole()==1){
                JOptionPane.showMessageDialog(oldFrame,"Odabrali ste trenutnog prodekana", "Greska", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if(odabrani_korisnik.getRole()!=3){
                JOptionPane.showMessageDialog(oldFrame,"Student ne moze biti prodekan", "Greska", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (odabrani_korisnik.getId()==0){
                int odgovor =  JOptionPane.showConfirmDialog(oldFrame, "Nastavnik nije pronadjen, zelite li ga sada dodati?","Obavijest",JOptionPane.WARNING_MESSAGE);
                if(odgovor == JOptionPane.YES_OPTION){
                    WindowCreator.create_window_unesi_nastavnika(oldFrame);
                }
                return;
            }

            int rez = JOptionPane.showConfirmDialog(oldFrame, "Jeste li sigurni da zelite: " + odabrani_nastavnik.toResult() + "postaviti za prodekana?", "Potvrda", JOptionPane.WARNING_MESSAGE);
            if(rez == JOptionPane.YES_OPTION) {
                userDaoSQL.updateUser_role(stari_prodekan,3);
                userDaoSQL.updateUser_role(odabrani_korisnik,1);
            }
            else {
                return;
            }
                success_message(oldFrame, odabrani_nastavnik);
            WindowCreator.create_window_unesi_prodekana(oldFrame);

        }
    });
        textField1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                String unos = textField1.getText();
                String[] ime_prezime = unos.split(" ");
                DefaultListModel<Teacher> lista_nastavnika = new DefaultListModel<>();
                TeacherDaoSQL teacherDaoSQL = new TeacherDaoSQL("Teacher");
                List<Teacher> lista_pregraga= teacherDaoSQL.getTeacherLike(unos);
                if(ime_prezime.length >1){
                    lista_pregraga.add(teacherDaoSQL.getTeacherByNameLastName(ime_prezime[0], ime_prezime[1]));
                }
                for(int i = 0 ; i < lista_pregraga.size(); i++){
                    lista_nastavnika.addElement(lista_pregraga.get(i));
                }
                list1.setModel(lista_nastavnika);
            }
        });
    }
    private void success_message(JFrame oldFrame, Teacher nastavnik){
        String potvrda = nastavnik.toResult();
        JOptionPane.showMessageDialog(oldFrame,"Novi prodekan: \n" + potvrda + "uspjesno azuriran!","Uspjeh",JOptionPane.INFORMATION_MESSAGE);
    }
}
