package org.example.Frontend;

import org.example.Dao.TeacherDaoSQL;
import org.example.Entity.Teacher;
import org.example.Entity.User;
import org.example.Exceptions.UniException;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.util.LinkedList;
import java.util.List;

public class zvanje_nast {
    public JPanel panel_zvanje;
    private JTextField textField1;
    private JList<Teacher> list1;
    private JComboBox comboBox1;
    private JButton potvrdiButton;
    private JButton nazadButton;
    private JButton ucitajNastavnikeButton;
    private JTextField textField2;

    public zvanje_nast(JFrame oldFrame, User user) {
    nazadButton.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            WindowCreator.create_window_prodekan(oldFrame,user);
        }
    });

    potvrdiButton.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent actionEvent) {
                Teacher t = new Teacher();
                t = list1.getSelectedValue();
               String zvanje = new String();
               zvanje = (String) comboBox1.getSelectedItem();

                if(t == null && zvanje == null){
                    JOptionPane.showMessageDialog(oldFrame,"Niste odabrali nastavnika, kao ni zvanje", "Upozorenje",JOptionPane.WARNING_MESSAGE);
                    return;
                }
               if(t == null){
                   JOptionPane.showMessageDialog(oldFrame,"Niste odabrali nastavnika", "Upozorenje",JOptionPane.WARNING_MESSAGE);
                    return;
               }
               if(zvanje == null){
                   JOptionPane.showMessageDialog(oldFrame,"Niste odabrali zvanje nastavnika", "Upozorenje",JOptionPane.WARNING_MESSAGE);
                   return;
               }
               String zvanjePrije = t.getTitle();
               if(zvanjePrije.equals(zvanje)){
                   JOptionPane.showMessageDialog(oldFrame,t.getName() + " "+ t.getLastName() + " je vec " + zvanjePrije);
                   return;
               }

                int id = t.getId();
               //System.out.println(id);
               TeacherDaoSQL teacher = new TeacherDaoSQL("Teacher");
               teacher.updateTeacher_zvanje(t,zvanje,id);

               JOptionPane.showMessageDialog(oldFrame, "Azurirali ste zvanje nastavnika "+ t.getName() + " " + t.getLastName() + " kao "+ zvanje, "Uspjeh", JOptionPane.INFORMATION_MESSAGE);
                textField2.setText(zvanje);
        }
    });
        ucitajNastavnikeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>();
                comboBox1.setModel(model);
               List<Teacher> lista = new LinkedList<>();
               TeacherDaoSQL test = new TeacherDaoSQL("Teacher");
                try {
                    lista = test.getAll();
                } catch (UniException e) {
                    throw new RuntimeException(e);
                }
                DefaultListModel<Teacher> lista_model = new DefaultListModel<>();
                for(int i = 0 ; i < lista.size(); i++) {
                    lista_model.addElement(lista.get(i));
                }
                list1.setModel(lista_model);
            }
        });


        textField1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {

                TeacherDaoSQL teacherQuery = new TeacherDaoSQL("Teacher");
                Teacher teacher = new Teacher();

                String imeIprezime = textField1.getText();
                String[] nameLastName = imeIprezime.split("\\s");
                String name,last_name;
                //int id;
                List<Teacher> listaNastavnika = new LinkedList<>();
                DefaultListModel<Teacher> lista = new DefaultListModel<>();

                if(nameLastName.length == 2) {
                    name = nameLastName[0];
                    last_name = nameLastName[1];
                    listaNastavnika = teacherQuery.getTeacherByNameAndLastName(name, last_name);

                }
                if(nameLastName.length ==1 ){
                    name = nameLastName[0];
                    listaNastavnika = teacherQuery.getTeacherByName(name);
                }
                if(listaNastavnika.size()>0){
                    for(int i= 0; i<listaNastavnika.size();i++){
                        lista.addElement(listaNastavnika.get(i));
                    }
                }
                if(listaNastavnika.size() == 0){
                    JOptionPane.showMessageDialog(oldFrame,"Ne postoje u sistemu nastavnici sa unesenim imenom ili prezimenom");
                }
                list1.setModel(lista);
            }
        });
        comboBox1.addPopupMenuListener(new PopupMenuListener() {
            @Override
            public void popupMenuWillBecomeVisible(PopupMenuEvent popupMenuEvent) {
                DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>();
                String [] roles = {"Redovan","Vanredan","Docent"};
                for (int i = 0; i < roles.length; i++){
                    model.insertElementAt(roles[i],i);
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
        list1.addComponentListener(new ComponentAdapter() {
        });
        list1.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent listSelectionEvent) {

                Teacher teacher = (Teacher) list1.getSelectedValue();
                if(teacher == null){
                    return;
                }
                textField2.setText(teacher.getTitle());
            }
        });
    }
}
